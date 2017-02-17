INSERT INTO PERSON (ID,  FIRST_NAME, LAST_NAME,PATRONYMIC, DESCRIPTION, DEPARTMENT, POSITION, BIRTH_DATE) VALUES (1,'Ivan','Ivanov','Ivanovich','Very good specialist','IT Department','Software Engineer','1961-05-16');
INSERT INTO PERSON (ID,  FIRST_NAME, LAST_NAME,PATRONYMIC, DESCRIPTION, DEPARTMENT, POSITION, BIRTH_DATE) VALUES (2,'Petr','Petrov','Petrovich','Very good specialist','Development Department','Team Leader','1982-05-03');

INSERT INTO history (ID, person_id, rec, WHEN_DATE) VALUES (1,1,'Hired by the system administrator','2014-05-03');
INSERT INTO history (ID, person_id, rec, WHEN_DATE) VALUES (2,1,'Transferred to the position of Software Engineer','2014-06-03');
INSERT INTO history (ID, person_id, rec, WHEN_DATE) VALUES (3,2,'Hired by the Developer','2014-05-03');
INSERT INTO history (ID, person_id, rec, WHEN_DATE) VALUES (4,2,'Transferred to the position of Team Leader','2014-06-03');

