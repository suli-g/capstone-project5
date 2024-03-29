/*
** This file uses information obtained at:
** - https://www.mysqltutorial.org/mysql-triggers/mysql-after-insert-trigger
** - https://phoenixnap.com/kb/mysql-trigger
*/

DELIMITER $$
/*
** This trigger checks if the project_name field is blank of the project with project_id matching new.project_id
** and if the project_name field is blank, sets the project_name of the matching project to the result of calling
** set_default_project_name.
**
** see: procedures.sql -> set_default_project_name
*/
CREATE TRIGGER check_project_name BEFORE INSERT ON participant FOR EACH ROW
BEGIN
    DECLARE this_project_name VARCHAR(80);
    DECLARE this_project_type VARCHAR(30);

    SELECT project_name, project_type INTO this_project_name, this_project_type FROM project WHERE project_id = NEW.project_id;
    IF (this_project_name = '') THEN
        IF NEW.relationship = @customer_relationship THEN
            CALL set_default_project_name(NEW.project_id, this_project_type, NEW.person_id);
        END IF;
    END IF;
END$$
/*
** This trigger checks if the project_name field is blank of the project with project_id matching new.project_id
** and if the project_name field is blank, sets the project_name of the matching project to the result of calling
** set_default_project_name.
**
** see: procedures.sql -> set_default_project_name
*/
CREATE TRIGGER create_account AFTER INSERT ON project FOR EACH ROW
BEGIN
    INSERT INTO Account(project, amount_due, amount_paid) VALUE (NEW.project_id, 0, 0);
END$$
/*
** This trigger checks if the project_name field is blank of the project with project_id matching new.project_id
** and if the project_name field is blank, sets the project_name of the matching project to the result of calling
** set_default_project_name.
**
** see: procedures.sql -> set_default_project_name
*/
CREATE TRIGGER check_relationship_on_insert BEFORE INSERT ON participant FOR EACH ROW
BEGIN
    CALL limit_relationships(NEW.relationship, NEW.project_id);
END$$
/*
** This trigger is similar to calls .
**
** see: procedures.sql -> set_default_project_name
*/
CREATE TRIGGER check_relationship_on_update BEFORE UPDATE ON participant FOR EACH ROW
BEGIN
    IF NEW.relationship != OLD.relationship THEN
        CALL limit_relationships(NEW.relationship, NEW.project_id);
    END IF;
END$$
DELIMITER ;