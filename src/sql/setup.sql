DROP DATABASE IF EXISTS PoisePMS;   /* Remove this line before deploying to production */
CREATE DATABASE IF NOT EXISTS PoisePMS;
USE PoisePMS; 
-- Error messages
SET @duplicate_erf_error_message := 'erf number not associated with an address';
SET @max_relationship_error := 'Max amount reached for %s. Maximum allowed: %d';
-- Relationships
SET @customer_relationship := 'customer';
SET @project_manager_relationship := 'project manager';
SET @contractor_relationship := 'contractor';
SET @architect_relationship := 'architect';
SET @structural_engineer_relationship := 'structural engineer';

source ./tables.sql
source ./views.sql
source ./procedures.sql
source ./triggers.sql

INSERT INTO building(building_type) 
    VALUES ("house"), ("villa"), ("mansion"),("apartment"), ("rdp"), ("shack");

INSERT INTO relationship(relationship_type)
    VALUES (@customer_relationship),
(@project_manager_relationship),
(@contractor_relationship),
(@architect_relationship),
(@structural_engineer_relationship);

source ./test_data.sql /** Remove line before deploying to production.
