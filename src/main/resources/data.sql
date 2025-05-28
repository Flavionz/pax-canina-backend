-- 1. Inserisci utenti (ADMIN, COACH, PROPRIETAIRE)
INSERT INTO utilisateur (id_utilisateur, nom, prenom, email, password_hash, telephone, date_inscription, last_login, type_utilisateur)
VALUES
    (1, 'Dupont', 'Alice', 'alice@paxcanina.com', '$2a$10$uNJijirLSWel8ndVZyo19e3tHweXymwBLoxTUQ1gsqOFmyQtsHKvW', '0612345678', '2024-05-01', '2024-05-06 10:00:00', 'ADMIN'),
    (2, 'Martin', 'Bob', 'bob@paxcanina.com', '$2a$10$uNJijirLSWel8ndVZyo19e3tHweXymwBLoxTUQ1gsqOFmyQtsHKvW', '0698765432', '2024-05-02', '2024-05-06 09:00:00', 'COACH'),
    (3, 'Rossi', 'Carla', 'carla@paxcanina.com', '$2a$10$uNJijirLSWel8ndVZyo19e3tHweXymwBLoxTUQ1gsqOFmyQtsHKvW', '0678954321', '2024-05-03', '2024-05-06 08:30:00', 'PROPRIETAIRE'),
    (4, 'Verdi', 'Dario', 'dario@paxcanina.com', '$2a$10$uNJijirLSWel8ndVZyo19e3tHweXymwBLoxTUQ1gsqOFmyQtsHKvW', '0687654321', '2024-05-04', '2024-05-06 08:00:00', 'PROPRIETAIRE'),
    (5, 'Terenzi', 'Flavio', 'flavio@admin.com', '$2a$12$fqakxrv4uo/in3x2Yy1dC.VON4u09Y5/20UGXmrbYe7EXPspsspBS', '0687653321', '2024-05-04', '2024-05-06 08:00:00', 'ADMIN');


-- 2. Inserisci admin, coach e proprietari (JOINED: stesso id_utilisateur)
INSERT INTO admin (id_utilisateur) VALUES (1);
INSERT INTO coach (id_utilisateur) VALUES (2);
INSERT INTO proprietaire (id_utilisateur, adresse, ville, code_postal, bio, avatar_url)
VALUES
    (3, 'Via Roma 1', 'Milano', '20100', 'Amante dei cani', 'https://randomuser.me/api/portraits/women/1.jpg'),
    (4, 'Via Milano 2', 'Torino', '10100', 'Dog lover', 'https://randomuser.me/api/portraits/men/2.jpg');

-- 3. Razze
INSERT INTO race (id_race, nom) VALUES
                                    (1, 'Labrador'),
                                    (2, 'Golden Retriever'),
                                    (3, 'Bulldog'),
                                    (4, 'Barboncino');

-- 4. Cani (devono riferirsi a proprietari esistenti)
INSERT INTO chien (id_chien, nom, date_naissance, sexe, photo_url, id_proprietaire, id_race)
VALUES
    (1, 'Rocky', '2022-03-15', 'M', 'https://images.unsplash.com/photo-1', 3, 1),
    (2, 'Luna', '2021-08-10', 'F', 'https://images.unsplash.com/photo-2', 3, 2),
    (3, 'Max', '2020-12-05', 'M', 'https://images.unsplash.com/photo-3', 4, 3),
    (4, 'Maya', '2023-01-20', 'F', 'https://images.unsplash.com/photo-4', 4, 4);

-- 5. Corsi
INSERT INTO cours (id_cours, nom, description)
VALUES
    (1, 'Obbedienza Base', 'Corso base per l’obbedienza del cane'),
    (2, 'Agility', 'Corso di agility per cani energici');

-- 6. Sessioni (id_coach deve esistere)
INSERT INTO session (id_session, date_heure, lieu, id_cours, id_coach)
VALUES
    (1, '2024-06-10 10:00:00', 'Parco Nord', 1, 2),
    (2, '2024-06-12 15:00:00', 'Parco Sud', 2, 2);

-- 7. Iscrizioni (sessioni e cani devono esistere)
INSERT INTO inscription (id_session, id_chien)
VALUES
    (1, 1),
    (1, 2),
    (2, 3),
    (2, 4);
