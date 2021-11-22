CREATE DATABASE evidence;
use evidence;

CREATE TABLE drivers (
    id INT IDENTITY PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    surname VARCHAR(30) NOT NULL,
    driving_license VARCHAR(30) NOT NULL
);

CREATE TABLE trailers (
    id INT IDENTITY PRIMARY KEY,
    trailer_number VARCHAR(10) NOT NULL,
    trailer_type VARCHAR(20) NOT NULL
);

CREATE TABLE trucks (
    id INT IDENTITY PRIMARY KEY,
    licence_number VARCHAR(10) NOT NULL,
    brand VARCHAR(20) NOT NULL,
    model VARCHAR(20) NOT NULL,
    max_load FLOAT NOT NULL,
    current_trailer_id INT NOT NULL,
    current_driver_id INT NOT NULL,
    FOREIGN KEY (current_trailer_id) REFERENCES trailers(id),
    FOREIGN KEY (current_driver_id) REFERENCES drivers(id)
);
CREATE TABLE users (
    id INT IDENTITY PRIMARY KEY,
	role VARCHAR(30) NOT NULL,
    name VARCHAR(30) NOT NULL,
    surname VARCHAR(30) NOT NULL,
    username VARCHAR(7) NOT NULL,
    password_hash NVARCHAR(100) NOT NULL,
	email VARCHAR(30) NOT NULL
);

CREATE TABLE schedule (
	id INT IDENTITY PRIMARY KEY,
	scheduled_date DATETIME NOT NULL,
	arrival_date DATETIME,
	departure_date DATETIME,
	trailer_id INT NOT NULL,
	driver_id INT NOT NULL,
	truck_id INT NOT NULL,
	type VARCHAR(30) NOT NULL,
	FOREIGN KEY (trailer_id) REFERENCES trailers(id),
	FOREIGN KEY (driver_id) REFERENCES drivers(id),
	FOREIGN KEY (truck_id) REFERENCES drivers(id)
);

