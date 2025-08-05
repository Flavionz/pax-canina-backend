# 🐾 Pax Canina – Backend

Backend de l’application de gestion de club canin, développé dans le cadre du projet fil rouge CDA 2025.  
Ce serveur gère l’authentification, la logique métier, la sécurité et la persistance des données du club.

---

## 🚩 Description du Projet

Pax Canina vise à digitaliser et centraliser la gestion d’un club canin :  
gestion des membres, chiens, cours, sessions, staff, inscriptions, calendrier et documents, avec un contrôle des accès par rôle et des règles métier robustes.  
L’objectif : simplifier la gestion pour le staff, améliorer l’expérience utilisateur et garantir la conformité RGPD.

---

## 🛠️ Fonctionnalités Clés

- Gestion des utilisateurs (**Admin, Coach, Utilisateur**)
- Authentification sécurisée (**JWT, Spring Security**)
- Gestion des **cours**, **sessions**, **chiens** et **inscriptions** (CRUD complet)
- Contrôle métier : capacités max, validation âge/niveau chiens
- Upload et gestion sécurisée de fichiers (avatars, documents)
- Initialisation automatique BDD (`data.sql`)
- Configuration centralisée (`.env`)
- Architecture modulaire prête pour Docker

---

## 📁 Structure du Projet

```plaintext
projet-fil-rouge-backend/
├── docker-compose.yml
├── pom.xml
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── flavio/
│       │           └── paxcanina/
│       │               ├── controller/
│       │               ├── service/
│       │               ├── model/
│       │               ├── repository/
│       │               ├── security/
│       │               ├── annotation/
│       │               └── dto/
│       └── resources/
│           ├── application.yml
│           └── data.sql
└── .env.example
```

*Chaque dossier représente une couche ou une responsabilité métier (architecture claire et évolutive).*

---

## 🧰 Technologies Utilisées

| Partie      | Technologie |
|-------------|-----------|
| Backend     | Java 17, Spring Boot 3 |
| Auth        | JWT, Spring Security |
| BDD         | MySQL     |
| ORM         | Hibernate (JPA) |
| Outils      | Git, Maven, Docker |

---

## ✔️ Bonnes Pratiques

- Architecture **multi-couche** claire et évolutive
- Séparation des responsabilités (**SRP**, **SOLID**)
- Convention de nommage (Java, Spring)
- Sécurité : endpoints protégés, contrôle par rôle (**RBAC**)
- Préparation à la conteneurisation (**Docker-ready**)
- Respect de la confidentialité (**RGPD**)
- Initialisation & configuration automatisées

---

## 🛡️ Sécurité & RGPD

- Authentification JWT robuste
- Gestion précise des rôles et permissions
- Protection des données personnelles et fichiers uploadés
- Traçabilité et gestion des accès (logs)

---

## 👨‍💻 Auteur

**Flavio Terenzi**  
Projet CDA – Metz Numeric School - 2025