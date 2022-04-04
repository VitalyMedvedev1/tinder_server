--liquibase formatted sql
--changeset vemedvedev:USERS
CREATE TABLE OTPMM.USERS (
	ID INTEGER NOT NULL,
	USERNAME VARCHAR(32) NOT NULL,
	NAME VARCHAR(32) NOT NULL,
	PASSWORD VARCHAR(64) NOT NULL,
	GENDER VARCHAR(8) NOT NULL,
	HEADER VARCHAR(32),
	LOOKINGFOR VARCHAR(8),
	DESCRIPTION VARCHAR(255),
	FORM_FILE_NAME VARCHAR(32),
	CREATED_AT TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX USERS_ID_INDX ON OTPMM.USERS (ID);
CREATE UNIQUE INDEX USERS_NAME_INDX ON OTPMM.USERS (NAME);