DELIMITER $$
CREATE PROCEDURE limit_relationships(
    new_relationship varchar(30),
    new_id int unsigned)
BEGIN
-- prevent multiple relationships with the same type.
    DECLARE quota int UNSIGNED;
    DECLARE registered int UNSIGNED;
    DECLARE message_text VARCHAR(63);
    SELECT max_allowed INTO quota FROM relationship 
        WHERE relationship_type = new_relationship;
    SELECT COUNT(relationship) INTO registered FROM participant 
        WHERE relationship = new_relationship 
            AND project_id = new_id;
    IF (quota = registered) THEN
        SET message_text := REPLACE(@max_relationship_error, '%s', new_relationship);
        SET message_text := REPLACE(@message_text, '%d', quota);
                SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = message_text;
    END IF;
END$$
CREATE PROCEDURE assign_project(
    project_id int unsigned,
    due_date Date,
    customer_id int unsigned,
    manager_id int unsigned,
    contractor_id int unsigned,
    architect_id int unsigned,
    structural_engineer_id int unsigned
)
BEGIN
    INSERT INTO progress(project, date_due) VALUES (project_id, due_date);
    INSERT INTO participant(project_id, person_id, relationship)
    VALUES
    (project_id, customer_id, @customer_relationship),
    (project_id, manager_id, @project_manager_relationship),
    (project_id, contractor_id, @contractor_relationship),
    (project_id, architect_id, @architect_relationship),
    (project_id, structural_engineer_id, @structural_engineer_relationship);
END$$
CREATE PROCEDURE assign_participant(
    project_id int unsigned,
    due_date Date,
    person_id int unsigned,
    relationship varchar(30)
)
BEGIN
    INSERT INTO participant(project_id, person_id, relationship)
    VALUES
    (project_id, person_id, relationship);
END$$
CREATE PROCEDURE set_default_project_name(
    project_id int unsigned,
    project_type varchar(30),
    customer_id int unsigned
)
BEGIN
    DECLARE customer_last_name VARCHAR(50);
    SELECT last_name INTO customer_last_name FROM person WHERE person_id = customer_id;
    UPDATE project SET project.project_name = CONCAT_WS(' ', project_type, customer_last_name)
        WHERE project.project_id = project_id;
END$$
DELIMITER ;