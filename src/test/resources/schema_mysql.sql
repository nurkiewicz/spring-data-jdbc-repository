CREATE TABLE USER (
	user_name varchar(255),
	date_of_birth TIMESTAMP NOT NULL,
	reputation INT NOT NULL,
	enabled BIT(1) NOT NULL,
	PRIMARY KEY (user_name)
);