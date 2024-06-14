DROP TABLE people IF EXISTS;

CREATE TABLE people  (
    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(40),
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    middle VARCHAR(20),
    email VARCHAR(20),
    phone VARCHAR(20),
    fax VARCHAR(20),
    title VARCHAR(20)
                               
);
