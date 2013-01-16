CREATE TABLE IF NOT EXISTS USERS (
	user_name varchar(256) PRIMARY KEY,
	date_of_birth DATE NOT NULL,
	reputation INT NOT NULL,
	enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS COMMENTS (
	id INT AUTO_INCREMENT PRIMARY KEY,
	user_name varchar(256),
	contents varchar(1000),
	created_time TIMESTAMP NOT NULL,
	favourite_count INT NOT NULL,
  FOREIGN KEY (user_name) REFERENCES USERS(user_name)
);