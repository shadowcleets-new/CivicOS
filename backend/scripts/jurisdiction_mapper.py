import os
import requests
from pyrosm import OSM
import geopandas as gpd
import logging

# Configure Logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

# Constants
DATA_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "data")
PBF_URL = "https://download.geofabrik.de/asia/india/southern-zone-latest.osm.pbf"
PBF_FILENAME = "southern-zone-latest.osm.pbf"
PBF_PATH = os.path.join(DATA_DIR, PBF_FILENAME)
OUTPUT_FILENAME = "bangalore_jurisdiction_roads.geojson"
OUTPUT_PATH = os.path.join(DATA_DIR, OUTPUT_FILENAME)

# Ensure data directory exists
os.makedirs(DATA_DIR, exist_ok=True)

def download_osm_data(url, save_path):
    """Downloads OSM PBF data if it doesn't already exist."""
    if os.path.exists(save_path):
        logger.info(f"Data already exists at {save_path}. Skipping download.")
        return

    logger.info(f"Downloading OSM data from {url}...")
    try:
        with requests.get(url, stream=True, timeout=30) as r:
            r.raise_for_status()
            with open(save_path, 'wb') as f:
                for chunk in r.iter_content(chunk_size=8192):
                    f.write(chunk)
        logger.info("Download complete.")
    except Exception as e:
        logger.error(f"Failed to download data: {e}")
        raise

def assign_jurisdiction(row):
    """
    Assigns jurisdiction based on highway tags and refs.
    
    Rules:
    1. NHAI: highway=trunk OR ref starts with "NH"
    2. State PWD: highway=primary
    3. Municipal: highway=residential (and ideally inside Ward boundary, but simple check here)
    """
    tags = row.get("tags", {})
    highway = row.get("highway")
    ref = row.get("ref")
    
    # Handle NaN/None values safely
    if ref is None:
        ref = ""
    if isinstance(ref, list): # Sometimes ref can be a list
        ref = str(ref[0])
    
    # Rule 1: NHAI
    if highway == 'trunk' or str(ref).startswith("NH"):
        return "NHAI"
    
    # Rule 2: State PWD
    if highway == 'primary':
        return "State PWD"
    
    # Rule 3: Municipal (Simplified for now - strictly should check point-in-polygon with Ward)
    if highway == 'residential' or highway == 'tertiary':
        return "Municipal Corporation"
        
    return "Unknown/Other"

def process_osm_data(pbf_path, output_path):
    """Filters and processes the OSM data."""
    logger.info("Initializing Pyrosm...")
    
    # Initialize OSM parser
    # Bounding box for Bangalore (approximate) to speed up processing if loading full southern zone
    # minx, miny, maxx, maxy
    bangalore_bbox = [77.46, 12.83, 77.74, 13.14] 
    
    try:
        osm = OSM(pbf_path, bounding_box=bangalore_bbox)
    except Exception as e:
        logger.error(f"Error initializing OSM parser: {e}")
        return

    logger.info("Reading driving network...")
    # Get driving network
    # We focus on major roads: trunk, primary, secondary, tertiary, residential
    custom_filter = {
        'highway': ['trunk', 'primary', 'secondary', 'tertiary', 'residential', 'motorway']
    }
    
    try:
        roads = osm.get_network(network_type="driving", extra_attributes=['ref', 'name']) # Ensure ref is included
    except Exception as e:
        logger.error(f"Error getting network: {e}")
        return
        
    if roads is None or roads.empty:
        logger.warning("No roads found in the specified bounding box.")
    else:
        logger.info(f"Processing {len(roads)} road segments...")

        # Apply Jurisdiction Logic
        roads['Jurisdiction'] = roads.apply(assign_jurisdiction, axis=1)
        
        # Filter for relevant columns
        cols = ['id', 'name', 'ref', 'highway', 'length', 'geometry', 'Jurisdiction']
        final_cols = [c for c in cols if c in roads.columns]
        output_gdf = roads[final_cols]

        logger.info(f"Saving processed roads to {output_path}...")
        output_gdf.to_file(output_path, driver="GeoJSON")
        
        # Validation stats
        logger.info("Road Jurisdiction Breakdown:")
        print(output_gdf['Jurisdiction'].value_counts())

    # --- Process Boundaries ---
    logger.info("Reading administrative boundaries...")
    try:
        # admin_level 6 = Taluk/Tehsil, 8 = Ward/Town (varies by region in India, but 8-10 is usually local)
        boundaries = osm.get_boundaries()
    except Exception as e:
        logger.error(f"Error getting boundaries: {e}")
        return

    if boundaries is not None and not boundaries.empty:
        # Filter for relevant admin levels if possible, or just save all
        logger.info(f"Found {len(boundaries)} boundaries.")
        boundaries_output_path = output_path.replace("roads.geojson", "boundaries.geojson")
        
        # Keep relevant columns
        b_cols = ['id', 'name', 'admin_level', 'geometry']
        final_b_cols = [c for c in b_cols if c in boundaries.columns]
        boundaries_gdf = boundaries[final_b_cols]
        
        logger.info(f"Saving processed boundaries to {boundaries_output_path}...")
        boundaries_gdf.to_file(boundaries_output_path, driver="GeoJSON")
    else:
        logger.warning("No boundaries found.")

    logger.info("Processing complete.")

if __name__ == "__main__":
    download_osm_data(PBF_URL, PBF_PATH)
    process_osm_data(PBF_PATH, OUTPUT_PATH)
