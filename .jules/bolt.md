## 2024-06-03 - Compile regex patterns at class level instead of within parse loops
**Learning:** Found an anti-pattern in the Scrapy spiders (`official_spider.py`) where regular expressions (`re.compile`) were being recompiled within the `parse` method for every single crawled page. In a web scraper that processes thousands of pages, this causes unnecessary CPU overhead and slows down parsing.
**Action:** Always pre-compile static regular expressions as class-level attributes or module-level constants in Scrapy spiders and other frequently executed loops.
