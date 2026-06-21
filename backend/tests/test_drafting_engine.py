import pytest
import os
import sys

# To be able to import backend code
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

import unittest.mock as mock

# Mock langchain imports before importing drafting_engine
sys.modules['langchain_google_genai'] = mock.MagicMock()
sys.modules['langchain'] = mock.MagicMock()
sys.modules['langchain.prompts'] = mock.MagicMock()
sys.modules['langchain.output_parsers'] = mock.MagicMock()

from scripts.drafting_engine import load_template

def test_load_template_file_not_found():
    """Test that load_template raises a FileNotFoundError when the template does not exist."""
    with pytest.raises(FileNotFoundError) as exc_info:
        load_template("non_existent_template_name")

    assert "Template non_existent_template_name not found." in str(exc_info.value)
