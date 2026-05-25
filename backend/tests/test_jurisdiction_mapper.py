import pytest
import sys
from unittest.mock import MagicMock

# Mock out pyrosm before importing scripts.jurisdiction_mapper
sys.modules['pyrosm'] = MagicMock()
sys.modules['geopandas'] = MagicMock()

from scripts.jurisdiction_mapper import assign_jurisdiction

def test_assign_jurisdiction_nhai_highway_trunk():
    row = {"highway": "trunk"}
    assert assign_jurisdiction(row) == "NHAI"

def test_assign_jurisdiction_nhai_ref_startswith_nh():
    row = {"ref": "NH44"}
    assert assign_jurisdiction(row) == "NHAI"

def test_assign_jurisdiction_nhai_ref_list():
    row = {"ref": ["NH44", "AH43"]}
    assert assign_jurisdiction(row) == "NHAI"

def test_assign_jurisdiction_nhai_trunk_and_ref():
    row = {"highway": "trunk", "ref": "NH44"}
    assert assign_jurisdiction(row) == "NHAI"

def test_assign_jurisdiction_state_pwd():
    row = {"highway": "primary"}
    assert assign_jurisdiction(row) == "State PWD"

def test_assign_jurisdiction_municipal_residential():
    row = {"highway": "residential"}
    assert assign_jurisdiction(row) == "Municipal Corporation"

def test_assign_jurisdiction_municipal_tertiary():
    row = {"highway": "tertiary"}
    assert assign_jurisdiction(row) == "Municipal Corporation"

def test_assign_jurisdiction_unknown_highway():
    row = {"highway": "unclassified"}
    assert assign_jurisdiction(row) == "Unknown/Other"

def test_assign_jurisdiction_empty_dict():
    row = {}
    assert assign_jurisdiction(row) == "Unknown/Other"

def test_assign_jurisdiction_none_ref():
    row = {"highway": "unclassified", "ref": None}
    assert assign_jurisdiction(row) == "Unknown/Other"

def test_assign_jurisdiction_ref_not_string_but_starts_with_nh():
    # Technically impossible if it's an int, but let's test integer ref behavior
    row = {"ref": 44}
    assert assign_jurisdiction(row) == "Unknown/Other"

def test_assign_jurisdiction_ref_list_not_nh():
    row = {"ref": ["SH1", "1"]}
    assert assign_jurisdiction(row) == "Unknown/Other"
