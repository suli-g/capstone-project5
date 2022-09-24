CREATE VIEW roles AS
SELECT relationship_type
from relationship;
CREATE VIEW addresses AS
SELECT DISTINCT erf_number,
    CONCAT_WS(
        ", ",
        street_address,
        suburb,
        city,
        province,
        post_code
    ) AS full_address
FROM address;
CREATE VIEW people AS
SELECT person_id, first_name, last_name, phone_number, email_address, full_address AS physical_address
    FROM person
    LEFT JOIN addresses ON person.physical_address = erf_number;
CREATE VIEW participants AS
SELECT DISTINCT project_id,
    person.person_id,
    relationship_type,
    first_name,
    last_name,
    phone_number,
    email_address,
    full_address AS physical_address
FROM relationship
    LEFT JOIN participant ON relationship_type = participant.relationship
    LEFT JOIN person ON person.person_id = participant.person_id
    LEFT JOIN addresses ON person.physical_address = erf_number
    ;
CREATE view contacts AS
SELECT DISTINCT erf_number,
    phone_number
    FROM address
    LEFT JOIN person ON physical_address = erf_number;
CREATE VIEW projects AS
SELECT DISTINCT project.project_id,
    project.project_name,
    project_type,
    erf_number,
    addresses.full_address AS project_address,
    account.amount_due,
    account.amount_paid,
    progress.date_due,
    progress.date_finalized AS date_finalized
FROM project
    LEFT JOIN addresses ON project.project_address = addresses.erf_number
    LEFT JOIN account ON project.project_id = account.project
    LEFT JOIN progress ON progress.project = project.project_id
    LEFT JOIN participants ON project.project_id = participants.project_id;

CREATE VIEW incomplete_projects AS
SELECT DISTINCT *
from projects
WHERE date_finalized IS NULL;
CREATE VIEW outstanding_projects AS
SELECT DISTINCT *
from incomplete_projects
WHERE date_due < CURRENT_DATE()
    AND date_finalized IS NULL;
CREATE VIEW finalized_projects AS
SELECT DISTINCT *
FROM projects
WHERE date_finalized IS NOT NULL;
CREATE VIEW types_view AS
    SELECT building_type, relationship_type FROM building b
            RIGHT JOIN relationship r 
                ON b.t_id = r.t_id
                    UNION
    SELECT building_type, relationship_type FROM building b
            LEFT JOIN relationship r 
                ON r.t_id = b.t_id
            ORDER BY -relationship_type DESC;
    
