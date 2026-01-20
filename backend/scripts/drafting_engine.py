import os
import jinja2
from langchain_google_genai import ChatGoogleGenerativeAI
from langchain.prompts import PromptTemplate
from langchain.output_parsers import StructuredOutputParser, ResponseSchema
import logging
from datetime import date

# Configure Logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Constants
TEMPLATE_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "templates")

def load_template(template_name):
    """Loads a markdown template."""
    path = os.path.join(TEMPLATE_DIR, f"{template_name}.md")
    if not os.path.exists(path):
        raise FileNotFoundError(f"Template {template_name} not found.")
    with open(path, 'r') as f:
        return f.read()

def draft_document(user_context, template_type):
    """
    Drafts a document by filling values from user context.
    
    Args:
        user_context (str): "I lost my phone in Indiranagar..."
        template_type (str): "FIR_Complaint" or "RTI_Request"
    """
    logger.info(f"Drafting {template_type} for context: {user_context[:50]}...")
    
    # 1. Load Template content
    raw_template = load_template(template_type)
    
    # 2. Identify Variables (Rule-based or LLM-based schema definition would be better, 
    # but for MVP we define schemas for known templates)
    
    schemas = []
    if template_type == "RTI_Request":
        schemas = [
            ResponseSchema(name="department_name", description="Name of the government department"),
            ResponseSchema(name="department_address", description="Address of the department"),
            ResponseSchema(name="applicant_name", description="Name of the user"),
            ResponseSchema(name="applicant_address", description="Address of the user"),
            ResponseSchema(name="applicant_phone", description="Phone number of the user"),
            ResponseSchema(name="subject_matter", description="One line subject of the query"),
            ResponseSchema(name="period", description="Time period of information needed"),
            ResponseSchema(name="description_list", description="Bulleted list of specific questions"),
            ResponseSchema(name="delivery_mode", description="Post or In-Person (default Post)"),
            ResponseSchema(name="payment_details", description="IPO number or 'To be paid'"),
            ResponseSchema(name="payment_date", description="Date of payment"),
            ResponseSchema(name="place", description="City of applicant")
        ]
    elif template_type == "FIR_Complaint":
        schemas = [
            ResponseSchema(name="police_station_name", description="Name of the jurisdiction PS"),
            ResponseSchema(name="city_district", description="City and District"),
            ResponseSchema(name="incident_type", description="Type of crime (Theft, Assault, etc.)"),
            ResponseSchema(name="accused_name_or_unknown", description="Name of accused or 'Unknown Persons'"),
            ResponseSchema(name="complainant_name", description="Name of person filing complaint"),
            ResponseSchema(name="parent_name", description="Father/Spouse name"),
            ResponseSchema(name="age", description="Age of complainant"),
            ResponseSchema(name="address", description="Address of complainant"),
            ResponseSchema(name="phone", description="Phone of complainant"),
            ResponseSchema(name="incident_datetime", description="Date and Time of event"),
            ResponseSchema(name="location", description="Specific spot of incident"),
            ResponseSchema(name="incident_narrative", description="Detailed story of what happened"),
            ResponseSchema(name="accused_details", description="Description of accused"),
            ResponseSchema(name="witness_details", description="Names of witnesses or 'None'"),
            ResponseSchema(name="loss_details", description="List of items lost/damaged")
        ]
    
    output_parser = StructuredOutputParser.from_response_schemas(schemas)
    format_instructions = output_parser.get_format_instructions()
    
    # 3. Prompt LLM
    if "GOOGLE_API_KEY" not in os.environ:
        return "Error: GOOGLE_API_KEY not set."
        
    llm = ChatGoogleGenerativeAI(model="gemini-2.0-flash", temperature=0)
    
    prompt = PromptTemplate(
        template="You are a legal aid assistant. Extract the following details from the user's story to fill a {template_type}.\n"
                 "If a detail is missing, infer a placeholder like '[Insert Name]' or leave blank if optional.\n"
                 "User Context: {user_context}\n"
                 "Current Date: {today}\n\n"
                 "{format_instructions}\n",
        input_variables=["user_context", "template_type", "today"],
        partial_variables={"format_instructions": format_instructions}
    )
    
    _input = prompt.format_prompt(
        user_context=user_context,
        template_type=template_type,
        today=date.today().strftime("%Y-%m-%d")
    )
    
    response = llm.invoke(_input.to_messages())
    parsed_data = output_parser.parse(response.content)
    
    # Add generic date/place if missing from schema but needed
    parsed_data['date'] = date.today().strftime("%d-%m-%Y")
    if 'place' not in parsed_data:
        parsed_data['place'] = parsed_data.get('city_district', '[City]')

    # 4. Fill Template
    template = jinja2.Template(raw_template)
    filled_doc = template.render(**parsed_data)
    
    return filled_doc

if __name__ == "__main__":
    # Test
    sample_context = "My name is Rahul. I want to file an FIR. Yesterday night 10pm near MG Road metro station, someone snatched my iPhone 13. He was wearing a black hoodie. I live in Indiranagar, Bangalore. Phone 9999999999."
    print(draft_document(sample_context, "FIR_Complaint"))
