-- ==================================================
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

-- ==================================================
-- 2. ADMIN USER FOR TESTING
-- PASSWORD (bcrypt hash for "admin")
INSERT INTO user (
    id_user, last_name, first_name, email, password_hash, phone, registration_date, avatar_url, bio, last_login, email_verified
) VALUES (
             1, 'Admin', 'Master', 'admin@admin.com', '$2a$12$0HbMw63xaW7ckkG56MCrEeqVgJSJg34kzV1pFBfWO1eHu6j5SkDaq',
             '3332223333', '2024-01-01', NULL, 'Super Admin', '2024-05-21 10:30:00', true
         );

INSERT INTO admin (id_user) VALUES (1);

-- ==================================================
-- 3. ADD BASE DATA ONLY IF NEEDED (e.g. age_group, specialization, course)
-- Do **NOT** create actual coach/owner/dogs/sessions/courses
-- If you want a completely empty platform, you can comment out these rows.
-- Example: create at least 2 dummy specializations and 2 dummy courses to avoid an empty interface

INSERT INTO specialization (id_specialization, name, description) VALUES
                                                                      (1, 'Obedience', 'Base obedience skills'),
                                                                      (2, 'Agility', 'Agility courses and obstacles');

INSERT INTO age_group (id_age_group, name, min_age, max_age) VALUES
                                                                 (1, 'PUPPY', 0, 4),
                                                                 (2, 'JUNIOR', 5, 11),
                                                                 (3, 'ADULT', 12, NULL);

INSERT INTO course (id_course, name, description, status, image_url) VALUES
                                                                         (1, 'Obedience Basics', 'Corso base di obbedienza', 'ACTIVE', 'https://example.com/images/obedience.jpg'),
                                                                         (2, 'Agility Dog', 'Corso agility con ostacoli', 'ACTIVE', 'https://example.com/images/agility.jpg');

INSERT INTO course_specialization (id_course, id_specialization) VALUES
                                                                     (1, 1), -- Obedience Basics → Obedience
                                                                     (2, 2); -- Agility Dog → Agility

