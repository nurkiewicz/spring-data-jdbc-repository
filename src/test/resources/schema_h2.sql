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

CREATE TABLE IF NOT EXISTS BOARDING_PASS (
  flight_no varchar(8) NOT NULL,
  seq_no INT NOT NULL,
  passenger VARCHAR(1000),
  seat CHAR(3),
  PRIMARY KEY (flight_no, seq_no)
);

