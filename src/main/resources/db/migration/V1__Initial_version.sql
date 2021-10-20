CREATE TABLE t_user (
	id bigint NOT NULL,
	user_name character varying(50),
	email character varying(255),
	first_name character varying(50),
	last_name character varying(50),
	password character varying(255)
	
);

CREATE SEQUENCE t_user_user_id_seq 
	START WITH 1
	INCREMENT BY 1
	NO MINVALUE
	NO MAXVALUE
	CACHE 1;
	
ALTER SEQUENCE t_user_user_id_seq OWNED BY t_user.id;
ALTER TABLE ONLY t_user ALTER COLUMN id SET DEFAULT nextval('t_user_user_id_seq'::regclass);
ALTER TABLE ONLY t_user ADD CONSTRAINT t_user_pkey PRIMARY KEY (id);

INSERT INTO t_user(
	user_name, email, first_name, last_name, password)
	VALUES ('username1', 'name1@gmail.com', 'firstname1', 'lastname1', '$2a$10$qJvkQYc2PjVwEXsRS0VEq.bedlRlPwssYSi.J/U6tAn77plRGSt.a');
INSERT INTO t_user(
	user_name, email, first_name, last_name)
	VALUES ('username2', 'name2@gmail.com', 'firstname2', 'lastname2');
INSERT INTO t_user(
	user_name, email, first_name, last_name)
	VALUES ('username3', 'name3@gmail.com', 'firstname3', 'lastname3');
INSERT INTO t_user(
	user_name, email, first_name, last_name)
	VALUES ('username4', 'name4@gmail.com', 'firstname4', 'lastname4');	
INSERT INTO t_user(
	user_name, email, first_name, last_name)
	VALUES ('username5', 'name5@gmail.com', 'firstname5', 'lastname5');	
INSERT INTO t_user(
	user_name, email, first_name, last_name)
	VALUES ('username6', 'name6@gmail.com', 'firstname6', 'lastname6');	
INSERT INTO t_user(
	user_name, email, first_name, last_name)
	VALUES ('username7', 'name7@gmail.com', 'firstname7', 'lastname7');	

CREATE TABLE t_role (
    role_id integer NOT NULL,
    name character varying(255),
    description character varying(512)
);

CREATE SEQUENCE role_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE role_role_id_seq OWNED BY t_role.role_id;
ALTER TABLE ONLY t_role ALTER COLUMN role_id SET DEFAULT nextval('role_role_id_seq'::regclass);
SELECT pg_catalog.setval('role_role_id_seq', 1, false);
ALTER TABLE ONLY t_role
    ADD CONSTRAINT role_id_pkey PRIMARY KEY (role_id);

INSERT INTO t_role(name, description)
	VALUES('Administrator', 'Administrator');
INSERT INTO t_role(name, description)
	VALUES('User', 'User');
	
	
CREATE TABLE user_role (
    role_id integer,
    user_id bigint
);

ALTER TABLE ONLY user_role
    ADD CONSTRAINT fk_user__reference_user FOREIGN KEY (user_id) REFERENCES t_user(id) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE ONLY user_role
    ADD CONSTRAINT fk_user__reference_role FOREIGN KEY (role_id) REFERENCES t_role(role_id) ON UPDATE RESTRICT ON DELETE RESTRICT;

INSERT INTO user_role(role_id, user_id)
	VALUES(1, 1);
