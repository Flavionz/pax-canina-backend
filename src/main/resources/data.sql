-- 1. Utenti (ADMIN, COACH, PROPRIETAIRE)
INSERT INTO utilisateur (id_utilisateur, nom, prenom, email, password_hash, telephone, date_inscription, last_login, type_utilisateur)
VALUES
    (1, 'Dupont', 'Alice', 'alice@paxcanina.com', '$2a$10$uNJijirLSWel8ndVZyo19e3tHweXymwBLoxTUQ1gsqOFmyQtsHKvW', '0612345678', '2024-05-01', '2024-05-06 10:00:00', 'ADMIN'),
    (2, 'Martin', 'Bob', 'bob@paxcanina.com', '$2a$10$uNJijirLSWel8ndVZyo19e3tHweXymwBLoxTUQ1gsqOFmyQtsHKvW', '0698765432', '2024-05-02', '2024-05-06 09:00:00', 'COACH'),
    (3, 'Rossi', 'Carla', 'carla@paxcanina.com', '$2a$10$uNJijirLSWel8ndVZyo19e3tHweXymwBLoxTUQ1gsqOFmyQtsHKvW', '0678954321', '2024-05-03', '2024-05-06 08:30:00', 'PROPRIETAIRE'),
    (4, 'Verdi', 'Dario', 'dario@paxcanina.com', '$2a$10$uNJijirLSWel8ndVZyo19e3tHweXymwBLoxTUQ1gsqOFmyQtsHKvW', '0687654321', '2024-05-04', '2024-05-06 08:00:00', 'PROPRIETAIRE'),
    (5, 'Terenzi', 'Flavio', 'flavio@admin.com', '$2a$12$fqakxrv4uo/in3x2Yy1dC.VON4u09Y5/20UGXmrbYe7EXPspsspBS', '0687653321', '2024-05-04', '2024-05-06 08:00:00', 'PROPRIETAIRE');

-- 2. Admin, Coach e Proprietari (JOINED: stesso id_utilisateur)
INSERT INTO admin (id_utilisateur) VALUES (1);
INSERT INTO coach (id_utilisateur) VALUES (2);
INSERT INTO proprietaire (id_utilisateur, adresse, ville, code_postal, bio, avatar_url)
VALUES
    (3, 'Via Roma 1', 'Milano', '20100', 'Amante dei cani', 'https://randomuser.me/api/portraits/women/1.jpg'),
    (4, 'Via Milano 2', 'Torino', '10100', 'Dog lover', 'https://randomuser.me/api/portraits/men/2.jpg'),
    (5, 'Via Firenze 3', 'Firenze', '50100', 'Amico degli animali', 'https://randomuser.me/api/portraits/men/5.jpg');

-- 3. Specializzazioni
INSERT INTO specialisation (nom, description) VALUES
                                                  ('Agility', 'Corsi per agilità'),
                                                  ('Obedience', 'Obbedienza di base'),
                                                  ('Difesa', 'Protezione e difesa');

-- 4. Associazioni coach-specializzazione (ManyToMany)
INSERT INTO coach_specialisation (id_utilisateur, id_specialisation)
SELECT 2, id_specialisation FROM specialisation WHERE nom IN ('Agility', 'Obedience');

-- 5. Razze
INSERT INTO race (nom) VALUES
                           ('Labrador'),
                           ('Golden Retriever'),
                           ('Bulldog'),
                           ('Barboncino');

-- 6. Fasce d'età
INSERT INTO tranche_age (nom, age_min, age_max) VALUES
                                                    ('Cuccioli', 2, 12),
                                                    ('Adulti', 13, 120);

-- 7. Corsi
INSERT INTO cours (nom, description, statut)
VALUES
    ('Obbedienza Base', 'Corso base per l’obbedienza del cane', 'ATTIVO'),
    ('Agility', 'Corso di agility per cani energici', 'ATTIVO');

-- 8. Cani (senza id_chien, lasciato auto-increment)
INSERT INTO chien (nom, date_naissance, sexe, photo_url, numero_puce, poids, id_utilisateur, id_race)
VALUES
    ('Rocky', '2022-03-15', 'M', 'https://images.unsplash.com/photo-1', '123456789', 32.5, 3, 1),
    ('Luna', '2021-08-10', 'F', 'https://images.unsplash.com/photo-2', '987654321', 28.0, 3, 2),
    ('Max', '2020-12-05', 'M', 'https://images.unsplash.com/photo-3', '456789123', 24.7, 4, 3),
    ('Maya', '2023-01-20', 'F', 'https://images.unsplash.com/photo-4', '654321987', 8.9, 4, 4);

-- 9. Sessioni (senza id_session, lasciato auto-increment)
-- Nota: controlla che i valori di id_cours, id_tranche, id_coach corrispondano agli id generati sopra
INSERT INTO session (date, heure_debut, heure_fin, description, duree, capacite_max, niveau, id_tranche, id_cours, id_coach)
VALUES
    ('2024-06-10', '10:00:00', '11:00:00', 'Sessione mattutina', '01:00:00', 10, 'DEBUTANT', 1, 1, 2),
    ('2024-06-12', '15:00:00', '16:00:00', 'Sessione pomeridiana', '01:00:00', 12, 'INTERMEDIAIRE', 2, 2, 2);

-- 10. Iscrizioni (senza id_inscription, lasciato auto-increment)
-- Nota: i valori di id_chien e id_session devono corrispondere agli id generati sopra
INSERT INTO inscription (date_inscription, status, id_chien, id_session)
VALUES
    ('2024-06-01', 'ATTIVA', 1, 1),
    ('2024-06-01', 'ATTIVA', 2, 1),
    ('2024-06-02', 'ATTIVA', 3, 2),
    ('2024-06-02', 'ATTIVA', 4, 2);
