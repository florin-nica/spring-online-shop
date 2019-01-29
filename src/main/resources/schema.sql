CREATE TABLE IF NOT EXISTS product
(
    id int(11) NOT NULL AUTO_INCREMENT,
    name varchar(100) NOT NULL,
    description varchar(100) DEFAULT NULL,
    price decimal(19,2) NOT NULL,
    weight double precision NOT NULL,
    category int(11) NOT NULL,
    supplier int(11) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS product_category
(
    id int(11) NOT NULL AUTO_INCREMENT,
    name varchar(100) NOT NULL,
    description varchar(100) DEFAULT NULL,
    PRIMARY KEY (id)
);