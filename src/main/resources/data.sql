INSERT INTO utilisateur (nom, prenom, email, password_hash, telephone, date_inscription, last_login, type_utilisateur)
VALUES
    ('Dupont', 'Alice', 'alice@paxcanina.com', '$2a$10$uNJijirLSWel8ndVZyo19e3tHweXymwBLoxTUQ1gsqOFmyQtsHKvW', '0612345678', '2024-05-01', '2024-05-06 10:00:00', 'ADMIN'),
    ('Martin', 'Bob', 'bob@paxcanina.com', '$2a$10$uNJijirLSWel8ndVZyo19e3tHweXymwBLoxTUQ1gsqOFmyQtsHKvW', '0698765432', '2024-05-02', '2024-05-06 09:00:00', 'COACH'),
    ('Rossi', 'Carla', 'carla@paxcanina.com', '$2a$10$uNJijirLSWel8ndVZyo19e3tHweXymwBLoxTUQ1gsqOFmyQtsHKvW', '0678954321', '2024-05-03', '2024-05-06 08:30:00', 'PROPRIETAIRE'),
    ('Verdi', 'Dario', 'dario@paxcanina.com', '$2a$10$uNJijirLSWel8ndVZyo19e3tHweXymwBLoxTUQ1gsqOFmyQtsHKvW', '0687654321', '2024-05-04', '2024-05-06 08:00:00', 'PROPRIETAIRE');


INSERT INTO race (nom) VALUES
                           ('Labrador'),
                           ('Golden Retriever'),
                           ('Bulldog'),
                           ('Barboncino');


INSERT INTO chien (nom, date_naissance, sexe, photo_url, id_proprietaire, id_race)
VALUES
    ('Rocky', '2022-03-15', 'M', 'https://images.unsplash.com/photo-1', 3, 1),
    ('Luna', '2021-08-10', 'F', 'https://images.unsplash.com/photo-2', 3, 2),
    ('Max', '2020-12-05', 'M', 'https://images.unsplash.com/photo-3', 4, 3),
    ('Maya', '2023-01-20', 'F', 'https://images.unsplash.com/photo-4', 4, 4);


INSERT INTO cours (nom, description)
VALUES
    ('Obbedienza Base', 'Corso base per l’obbedienza del cane'),
    ('Agility', 'Corso di agility per cani energici');


INSERT INTO session (date_heure, lieu, id_cours, id_coach)
VALUES
    ('2024-06-10 10:00:00', 'Parco Nord', 1, 2),
    ('2024-06-12 15:00:00', 'Parco Sud', 2, 2);


INSERT INTO inscription (id_session, id_chien)
VALUES
    (1, 1),
    (1, 2),
    (2, 3),
    (2, 4);
