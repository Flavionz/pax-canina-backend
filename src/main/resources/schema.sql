-- ========================================
-- 1. USERS & ROLES (USER, ADMIN, COACH, OWNER)
-- ========================================
CREATE TABLE IF NOT EXISTS user (
                                    id_user INT AUTO_INCREMENT PRIMARY KEY,
                                    last_name VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    registration_date DATE NOT NULL,
    avatar_url VARCHAR(255),
    bio TEXT,
    last_login DATETIME,
    email_verified BOOLEAN NOT NULL DEFAULT false   -- NEW FIELD: Email verified
    );

CREATE TABLE IF NOT EXISTS admin (
                                     id_user INT PRIMARY KEY,
                                     FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS coach (
                                     id_user INT PRIMARY KEY,
                                     FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS owner (
                                     id_user INT PRIMARY KEY,
                                     address VARCHAR(255),
    city VARCHAR(100),
    postal_code VARCHAR(20),
    FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE CASCADE
    );

-- ========================================
-- 0. VALIDATION TOKENS (NEW FOR EMAIL VALIDATION)
-- ========================================

CREATE TABLE IF NOT EXISTS validation_token (
                                                id INT AUTO_INCREMENT PRIMARY KEY,
                                                token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date DATETIME NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id_user) ON DELETE CASCADE
    );
-- ========================================
-- 2. SPECIALIZATIONS
-- ========================================
CREATE TABLE IF NOT EXISTS specialization (
                                              id_specialization INT AUTO_INCREMENT PRIMARY KEY,
                                              name VARCHAR(100) NOT NULL,
    description VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS coach_specialization (
                                                    id_user INT,
                                                    id_specialization INT,
                                                    PRIMARY KEY (id_user, id_specialization),
    FOREIGN KEY (id_user) REFERENCES coach(id_user) ON DELETE CASCADE,
    FOREIGN KEY (id_specialization) REFERENCES specialization(id_specialization) ON DELETE CASCADE
    );

-- ========================================
-- 3. COURSES & RELATIONS
-- ========================================
CREATE TABLE IF NOT EXISTS course (
                                      id_course INT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR(100) NOT NULL,
    description TEXT,
    status VARCHAR(50),
    image_url VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS course_specialization (
                                                     id_course INT,
                                                     id_specialization INT,
                                                     PRIMARY KEY (id_course, id_specialization),
    FOREIGN KEY (id_course) REFERENCES course(id_course) ON DELETE CASCADE,
    FOREIGN KEY (id_specialization) REFERENCES specialization(id_specialization) ON DELETE CASCADE
    );

-- ========================================
-- 4. AGE GROUPS (ENUM as STRING)
-- ========================================
CREATE TABLE IF NOT EXISTS age_group (
                                         id_age_group INT AUTO_INCREMENT PRIMARY KEY,
                                         name VARCHAR(100) NOT NULL,      -- Enum: PUPPY/JUNIOR/YOUNG_ADULT/ADULT
    min_age INT,                     -- In months (INT)
    max_age INT                      -- In months, can be NULL
    );

-- ========================================
-- 5. SESSIONS (uses coach, course, age_group)
-- ========================================
CREATE TABLE IF NOT EXISTS session (
                                       id_session INT AUTO_INCREMENT PRIMARY KEY,
                                       date DATE NOT NULL,
                                       level VARCHAR(50),
    start_time TIME,
    end_time TIME,
    max_capacity INT,
    description TEXT,
    location VARCHAR(255),
    image_url VARCHAR(255),
    id_course INT NOT NULL,
    id_age_group INT NOT NULL,
    id_user INT NOT NULL,            -- Coach creating the session
    FOREIGN KEY (id_course) REFERENCES course(id_course) ON DELETE CASCADE,
    FOREIGN KEY (id_age_group) REFERENCES age_group(id_age_group) ON DELETE CASCADE,
    FOREIGN KEY (id_user) REFERENCES coach(id_user) ON DELETE CASCADE
    );

-- ========================================
-- 6. BREEDS
-- ========================================
CREATE TABLE IF NOT EXISTS breed (
                                     id_breed INT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(100) NOT NULL
    );

-- ========================================
-- 7. DOGS
-- ========================================
CREATE TABLE IF NOT EXISTS dog (
                                   id_dog INT AUTO_INCREMENT PRIMARY KEY,
                                   name VARCHAR(50) NOT NULL,
    birth_date DATE,
    sex VARCHAR(10),
    photo_url VARCHAR(255),
    chip_number VARCHAR(50) UNIQUE,
    weight DECIMAL(5,2),
    id_owner INT NOT NULL,
    id_breed INT,
    FOREIGN KEY (id_owner) REFERENCES owner(id_user) ON DELETE CASCADE,
    FOREIGN KEY (id_breed) REFERENCES breed(id_breed) ON DELETE SET NULL
    );

-- ========================================
-- 8. REGISTRATIONS
-- ========================================
CREATE TABLE IF NOT EXISTS registration (
                                            id_registration INT AUTO_INCREMENT PRIMARY KEY,
                                            registration_date DATE NOT NULL,
                                            status VARCHAR(50),
    cancellation_date DATE,
    cancellation_reason TEXT,
    id_session INT NOT NULL,
    id_dog INT NOT NULL,
    FOREIGN KEY (id_session) REFERENCES session(id_session) ON DELETE CASCADE,
    FOREIGN KEY (id_dog) REFERENCES dog(id_dog) ON DELETE CASCADE,
    UNIQUE (id_session, id_dog) -- Each dog can be registered only once per session
    );
