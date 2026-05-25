import os
import requests
import logging
from langchain_community.document_loaders import PyPDFLoader
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain_google_genai import GoogleGenerativeAIEmbeddings
from langchain_community.vectorstores import Chroma

# Configure Logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

# Constants
DATA_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "data")
DB_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "chroma_db")
os.makedirs(DATA_DIR, exist_ok=True)
os.makedirs(DB_DIR, exist_ok=True)

# Document URLs
DOCS = {
    "MV_Act_Amendment_2019.pdf": "https://upload.indiacode.nic.in/showfile?actid=AC_CEN_30_44_00004_198859_1517807323910&type=rule&filename=The_Motor_Vehicles_Act_1988_0.pdf", # Using the main act hosted on IndiaCode
    "Good_Samaritan_Guidelines.pdf": "https://morth.nic.in/sites/default/files/GoodSamaritanGuidelines.pdf" 
}

def download_docs():
    """Downloads the required PDFs."""
    for name, url in DOCS.items():
        path = os.path.join(DATA_DIR, name)
        if os.path.exists(path):
            logger.info(f"{name} already exists.")
            continue
        
        logger.info(f"Downloading {name} from {url}...")
        try:
            headers = {'User-Agent': 'Mozilla/5.0'} # Some gov sites block python-requests
            response = requests.get(url, headers=headers, stream=True, verify=False, timeout=30) # verify=False because gov sites often have bad certs
            response.raise_for_status()
            with open(path, 'wb') as f:
                f.write(response.content)
            logger.info(f"Downloaded {name}.")
        except Exception as e:
            logger.error(f"Failed to download {name}: {e}")

def ingest_to_chroma():
    """Ingests PDFs into ChromaDB."""
    documents = []
    
    # Load Documents
    for name in DOCS.keys():
        path = os.path.join(DATA_DIR, name)
        if not os.path.exists(path):
            logger.warning(f"File {path} not found, skipping.")
            continue
            
        logger.info(f"Loading {name}...")
        try:
            loader = PyPDFLoader(path)
            docs = loader.load()
            # Add metadata source
            for d in docs:
                d.metadata["source_doc"] = name
            documents.extend(docs)
        except Exception as e:
            logger.error(f"Error loading {name}: {e}")

    if not documents:
        logger.error("No documents to ingest.")
        return

    # Split Text
    text_splitter = RecursiveCharacterTextSplitter(chunk_size=1000, chunk_overlap=200)
    chunks = text_splitter.split_documents(documents)
    logger.info(f"Split {len(documents)} documents into {len(chunks)} chunks.")

    # Create Embeddings & Store
    logger.info("Initializing Vector Store...")
    if "GOOGLE_API_KEY" not in os.environ:
        logger.warning("GOOGLE_API_KEY not found in environment. Please set it.")
        return

    embeddings = GoogleGenerativeAIEmbeddings(model="models/embedding-001")
    
    vectorstore = Chroma.from_documents(
        documents=chunks,
        embedding=embeddings,
        persist_directory=DB_DIR
    )
    vectorstore.persist()
    logger.info(f"Successfully ingested data into ChromaDB at {DB_DIR}")

if __name__ == "__main__":
    download_docs()
    ingest_to_chroma()
