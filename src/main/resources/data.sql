-- 1. CLEANUP
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
DELETE FROM user;

-- 2. USERS & ROLES
-- Coach (Mario Rossi), Coach (Elena Bianchi), Owner (Flavio Terenzi), Admin (Admin Master)
INSERT INTO user (id_user, last_name, first_name, email, password_hash, phone, registration_date, avatar_url, bio, last_login)
VALUES
    (2, 'Rossi', 'Mario', 'mario@coach.com', '$2a$12$QKQwiAbwq2OwDmh7AyLOi.JljEkUL8D9OX6OfDttthDG1OQp5nxyi', '3339876543', '2024-04-15', 'https://randomuser.me/api/portraits/men/1.jpg', 'Esperto in agility e obedience', '2024-06-30 09:30:00'),
    (3, 'Admin', 'Master', 'admin@admin.com', '$2a$12$xqlCUYAxHlTKLAB0YMlo4.SY9D59GxE/250F0jnZL1egn9bT0OHyG', '3332223333', '2024-01-01', NULL, 'Super Admin', '2024-05-21 10:30:00'),
    (11, 'Terenzi', 'Flavio', 'flavio@owner.com', '$2a$10$zvLV1cYKiTd6/4IMH6uXeeYsm6W4D5auN2BPGN0WjI0fRrVRqMbiC', '3331234567', '2024-06-01', 'https://randomuser.me/api/portraits/men/5.jpg', 'Owner Flavio', '2024-06-01 10:00:00'),
    (21, 'Bianchi', 'Elena', 'elena@coach.com', '$2a$12$abcdefgQKQwiAbwq2OwDmh7AyLOiJljEkUL8D9OX6OfDttthDG1OQp5nxyi', '3332225566', '2024-05-12', 'https://randomuser.me/api/portraits/women/2.jpg', 'Specializzata in Puppy School', '2024-06-30 08:15:00');

INSERT INTO coach (id_user) VALUES (2), (21);
INSERT INTO owner (id_user, address, city, postal_code) VALUES (11, 'Via Roma 1', 'Milan', '20100');
INSERT INTO admin (id_user) VALUES (3);

-- 3. SPECIALIZZAZIONI
INSERT INTO specialization (id_specialization, name) VALUES
                                                         (1, 'Obedience'), (2, 'Agility'), (3, 'Puppy School'), (4, 'Dog Dancing'), (5, 'Trick Training');

-- 4. COACH-SPECIALIZZAZIONE (molti a molti)
INSERT INTO coach_specialization (id_user, id_specialization) VALUES
                                                                  (2, 1), (2, 2), (2, 5),    -- Mario: Obedience, Agility, Trick Training
                                                                  (21, 3), (21, 4);          -- Elena: Puppy School, Dog Dancing

-- 5. CORSI
INSERT INTO course (id_course, name, description, status, image_url) VALUES
                                                                         (1, 'Obedience Basics', 'Corso base di obbedienza', 'ACTIVE', 'https://example.com/images/obedience.jpg'),
                                                                         (2, 'Agility Dog', 'Corso agility con ostacoli', 'ACTIVE', 'https://example.com/images/agility.jpg'),
                                                                         (3, 'Puppy School', 'Per cuccioli 2–12 mesi', 'ACTIVE', NULL),
                                                                         (4, 'Dog Dancing', 'Trucchi di danza col cane', 'INACTIVE', 'https://example.com/images/dogdancing.jpg'),
                                                                         (5, 'Trick Masterclass', 'Impara i trick avanzati', 'ACTIVE', 'https://example.com/images/tricks.jpg');

-- 6. COURSE-SPECIALIZATION (molti a molti)
INSERT INTO course_specialization (id_course, id_specialization) VALUES
                                                                     (1, 1), -- Obedience Basics → Obedience
                                                                     (2, 2), -- Agility Dog → Agility
                                                                     (3, 3), -- Puppy School → Puppy School
                                                                     (4, 4), -- Dog Dancing → Dog Dancing
                                                                     (5, 5), -- Trick Masterclass → Trick Training
                                                                     (2, 5); -- Agility Dog richiede anche Trick Training

-- 7. AGE GROUPS
INSERT INTO age_group (id_age_group, name, min_age, max_age) VALUES
                                                                 (1, 'PUPPY', 0, 4),
                                                                 (2, 'JUNIOR', 5, 11),
                                                                 (3, 'YOUNG_ADULT', 12, 23),
                                                                 (4, 'ADULT', 24, NULL);

-- 8. SESSIONI
INSERT INTO session (id_session, date, level, start_time, end_time, max_capacity, description, location, image_url, id_course, id_age_group, id_user)
VALUES
    (1, '2024-07-10', 'BEGINNER', '10:00:00', '11:00:00', 10, 'Sessione base obedience', 'North Park', 'https://example.com/images/session1.jpg', 1, 1, 2),
    (2, '2024-07-12', 'INTERMEDIATE', '14:00:00', '15:00:00', 8, 'Agility intermedia', 'South Park', 'https://example.com/images/session2.jpg', 2, 2, 2),
    (3, '2024-07-15', 'BEGINNER', '09:30:00', '10:30:00', 15, 'Puppy speciale', 'West Park', NULL, 3, 1, 21);

-- 9. RAZZE
INSERT INTO breed (id_breed, name) VALUES
                                       (1, 'Labrador Retriever'),
                                       (2, 'Poodle'),
                                       (3, 'Border Collie');

-- 10. CANI (Flavio owner)
INSERT INTO dog (id_dog, name, birth_date, sex, photo_url, chip_number, weight, id_owner, id_breed)
VALUES
    (1, 'Rocky', '2022-03-15', 'M', 'https://images.unsplash.com/photo-1', '123456789', 32.5, 11, 1),
    (2, 'Molly', '2023-01-05', 'F', 'https://images.unsplash.com/photo-2', '987654321', 12.2, 11, 2);

-- 11. REGISTRAZIONI
INSERT INTO registration (id_registration, registration_date, status, cancellation_date, cancellation_reason, id_session, id_dog)
VALUES
    (1, '2024-07-01', 'ACTIVE', NULL, NULL, 1, 1),
    (2, '2024-07-02', 'ACTIVE', NULL, NULL, 2, 2),
    (3, '2024-07-03', 'CANCELLED', '2024-07-05', 'Illness', 3, 1);
