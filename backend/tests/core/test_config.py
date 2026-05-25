import unittest
import os
from unittest.mock import patch
from pydantic_core import ValidationError
from app.core.config import Settings

class TestConfig(unittest.TestCase):
    @patch.dict(os.environ, {'POSTGRES_PASSWORD': 'test_password', 'GOOGLE_API_KEY': 'test_key'}, clear=True)
    def test_settings_missing_secret_key(self):
        # Temporarily disable env_file reading to avoid loading from a local .env
        class TestSettings(Settings):
            class Config:
                env_file = None

        # Ensure ValidationError is raised when SECRET_KEY is missing
        with self.assertRaises(ValidationError):
            TestSettings()

    @patch.dict(os.environ, {'SECRET_KEY': 'test_secret_key_123', 'POSTGRES_PASSWORD': 'test_password', 'GOOGLE_API_KEY': 'test_key'}, clear=True)
    def test_settings_with_secret_key(self):
        class TestSettings(Settings):
            class Config:
                env_file = None

        # Instantiate Settings, this should not raise an error
        settings = TestSettings()

        # Verify the key was set correctly
        self.assertEqual(settings.SECRET_KEY, 'test_secret_key_123')

if __name__ == '__main__':
    unittest.main()
