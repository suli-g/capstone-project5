INSERT INTO
    Address (
        erf_number,
        street_address,
        suburb,
        city,
        province,
        post_code
    )
    VALUES
    (
        30001,
        "27 Panado street",
        "Medicine lane",
        "Cape Town",
        "Western Cape",
        3526
    ),
    (
        3021,
        "17 Steak street",
        "Burgerzone",
        "Cape Town",
        "Western Cape",
        7786
    ),
    (
        8091,
        "67 Kilo street",
        "Gram",
        "Cape Town",
        "Western Cape",
        7510
    ),
    (
        80917,
        "7 Mega street",
        "Bitsburg",
        "Cape Town",
        "Western Cape",
        4253
    ),
    (
        62017,
        "57 Kagel street",
        "Braaidorp",
        "Cape Town",
        "Western Cape",
        9256
    );

INSERT INTO
    Person(
        first_name,
        last_name,
        phone_number,
        email_address,
        physical_address
    )
    VALUES
    (
        "Leonardo",
        "Davinci",
        "0816673952",
        "leo\@davinci.com",
        30001
    ),
    (
        "Leonardo",
        "DaCaprio",
        "0716373214",
        "leo\@dacaprio.com",
        3021
    ),
    (
        "Leon",
        "Leonardo",
        "0816273252",
        "leo\@nardo.com",
        8091
    ),
    (
        "Sylvester",
        "Stillown",
        "0712231451",
        "syl\@still.com",
        80917
    );

INSERT INTO project(project_type, project_address)
    SELECT building_type, a.erf_number FROM building b
        JOIN address a
        ORDER BY RAND()
    LIMIT 5;

CALL assign_project(1,"2024-06-01",1, 3,1,2,3);
CALL assign_project(2,"2024-06-01",2, 2, 1,NULL,NULL);
CALL assign_project(3,"2024-06-01",3, 3, 1,2,NULL);
CALL assign_project(4,"2023-11-30",1, 2, NULL,2,3);
CALL assign_project(5,"2025-01-01",4, 3, NULL, NULL, NULL);
