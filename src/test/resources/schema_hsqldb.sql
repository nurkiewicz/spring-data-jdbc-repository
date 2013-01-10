CREATE TABLE USER (
	user_name varchar(256) PRIMARY KEY,
	date_of_birth TIMESTAMP NOT NULL,
	reputation INT NOT NULL,
	enabled BOOLEAN NOT NULL
);

CREATE TABLE COMMENT (
	id INT IDENTITY PRIMARY KEY,
	user_name varchar(256),
	contents varchar(1000),
	created_time TIMESTAMP NOT NULL,
	favourite_count INT NOT NULL
);