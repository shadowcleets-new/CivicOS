import scrapy
import re
import logging
from urllib.parse import urljoin
# import pytesseract # Uncomment if Tesseract is installed and needed
# from PIL import Image
# from io import BytesIO

class OfficialSpider(scrapy.Spider):
    name = "officials"
    
    # Target seeds - in production this would be a large list
    start_urls = [
        'https://bangaloreurban.nic.in/en/contact-us/',
        'https://bbmp.gov.in/telephone-numbers.html', # Example
    ]

    custom_settings = {
        'USER_AGENT': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
        'DOWNLOAD_DELAY': 2, # Rate limit 1 req per 2 seconds (safe side)
        'ROBOTSTXT_OBEY': False # Often gov sites block bots, but we are "public interest"
    }

    def parse(self, response):
        logging.info(f"Crawling: {response.url}")
        
        # 1. Regex Extraction
        text = " ".join(response.xpath('//body//text()').getall())
        
        # Mobile Numbers: +91 or 6-9 followed by 9 digits
        mobile_pattern = re.compile(r'(?:\+91[\-\s]?)?[6789]\d{9}')
        mobiles = set(mobile_pattern.findall(text))
        
        # Landlines: STD code (0\d{2,4}) followed by 6-8 digits
        landline_pattern = re.compile(r'0\d{2,4}[\-\s]?\d{6,8}')
        landlines = set(landline_pattern.findall(text))
        
        # Designations
        designation_pattern = re.compile(r'(?i)(Commissioner|Collector|Magistrate|Engineer|SHO|Inspector|Tahsildar|Health Officer)')
        
        # Context extraction: Try to find "Name" near "Phone"
        # This is hard to do purely with regex on full text. 
        # Better approach: Iterate over table rows if they exist.
        
        rows = response.xpath('//tr')
        for row in rows:
            row_text = " ".join(row.xpath('.//text()').getall())
            found_desig = designation_pattern.search(row_text)
            found_mobile = mobile_pattern.search(row_text)
            
            if found_desig and found_mobile:
                yield {
                    'source_url': response.url,
                    'designation': found_desig.group(0),
                    'phone': found_mobile.group(0),
                    'raw_text': row_text.strip()[:200]
                }
        
        # 2. Link Following (looking for other directories)
        link_pattern = re.compile(r'(contact|directory|phone|list|who)', re.IGNORECASE)
        for href in response.css('a::attr(href)').getall():
            if link_pattern.search(href):
                yield response.follow(href, self.parse)

    def solve_captcha(self, image_url):
        """
        Placeholder for OCR logic.
        """
        # response = requests.get(image_url)
        # img = Image.open(BytesIO(response.content))
        # text = pytesseract.image_to_string(img)
        # return text
        pass
