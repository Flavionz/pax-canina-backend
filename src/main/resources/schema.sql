-- Table principale UTILISATEUR
CREATE TABLE IF NOT EXISTS utilisateur (
                                           id_utilisateur INT AUTO_INCREMENT PRIMARY KEY,
                                           nom VARCHAR(50) NOT NULL,
                                           prenom VARCHAR(50) NOT NULL,
                                           email VARCHAR(100) NOT NULL UNIQUE,
                                           password_hash VARCHAR(255) NOT NULL,
                                           telephone VARCHAR(20),
                                           date_inscription DATE NOT NULL,
                                           avatar_url VARCHAR(255),
                                           bio TEXT,
                                           last_login DATETIME
);

-- ADMIN
CREATE TABLE IF NOT EXISTS admin (
                                     id_utilisateur INT PRIMARY KEY,
                                     FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

-- COACH
CREATE TABLE IF NOT EXISTS coach (
                                     id_utilisateur INT PRIMARY KEY,
                                     FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

-- PROPRIETAIRE
CREATE TABLE IF NOT EXISTS proprietaire (
                                            id_utilisateur INT PRIMARY KEY,
                                            adresse VARCHAR(255),
                                            ville VARCHAR(100),
                                            code_postal VARCHAR(20),
                                            FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

-- SPECIALISATION
CREATE TABLE IF NOT EXISTS specialisation (
                                              id_specialisation INT AUTO_INCREMENT PRIMARY KEY,
                                              nom VARCHAR(100) NOT NULL
);

-- Association coach <-> specialisation (a_pour_specialisation)
CREATE TABLE IF NOT EXISTS coach_specialisation (
                                                    id_utilisateur INT,
                                                    id_specialisation INT,
                                                    PRIMARY KEY (id_utilisateur, id_specialisation),
                                                    FOREIGN KEY (id_utilisateur) REFERENCES coach(id_utilisateur),
                                                    FOREIGN KEY (id_specialisation) REFERENCES specialisation(id_specialisation)
);

-- COURS
CREATE TABLE IF NOT EXISTS cours (
                                     id_cours INT AUTO_INCREMENT PRIMARY KEY,
                                     nom VARCHAR(100) NOT NULL,
                                     description TEXT,
                                     statut VARCHAR(50),
                                     img_url VARCHAR(255) -- nuovo campo per immagine presentazione
);

-- Association cours <-> specialisation (requiert_specialisation)
CREATE TABLE IF NOT EXISTS cours_specialisation (
                                                    id_cours INT,
                                                    id_specialisation INT,
                                                    PRIMARY KEY (id_cours, id_specialisation),
                                                    FOREIGN KEY (id_cours) REFERENCES cours(id_cours),
                                                    FOREIGN KEY (id_specialisation) REFERENCES specialisation(id_specialisation)
);

-- TRANCHE_AGE
CREATE TABLE IF NOT EXISTS tranche_age (
                                           id_tranche INT AUTO_INCREMENT PRIMARY KEY,
                                           nom VARCHAR(100) NOT NULL,
                                           age_min INT,
                                           age_max INT
);

-- SESSION
CREATE TABLE IF NOT EXISTS session (
                                       id_session INT AUTO_INCREMENT PRIMARY KEY,
                                       date DATE NOT NULL,
                                       niveau VARCHAR(50),
                                       heure_debut TIME,
                                       heure_fin TIME,
                                       capacite_max INT,
                                       description TEXT,
                                       lieu VARCHAR(255),
                                       img_url VARCHAR(255), -- nuovo campo per immagine sessione
                                       id_cours INT NOT NULL,
                                       id_tranche INT NOT NULL,
                                       id_utilisateur INT NOT NULL, -- Coach che anima la sessione
                                       FOREIGN KEY (id_cours) REFERENCES cours(id_cours),
                                       FOREIGN KEY (id_tranche) REFERENCES tranche_age(id_tranche),
                                       FOREIGN KEY (id_utilisateur) REFERENCES coach(id_utilisateur)
);

-- RACE
CREATE TABLE IF NOT EXISTS race (
                                    id_race INT AUTO_INCREMENT PRIMARY KEY,
                                    nom VARCHAR(100) NOT NULL
);

-- CHIEN
CREATE TABLE IF NOT EXISTS chien (
                                     id_chien INT AUTO_INCREMENT PRIMARY KEY,
                                     nom VARCHAR(50) NOT NULL,
                                     date_naissance DATE,
                                     sexe VARCHAR(10),
                                     photo_url VARCHAR(255),
                                     numero_puce VARCHAR(50) UNIQUE,
                                     poids DECIMAL(5,2),
                                     id_proprietaire INT NOT NULL,
                                     id_race INT,
                                     FOREIGN KEY (id_proprietaire) REFERENCES proprietaire(id_utilisateur),
                                     FOREIGN KEY (id_race) REFERENCES race(id_race)
);

-- INSCRIPTION
CREATE TABLE IF NOT EXISTS inscription (
                                           id_inscription INT AUTO_INCREMENT PRIMARY KEY,
                                           date_inscription DATE NOT NULL,
                                           status VARCHAR(50),
                                           date_annulation DATE,
                                           motif_annulation TEXT,
                                           id_session INT NOT NULL,
                                           id_chien INT NOT NULL,
                                           FOREIGN KEY (id_session) REFERENCES session(id_session),
                                           FOREIGN KEY (id_chien) REFERENCES chien(id_chien),
                                           UNIQUE (id_session, id_chien) -- Un cane non può iscriversi due volte alla stessa sessione
);