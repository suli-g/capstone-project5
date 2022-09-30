/**
 ** Stores building types (for listing building type options).
 */
CREATE TABLE Building(
    t_id int unsigned AUTO_INCREMENT,
    building_type varchar(32) UNIQUE NOT NULL,
    PRIMARY KEY(t_id)
);
/**
 ** Stores the valid participant relationship types
 */
CREATE TABLE Relationship(
    t_id int unsigned AUTO_INCREMENT,
    relationship_type varchar(30) UNIQUE NOT NULL,
    max_allowed int UNSIGNED DEFAULT 1,
    PRIMARY KEY(t_id)
);
/**
 ** Stores all addresses.
 */
CREATE TABLE Address(
    erf_number int UNSIGNED,
    street_address varchar(80),
    suburb varchar(60),
    city varchar(60),
    province varchar(12),
    post_code char(4),
    PRIMARY KEY(erf_number)
);
/**
 ** Stores details for all people.
 */
CREATE TABLE Person(
    person_id int UNSIGNED AUTO_INCREMENT,
    first_name varchar(50) NOT NULL,
    last_name varchar(50) NOT NULL,
    phone_number char(10) NOT NULL UNIQUE,
    email_address varchar(80) UNIQUE NOT NULL,
    physical_address int UNSIGNED,
    PRIMARY KEY(person_id),
    FOREIGN KEY(physical_address) REFERENCES address(erf_number)
);

/**
 ** Stores project-specific details.
 */
CREATE TABLE Project(
    project_id int UNSIGNED AUTO_INCREMENT,
    project_name varchar(80) DEFAULT '',
    project_type varchar(32) NOT NULL,
    project_address int UNSIGNED NOT NULL,
    PRIMARY KEY(project_id),
    FOREIGN KEY (project_address) REFERENCES address(erf_number),
    FOREIGN KEY (project_type) REFERENCES building(building_type)
);

/**
 ** Stores references to the people involved with a given project.
 */
CREATE TABLE Participant(
    participant_id int UNSIGNED AUTO_INCREMENT,
    person_id int UNSIGNED,
    project_id int UNSIGNED,
    relationship varchar(30),
    PRIMARY KEY(participant_id),
    FOREIGN KEY(project_id) REFERENCES Project(project_id),
    FOREIGN KEY (person_id) REFERENCES Person(person_id),
    FOREIGN KEY (relationship) REFERENCES Relationship(relationship_type)
);
/**
 ** Stores financial account details.
 ** Does not cascade on deletion in case of 
 */
CREATE TABLE Account(
    account_id int UNSIGNED AUTO_INCREMENT,
    project int unsigned NOT NULL,
    amount_due int NOT NULL,
    amount_paid int DEFAULT 0,
    FOREIGN KEY(project) REFERENCES Project(project_id),
    PRIMARY KEY(account_id)
);
/**
 ** Stores project progress details.
 ** Cascades when a project is terminated.
 */
CREATE TABLE Progress(
    tracker_id int UNSIGNED AUTO_INCREMENT,
    project int UNSIGNED NOT NULL,
    date_due Date NOT NULL,
    date_finalized Date,
    PRIMARY KEY(tracker_id),
    FOREIGN KEY(project) REFERENCES Project(project_id) ON DELETE CASCADE
);