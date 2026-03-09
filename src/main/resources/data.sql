
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE registration;
TRUNCATE TABLE dog;
TRUNCATE TABLE breed;
TRUNCATE TABLE session;
TRUNCATE TABLE age_group;
TRUNCATE TABLE course_specialization;
TRUNCATE TABLE course;
TRUNCATE TABLE coach_specialization;
TRUNCATE TABLE specialization;
TRUNCATE TABLE owner;
TRUNCATE TABLE coach;
TRUNCATE TABLE admin;
TRUNCATE TABLE validation_token;
TRUNCATE TABLE user;

SET FOREIGN_KEY_CHECKS = 1;


INSERT INTO user (
    id_user, last_name, first_name, email, password_hash, phone, registration_date,
    avatar_url, bio, last_login, email_verified, is_active, anonymized_at, last_password_change_at
) VALUES (
             1, 'Admin', 'Master', 'admin@admin.com',
             '$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq',
             '3332223333', '2024-01-01', NULL, 'Super Admin', '2024-05-21 10:30:00',
             true, true, NULL, NULL
         );
INSERT INTO admin (id_user) VALUES (1);


INSERT INTO specialization (id_specialization, name, description) VALUES
                                                                      (1, 'Obedience', 'Base obedience skills'),
                                                                      (2, 'Agility',   'Agility courses and obstacles'),
                                                                      (3, 'Defense',   'Guards and Defense Speciality');

-- =========================================================
-- AGE GROUPS
-- =========================================================
INSERT INTO age_group (id_age_group, name, min_age, max_age) VALUES
                                                                 (1, 'PUPPY',  0,  4),
                                                                 (2, 'JUNIOR', 5, 11),
                                                                 (3, 'ADULT',  12, NULL);

-- =========================================================
-- COURSES
-- =========================================================
INSERT INTO course (id_course, name, description, status, image_url) VALUES
                                                                         (1, 'Éducation canine',
                                                                          'Cours pour apprendre les bases de l’obéissance et renforcer la relation maître-chien',
                                                                          'ACTIVE', 'https://example.com/images/obedience.jpg'),
                                                                         (2, 'Agility',
                                                                          'Parcours d’obstacles dynamique: vitesse, précision et complicité',
                                                                          'ACTIVE', 'https://example.com/images/agility.jpg'),
                                                                         (3, 'Garde et défense',
                                                                          'Entraînement encadré au respect des règles de sécurité et du bien-être animal',
                                                                          'ACTIVE', 'https://example.com/images/defense.jpg');

INSERT INTO course_specialization (id_course, id_specialization) VALUES
                                                                     (1,1), (2,2), (3,3);

-- =========================================================
-- USERS: 10 COACH (ID 2..11) + 30 OWNER (ID 12..41)
-- (tutti con lo stesso hash per semplicità)
-- =========================================================
-- COACH
INSERT INTO user (id_user,last_name,first_name,email,password_hash,phone,registration_date,avatar_url,bio,last_login,email_verified,is_active,anonymized_at,last_password_change_at) VALUES
                                                                                                                                                                                         (2,'Rossi','Marco','marco.rossi@club.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0600000002','2024-02-01',NULL,'Coach senior',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (3,'Bianchi','Luca','luca.bianchi@club.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0600000003','2024-02-03',NULL,'Coach',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (4,'Verdi','Giulia','giulia.verdi@club.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0600000004','2024-02-05',NULL,'Coach',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (5,'Ferrari','Paolo','paolo.ferrari@club.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0600000005','2024-02-07',NULL,'Coach',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (6,'Galli','Sara','sara.galli@club.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0600000006','2024-02-09',NULL,'Coach',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (7,'Legrand','Pierre','pierre.legrand@club.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0600000007','2024-02-11',NULL,'Coach',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (8,'Moreau','Claire','claire.moreau@club.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0600000008','2024-02-13',NULL,'Coach',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (9,'Martin','Hugo','hugo.martin@club.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0600000009','2024-02-15',NULL,'Coach',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (10,'Bernard','Camille','camille.bernard@club.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0600000010','2024-02-17',NULL,'Coach',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (11,'Leroy','Thomas','thomas.leroy@club.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0600000011','2024-02-19',NULL,'Coach',NULL,true,true,NULL,NULL);

INSERT INTO coach (id_user) VALUES (2),(3),(4),(5),(6),(7),(8),(9),(10),(11);

-- Specializzazioni per coach
INSERT INTO coach_specialization (id_user,id_specialization) VALUES
                                                                 (2,1),(2,2),
                                                                 (3,1),
                                                                 (4,2),
                                                                 (5,3),
                                                                 (6,1),(6,3),
                                                                 (7,2),
                                                                 (8,1),
                                                                 (9,2),(9,3),
                                                                 (10,1),
                                                                 (11,3);

-- OWNER (30)
INSERT INTO user (id_user,last_name,first_name,email,password_hash,phone,registration_date,avatar_url,bio,last_login,email_verified,is_active,anonymized_at,last_password_change_at) VALUES
                                                                                                                                                                                         (12,'Dupont','Anne','anne.dupont@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000012','2024-03-01',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (13,'Durand','Nicolas','nicolas.durand@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000013','2024-03-01',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (14,'Petit','Laura','laura.petit@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000014','2024-03-02',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (15,'Robert','Julien','julien.robert@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000015','2024-03-02',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (16,'Richard','Emma','emma.richard@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000016','2024-03-03',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (17,'Morel','Hugo','hugo.morel@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000017','2024-03-03',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (18,'Fournier','Chloé','chloe.fournier@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000018','2024-03-04',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (19,'Girard','Matteo','matteo.girard@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000019','2024-03-04',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (20,'Andre','Sofia','sofia.andre@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000020','2024-03-05',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (21,'Mercier','Alex','alex.mercier@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000021','2024-03-05',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (22,'Blanc','Camila','camila.blanc@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000022','2024-03-06',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (23,'Guillaume','Thomas','thomas.guillaume@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000023','2024-03-06',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (24,'Muller','Eva','eva.muller@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000024','2024-03-07',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (25,'Rossi','Lorenzo','lorenzo.rossi@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000025','2024-03-07',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (26,'Fontana','Giada','giada.fontana@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000026','2024-03-08',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (27,'Romano','Diego','diego.romano@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000027','2024-03-08',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (28,'Conti','Elena','elena.conti@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000028','2024-03-09',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (29,'Greco','Paola','paola.greco@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000029','2024-03-09',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (30,'Marino','Stefano','stefano.marino@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000030','2024-03-10',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (31,'De Luca','Marta','marta.deluca@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000031','2024-03-10',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (32,'Rizzo','Alessio','alessio.rizzo@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000032','2024-03-11',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (33,'Sartori','Ilaria','ilaria.sartori@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000033','2024-03-11',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (34,'Ferri','Davide','davide.ferri@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000034','2024-03-12',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (35,'Testa','Alice','alice.testa@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000035','2024-03-12',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (36,'Vitale','Enrico','enrico.vitale@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000036','2024-03-13',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (37,'Bellini','Chiara','chiara.bellini@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000037','2024-03-13',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (38,'Cattaneo','Giorgio','giorgio.cattaneo@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000038','2024-03-14',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (39,'Fiore','Noemi','noemi.fiore@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000039','2024-03-14',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (40,'Serra','Riccardo','riccardo.serra@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000040','2024-03-15',NULL,'Owner',NULL,true,true,NULL,NULL),
                                                                                                                                                                                         (41,'Barbieri','Elisa','elisa.barbieri@mail.com','$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq','0610000041','2024-03-15',NULL,'Owner',NULL,true,true,NULL,NULL);

INSERT INTO owner (id_user,address,city,postal_code) VALUES
                                                         (12,'12 Rue des Lilas','Metz','57000'),
                                                         (13,'8 Rue de la Gare','Metz','57000'),
                                                         (14,'2 Place d’Armes','Thionville','57100'),
                                                         (15,'5 Avenue Foch','Nancy','54000'),
                                                         (16,'10 Rue Nationale','Metz','57000'),
                                                         (17,'7 Rue Pasteur','Montigny-lès-Metz','57950'),
                                                         (18,'3 Allée des Chênes','Metz','57000'),
                                                         (19,'4 Rue de Verdun','Metz','57000'),
                                                         (20,'1 Rue des Écoles','Woippy','57140'),
                                                         (21,'6 Boulevard Paixhans','Metz','57000'),
                                                         (22,'9 Rue de la Liberté','Metz','57000'),
                                                         (23,'11 Rue Serpenoise','Metz','57000'),
                                                         (24,'14 Rue des Jardins','Metz','57000'),
                                                         (25,'3 Rue du Pont','Ban Saint-Martin','57050'),
                                                         (26,'15 Avenue André Malraux','Metz','57000'),
                                                         (27,'21 Rue du Canal','Metz','57000'),
                                                         (28,'18 Rue du Fort','Metz','57000'),
                                                         (29,'5 Rue du Sablon','Metz','57000'),
                                                         (30,'19 Rue des Tilleuls','Metz','57000'),
                                                         (31,'2 Rue Jeanne d’Arc','Metz','57000'),
                                                         (32,'17 Rue Saint-Louis','Metz','57000'),
                                                         (33,'22 Rue Saint-Pierre','Metz','57000'),
                                                         (34,'24 Rue du Château','Metz','57000'),
                                                         (35,'27 Rue du Pontiffroy','Metz','57000'),
                                                         (36,'29 Rue Vauban','Metz','57000'),
                                                         (37,'31 Rue Belle-Isle','Metz','57000'),
                                                         (38,'33 Rue aux Arènes','Metz','57000'),
                                                         (39,'35 Rue du Wad Billy','Metz','57000'),
                                                         (40,'37 Rue de la Chèvre','Metz','57000'),
                                                         (41,'39 Rue des Capucins','Metz','57000');

-- =========================================================
-- BREEDS
-- =========================================================
INSERT INTO breed (id_breed, name) VALUES
                                       (1,'Labrador Retriever'),(2,'Berger Allemand'),(3,'Border Collie'),(4,'Golden Retriever'),
                                       (5,'Malinois'),(6,'Beagle'),(7,'Cane Corso'),(8,'Shiba Inu'),
                                       (9,'Jack Russell'),(10,'Barboncino'),(11,'Bulldog'),(12,'Husky');

-- =========================================================
-- DOGS (~50) — 2 per i primi 20 owner, 1 per gli ultimi 10
-- =========================================================
-- owner 12..31: 2 cani ciascuno
INSERT INTO dog (name,birth_date,sex,photo_url,chip_number,weight,id_owner,id_breed) VALUES
                                                                                         ('Buddy','2022-05-10','M',NULL,'CHIP-000001',28.5,12,1), ('Luna','2023-02-14','F',NULL,'CHIP-000002',22.1,12,3),
                                                                                         ('Milo','2021-11-03','M',NULL,'CHIP-000003',24.0,13,2), ('Nala','2020-07-20','F',NULL,'CHIP-000004',18.2,13,6),
                                                                                         ('Rocky','2021-03-17','M',NULL,'CHIP-000005',30.0,14,5), ('Zoe','2022-09-09','F',NULL,'CHIP-000006',14.8,14,9),
                                                                                         ('Thor','2019-06-01','M',NULL,'CHIP-000007',32.4,15,7), ('Kira','2023-01-12','F',NULL,'CHIP-000008',20.3,15,1),
                                                                                         ('Leo','2022-03-22','M',NULL,'CHIP-000009',21.6,16,10), ('Maya','2021-12-30','F',NULL,'CHIP-000010',17.9,16,8),
                                                                                         ('Oscar','2020-04-05','M',NULL,'CHIP-000011',26.7,17,4), ('Molly','2019-10-28','F',NULL,'CHIP-000012',15.5,17,6),
                                                                                         ('Toby','2023-03-16','M',NULL,'CHIP-000013',23.3,18,3), ('Bella','2021-01-25','F',NULL,'CHIP-000014',19.0,18,1),
                                                                                         ('Zeus','2020-08-08','M',NULL,'CHIP-000015',34.2,19,7), ('Lily','2022-04-14','F',NULL,'CHIP-000016',16.2,19,10),
                                                                                         ('Charlie','2021-07-07','M',NULL,'CHIP-000017',25.9,20,1), ('Ruby','2022-10-11','F',NULL,'CHIP-000018',13.7,20,9),
                                                                                         ('Bruno','2020-02-02','M',NULL,'CHIP-000019',31.1,21,5), ('Cleo','2021-09-09','F',NULL,'CHIP-000020',12.9,21,11),
                                                                                         ('Max','2019-05-19','M',NULL,'CHIP-000021',27.3,22,2), ('Daisy','2023-02-02','F',NULL,'CHIP-000022',20.0,22,4),
                                                                                         ('Axel','2022-07-21','M',NULL,'CHIP-000023',29.2,23,7), ('Gina','2021-06-30','F',NULL,'CHIP-000024',18.9,23,1),
                                                                                         ('Duke','2020-11-11','M',NULL,'CHIP-000025',33.5,24,5), ('Ivy','2022-01-01','F',NULL,'CHIP-000026',15.4,24,6),
                                                                                         ('Rex','2021-04-04','M',NULL,'CHIP-000027',28.0,25,2), ('Penny','2023-05-05','F',NULL,'CHIP-000028',12.0,25,10),
                                                                                         ('Argo','2020-12-12','M',NULL,'CHIP-000029',35.0,26,7), ('Mimi','2022-08-18','F',NULL,'CHIP-000030',14.2,26,9),
                                                                                         ('Hugo','2021-02-14','M',NULL,'CHIP-000031',23.1,27,3), ('Nina','2020-06-06','F',NULL,'CHIP-000032',17.0,27,8),
                                                                                         ('Boris','2022-09-01','M',NULL,'CHIP-000033',26.0,28,4), ('Fifi','2021-03-03','F',NULL,'CHIP-000034',13.9,28,11),
                                                                                         ('Yago','2019-09-09','M',NULL,'CHIP-000035',31.4,29,5), ('Uma','2023-01-20','F',NULL,'CHIP-000036',12.5,29,10),
                                                                                         ('Otto','2022-02-02','M',NULL,'CHIP-000037',22.2,30,1), ('Aria','2021-08-08','F',NULL,'CHIP-000038',18.1,30,4),
                                                                                         ('Milo2','2020-07-07','M',NULL,'CHIP-000039',28.8,31,2), ('Lola','2022-12-12','F',NULL,'CHIP-000040',16.6,31,6);

-- owner 32..41: 1 cane ciascuno (10 cani)
INSERT INTO dog (name,birth_date,sex,photo_url,chip_number,weight,id_owner,id_breed) VALUES
                                                                                         ('Dante','2021-01-10','M',NULL,'CHIP-000041',27.2,32,3),
                                                                                         ('Sissi','2019-12-12','F',NULL,'CHIP-000042',24.4,33,1),
                                                                                         ('Paco','2022-05-05','M',NULL,'CHIP-000043',19.8,34,6),
                                                                                         ('Kiki','2020-10-10','F',NULL,'CHIP-000044',15.7,35,10),
                                                                                         ('Rocco','2021-11-11','M',NULL,'CHIP-000045',33.0,36,7),
                                                                                         ('Bibi','2022-03-03','F',NULL,'CHIP-000046',14.9,37,9),
                                                                                         ('Tyson','2019-04-04','M',NULL,'CHIP-000047',34.8,38,5),
                                                                                         ('Iris','2023-06-06','F',NULL,'CHIP-000048',13.1,39,11),
                                                                                         ('Nero','2020-02-20','M',NULL,'CHIP-000049',26.6,40,2),
                                                                                         ('Gilda','2022-09-09','F',NULL,'CHIP-000050',20.5,41,4);

-- =========================================================
-- SESSIONS (12) — diverse combinazioni corso/gruppo/coach
-- =========================================================
INSERT INTO session (date,level,start_time,end_time,max_capacity,description,location,image_url,id_course,id_age_group,id_user) VALUES
                                                                                                                                    ('2025-09-05','BEGINNER','18:00:00','19:00:00',10,'Base obedience','Club House',NULL,1,1,2),
                                                                                                                                    ('2025-09-06','BEGINNER','18:00:00','19:00:00',10,'Base obedience','Club House',NULL,1,2,3),
                                                                                                                                    ('2025-09-07','INTERMEDIATE','18:00:00','19:00:00',10,'Obedience intermédiaire','Terrain A',NULL,1,3,6),
                                                                                                                                    ('2025-09-08','BEGINNER','17:00:00','18:00:00',10,'Agility découverte','Terrain B',NULL,2,2,4),
                                                                                                                                    ('2025-09-09','INTERMEDIATE','17:00:00','18:00:00',10,'Agility pratique','Terrain B',NULL,2,3,7),
                                                                                                                                    ('2025-09-10','ADVANCED','19:00:00','20:00:00',10,'Agility avancé','Terrain B',NULL,2,3,9),
                                                                                                                                    ('2025-09-11','BEGINNER','18:30:00','19:30:00',10,'Sécurité et garde – bases','Terrain C',NULL,3,3,5),
                                                                                                                                    ('2025-09-12','INTERMEDIATE','18:30:00','19:30:00',10,'Garde – pratique','Terrain C',NULL,3,3,11),
                                                                                                                                    ('2025-09-13','BEGINNER','16:30:00','17:30:00',10,'Obedience jeunes','Terrain A',NULL,1,2,8),
                                                                                                                                    ('2025-09-14','BEGINNER','16:30:00','17:30:00',10,'Agility jeunes','Terrain B',NULL,2,2,10),
                                                                                                                                    ('2025-09-15','INTERMEDIATE','18:00:00','19:00:00',10,'Obedience pratique','Club House',NULL,1,3,2),
                                                                                                                                    ('2025-09-16','BEGINNER','18:00:00','19:00:00',10,'Accueil nouveaux','Club House',NULL,1,1,3);

-- =========================================================
-- REGISTRATIONS (nessun duplicato per sessione/cane)
-- (Assumo che gli id_dog partano da 1 in ordine d’inserimento)
-- =========================================================
-- Sessione 1: cani 1..8
INSERT INTO registration (registration_date,status,cancellation_date,cancellation_reason,id_session,id_dog) VALUES
                                                                                                                ('2025-08-20','CONFIRMED',NULL,NULL,1,1),
                                                                                                                ('2025-08-20','CONFIRMED',NULL,NULL,1,2),
                                                                                                                ('2025-08-20','CONFIRMED',NULL,NULL,1,3),
                                                                                                                ('2025-08-20','CONFIRMED',NULL,NULL,1,4),
                                                                                                                ('2025-08-20','CONFIRMED',NULL,NULL,1,5),
                                                                                                                ('2025-08-20','CONFIRMED',NULL,NULL,1,6),
                                                                                                                ('2025-08-21','CONFIRMED',NULL,NULL,1,7),
                                                                                                                ('2025-08-21','CONFIRMED',NULL,NULL,1,8);

-- Sessione 2: cani 9..16
INSERT INTO registration (registration_date,status,cancellation_date,cancellation_reason,id_session,id_dog) VALUES
                                                                                                                ('2025-08-21','CONFIRMED',NULL,NULL,2,9),
                                                                                                                ('2025-08-21','CONFIRMED',NULL,NULL,2,10),
                                                                                                                ('2025-08-21','CONFIRMED',NULL,NULL,2,11),
                                                                                                                ('2025-08-21','CONFIRMED',NULL,NULL,2,12),
                                                                                                                ('2025-08-22','CONFIRMED',NULL,NULL,2,13),
                                                                                                                ('2025-08-22','CONFIRMED',NULL,NULL,2,14),
                                                                                                                ('2025-08-22','CONFIRMED',NULL,NULL,2,15),
                                                                                                                ('2025-08-22','CONFIRMED',NULL,NULL,2,16);

-- Sessione 3: cani 17..24
INSERT INTO registration (registration_date,status,cancellation_date,cancellation_reason,id_session,id_dog) VALUES
                                                                                                                ('2025-08-22','CONFIRMED',NULL,NULL,3,17),
                                                                                                                ('2025-08-22','CONFIRMED',NULL,NULL,3,18),
                                                                                                                ('2025-08-22','CONFIRMED',NULL,NULL,3,19),
                                                                                                                ('2025-08-22','CONFIRMED',NULL,NULL,3,20),
                                                                                                                ('2025-08-23','CONFIRMED',NULL,NULL,3,21),
                                                                                                                ('2025-08-23','CONFIRMED',NULL,NULL,3,22),
                                                                                                                ('2025-08-23','CONFIRMED',NULL,NULL,3,23),
                                                                                                                ('2025-08-23','CONFIRMED',NULL,NULL,3,24);

-- Sessione 4: cani 25..32
INSERT INTO registration (registration_date,status,cancellation_date,cancellation_reason,id_session,id_dog) VALUES
                                                                                                                ('2025-08-23','CONFIRMED',NULL,NULL,4,25),
                                                                                                                ('2025-08-23','CONFIRMED',NULL,NULL,4,26),
                                                                                                                ('2025-08-23','CONFIRMED',NULL,NULL,4,27),
                                                                                                                ('2025-08-23','CONFIRMED',NULL,NULL,4,28),
                                                                                                                ('2025-08-24','CONFIRMED',NULL,NULL,4,29),
                                                                                                                ('2025-08-24','CONFIRMED',NULL,NULL,4,30),
                                                                                                                ('2025-08-24','CONFIRMED',NULL,NULL,4,31),
                                                                                                                ('2025-08-24','CONFIRMED',NULL,NULL,4,32);

-- Sessione 5: cani 33..40
INSERT INTO registration (registration_date,status,cancellation_date,cancellation_reason,id_session,id_dog) VALUES
                                                                                                                ('2025-08-24','CONFIRMED',NULL,NULL,5,33),
                                                                                                                ('2025-08-24','CONFIRMED',NULL,NULL,5,34),
                                                                                                                ('2025-08-24','CONFIRMED',NULL,NULL,5,35),
                                                                                                                ('2025-08-24','CONFIRMED',NULL,NULL,5,36),
                                                                                                                ('2025-08-25','CONFIRMED',NULL,NULL,5,37),
                                                                                                                ('2025-08-25','CONFIRMED',NULL,NULL,5,38),
                                                                                                                ('2025-08-25','CONFIRMED',NULL,NULL,5,39),
                                                                                                                ('2025-08-25','CONFIRMED',NULL,NULL,5,40);

-- Sessione 6: cani 41..48
INSERT INTO registration (registration_date,status,cancellation_date,cancellation_reason,id_session,id_dog) VALUES
                                                                                                                ('2025-08-25','CONFIRMED',NULL,NULL,6,41),
                                                                                                                ('2025-08-25','CONFIRMED',NULL,NULL,6,42),
                                                                                                                ('2025-08-25','CONFIRMED',NULL,NULL,6,43),
                                                                                                                ('2025-08-25','CONFIRMED',NULL,NULL,6,44),
                                                                                                                ('2025-08-26','CONFIRMED',NULL,NULL,6,45),
                                                                                                                ('2025-08-26','CONFIRMED',NULL,NULL,6,46),
                                                                                                                ('2025-08-26','CONFIRMED',NULL,NULL,6,47),
                                                                                                                ('2025-08-26','CONFIRMED',NULL,NULL,6,48);

-- Sessioni 7..12: alcuni cani “a rotazione” (senza duplicati per sessione)
INSERT INTO registration (registration_date,status,cancellation_date,cancellation_reason,id_session,id_dog) VALUES
                                                                                                                ('2025-08-26','CONFIRMED',NULL,NULL,7,5),
                                                                                                                ('2025-08-26','CONFIRMED',NULL,NULL,7,12),
                                                                                                                ('2025-08-26','CONFIRMED',NULL,NULL,7,19),
                                                                                                                ('2025-08-26','CONFIRMED',NULL,NULL,7,26),
                                                                                                                ('2025-08-27','CONFIRMED',NULL,NULL,7,33),
                                                                                                                ('2025-08-27','CONFIRMED',NULL,NULL,7,40),

                                                                                                                ('2025-08-27','CONFIRMED',NULL,NULL,8,6),
                                                                                                                ('2025-08-27','CONFIRMED',NULL,NULL,8,13),
                                                                                                                ('2025-08-27','CONFIRMED',NULL,NULL,8,20),
                                                                                                                ('2025-08-27','CONFIRMED',NULL,NULL,8,27),
                                                                                                                ('2025-08-28','CONFIRMED',NULL,NULL,8,34),
                                                                                                                ('2025-08-28','CONFIRMED',NULL,NULL,8,41),

                                                                                                                ('2025-08-28','CONFIRMED',NULL,NULL,9,2),
                                                                                                                ('2025-08-28','CONFIRMED',NULL,NULL,9,9),
                                                                                                                ('2025-08-28','CONFIRMED',NULL,NULL,9,16),
                                                                                                                ('2025-08-28','CONFIRMED',NULL,NULL,9,23),

                                                                                                                ('2025-08-29','CONFIRMED',NULL,NULL,10,3),
                                                                                                                ('2025-08-29','CONFIRMED',NULL,NULL,10,10),
                                                                                                                ('2025-08-29','CONFIRMED',NULL,NULL,10,17),
                                                                                                                ('2025-08-29','CONFIRMED',NULL,NULL,10,24),

                                                                                                                ('2025-08-29','CONFIRMED',NULL,NULL,11,4),
                                                                                                                ('2025-08-29','CONFIRMED',NULL,NULL,11,11),
                                                                                                                ('2025-08-29','CONFIRMED',NULL,NULL,11,18),
                                                                                                                ('2025-08-29','CONFIRMED',NULL,NULL,11,25),

                                                                                                                ('2025-08-30','CONFIRMED',NULL,NULL,12,1),
                                                                                                                ('2025-08-30','CONFIRMED',NULL,NULL,12,8),
                                                                                                                ('2025-08-30','CONFIRMED',NULL,NULL,12,15),
                                                                                                                ('2025-08-30','CONFIRMED',NULL,NULL,12,22);
