CREATE TABLE USER (
	user_name varchar(256) PRIMARY KEY,
	date_of_birth TIMESTAMP NOT NULL,
	reputation INT NOT NULL,
	enabled BIT(1) NOT NULL
);