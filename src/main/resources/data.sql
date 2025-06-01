DELETE FROM inscription;
DELETE FROM chien;
DELETE FROM race;
DELETE FROM session;
DELETE FROM tranche_age;
DELETE FROM cours_specialisation;
DELETE FROM cours;
DELETE FROM coach_specialisation;
DELETE FROM specialisation;
DELETE FROM proprietaire;
DELETE FROM coach;
DELETE FROM admin;
DELETE FROM utilisateur;

-- 1. UTENTE proprietario Flavio
INSERT INTO utilisateur (
    id_utilisateur, nom, prenom, email, password_hash, telephone, date_inscription, avatar_url, bio, last_login
) VALUES (
             11, 'Terenzi', 'Flavio', 'flavio@proprietaire.com',
             '$2a$10$zvLV1cYKiTd6/4IMH6uXeeYsm6W4D5auN2BPGN0WjI0fRrVRqMbiC', -- password: qwerty
             '3331234567', '2024-06-01', 'https://randomuser.me/api/portraits/men/5.jpg', 'Proprietario Flavio', '2024-06-01 10:00:00'
         );

INSERT INTO proprietaire (id_utilisateur, adresse, ville, code_postal)
VALUES (11, 'Via Roma 1', 'Milano', '20100');

-- 2. UTENTE coach Mario Rossi
INSERT INTO utilisateur (
    id_utilisateur, nom, prenom, email, password_hash, telephone, date_inscription, avatar_url, bio, last_login
) VALUES (
             2, 'Rossi', 'Mario', 'mario@coach.com',
             '$2a$12$QKQwiAbwq2OwDmh7AyLOi.JljEkUL8D9OX6OfDttthDG1OQp5nxyi', -- password: admin
             '3339876543', '2024-04-15', 'https://randomuser.me/api/portraits/men/1.jpg', 'Coach esperto', '2024-05-21 09:30:00'
         );

INSERT INTO coach (id_utilisateur)
VALUES (2);

-- 3. UTENTE admin (opzionale)
INSERT INTO utilisateur (
    id_utilisateur, nom, prenom, email, password_hash, telephone, date_inscription, avatar_url, bio, last_login
) VALUES (
             3, 'Admin', 'Master', 'admin@admin.com',
             '$2a$12$QKQwiAbwq2OwDmh7AyLOi.JljEkUL8D9OX6OfDttthDG1OQp5nxyi', -- password: admin
             '3332223333', '2024-01-01', NULL, 'Super Admin', '2024-05-21 10:30:00'
         );

INSERT INTO admin (id_utilisateur)
VALUES (3);

-- 4. SPECIALIZZAZIONI
INSERT INTO specialisation (id_specialisation, nom) VALUES (1, 'Obbedienza'), (2, 'Agility');

-- 5. ASSOCIAZIONI coach-specializzazione
INSERT INTO coach_specialisation (id_utilisateur, id_specialisation) VALUES (2, 1);

-- 6. CORSI con img_url
INSERT INTO cours (id_cours, nom, description, statut, img_url) VALUES
                                                                    (1, 'Obbedienza Base', 'Corso base di obbedienza', 'ATTIVO', 'https://example.com/images/obedienza.jpg'),
                                                                    (2, 'Agility Dog', 'Percorso Agility', 'ATTIVO', 'https://example.com/images/agility.jpg');

-- 7. ASSOCIAZIONI corso-specializzazione
INSERT INTO cours_specialisation (id_cours, id_specialisation) VALUES (1, 1), (2, 2);

-- 8. FASCE ETÀ
INSERT INTO tranche_age (id_tranche, nom, age_min, age_max) VALUES
                                                                (1, 'Cuccioli', 2, 12),
                                                                (2, 'Adulti', 13, 120);

-- 9. SESSIONI (con coach) con img_url
INSERT INTO session (
    id_session, date, niveau, heure_debut, heure_fin, capacite_max, description, lieu, img_url, id_cours, id_tranche, id_utilisateur
) VALUES
      (1, '2024-06-10', 'DEBUTANT', '10:00:00', '11:00:00', 10, 'Sessione mattutina', 'Parco Nord', 'https://example.com/images/session1.jpg', 1, 1, 2),
      (2, '2024-06-11', 'INTERMEDIAIRE', '14:00:00', '15:00:00', 8, 'Sessione pomeridiana', 'Parco Sud', 'https://example.com/images/session2.jpg', 2, 2, 2);

-- 10. RAZZE
INSERT INTO race (id_race, nom) VALUES (1, 'Labrador'), (2, 'Barboncino');

-- 11. CANI di Flavio
INSERT INTO chien (
    id_chien, nom, date_naissance, sexe, photo_url, numero_puce, poids, id_proprietaire, id_race
) VALUES
      (1, 'Rocky', '2022-03-15', 'M', 'https://images.unsplash.com/photo-1', '123456789', 32.5, 11, 1),
      (2, 'Molly', '2023-01-05', 'F', 'https://images.unsplash.com/photo-2', '987654321', 12.2, 11, 2);

-- 12. ISCRIZIONI dei cani alle sessioni
INSERT INTO inscription (
    id_inscription, date_inscription, status, date_annulation, motif_annulation, id_session, id_chien
) VALUES
      (1, '2024-06-01', 'ATTIVA', NULL, NULL, 1, 1),
      (2, '2024-06-01', 'ATTIVA', NULL, NULL, 2, 2);
