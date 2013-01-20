CREATE TABLE USERS (
	user_name varchar(255),
	date_of_birth TIMESTAMP NOT NULL,
	reputation INT NOT NULL,
	enabled BIT(1) NOT NULL,
	PRIMARY KEY (user_name)
);

CREATE TABLE COMMENTS (
	id INT AUTO_INCREMENT,
	user_name varchar(256),
	contents varchar(1000),
	created_time TIMESTAMP NOT NULL,
	favourite_count INT NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE BOARDING_PASS (
	flight_no VARCHAR(8) NOT NULL,
	seq_no INT NOT NULL,
	passenger VARCHAR(1000),
	seat CHAR(3),
	PRIMARY KEY (flight_no, seq_no)
);
