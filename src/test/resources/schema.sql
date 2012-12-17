CREATE TABLE IF NOT EXISTS USER (
	user_name varchar(256) PRIMARY KEY,
	date_of_birth TIMESTAMP NOT NULL,
	reputation INT NOT NULL,
	enabled BOOLEAN NOT NULL
);