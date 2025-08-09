-- CLEAN
DELETE FROM registration;
DELETE FROM dog;
DELETE FROM breed;
DELETE FROM session;
DELETE FROM age_group;
DELETE FROM course_specialization;
DELETE FROM course;
DELETE FROM coach_specialization;
DELETE FROM specialization;
DELETE FROM owner;
DELETE FROM coach;
DELETE FROM admin;
DELETE FROM validation_token;
DELETE FROM user;

-- ADMIN
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

-- SPECIALIZATIONS
INSERT INTO specialization (id_specialization, name, description) VALUES
                                                                      (1, 'Obedience', 'Base obedience skills'),
                                                                      (2, 'Agility', 'Agility courses and obstacles'),
                                                                      (3, 'Defense', 'Guards and Defense Speciality');



-- AGE GROUPS
INSERT INTO age_group (id_age_group, name, min_age, max_age) VALUES
                                                                 (1, 'PUPPY', 0, 4),
                                                                 (2, 'JUNIOR', 5, 11),
                                                                 (3, 'ADULT', 12, NULL);

-- COURSES
INSERT INTO course (id_course, name, description, status, image_url) VALUES
                                                                         (1, 'Éducation canine', 'Cours pour apprendre à votre compagnon les bases de l’obéissance, favoriser une communication claire et renforcer la relation maître-chien', 'ACTIVE', 'https://example.com/images/obedience.jpg'),
                                                                         (2, 'Agility', 'Un sport canin dynamique où le chien franchit des obstacles sous la conduite de son maître, développant vitesse, précision et complicité', 'ACTIVE', 'https://example.com/images/agility.jpg'),
                                                                         (3, 'Garde et défense', 'Entraînement encadré visant à développer l’instinct de protection du chien, dans le respect des règles de sécurité et du bien-être animal', 'ACTIVE', 'https://example.com/images/agility.jpg');
;

-- RELAZIONE COURSE ↔ SPECIALIZATION
INSERT INTO course_specialization (id_course, id_specialization) VALUES
                                                                     (1, 1),
                                                                     (2, 2),
                                                                     (3, 3);

