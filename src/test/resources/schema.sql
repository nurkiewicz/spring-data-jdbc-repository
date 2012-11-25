CREATE TABLE IF NOT EXISTS USER (
	id varchar(128) not null,
	userName varchar(256) not null,
	fullName varchar(256) not null,
	password varchar(256) not null,
	role varchar(256) not null,
);