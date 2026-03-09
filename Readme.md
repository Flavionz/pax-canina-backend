# 🐾 Pax Canina – Backend

> Backend de l'application de gestion de club canin, développé dans le cadre du projet fil rouge CDA 2025.  
> Ce serveur gère l'authentification, la logique métier, la sécurité et la persistance des données du club.

---

## 🚩 Description du Projet

Pax Canina vise à digitaliser et centraliser la gestion d'un club canin :  
gestion des membres, chiens, cours, sessions, staff, inscriptions, calendrier et documents,  
avec un contrôle des accès par rôle et des règles métier robustes.

**Objectif :** simplifier la gestion pour le staff, améliorer l'expérience utilisateur et garantir la conformité RGPD.

---

## 🛠️ Fonctionnalités Clés

- 👤 Gestion des utilisateurs avec rôles (**Admin, Coach, Propriétaire**)
- 🔐 Authentification sécurisée (**JWT, Spring Security, BCrypt**)
- 🐕 Gestion des **chiens** : races, groupes d'âge, niveaux (CRUD complet)
- 📅 Gestion des **cours** et **sessions** avec contrôle des capacités
- 📋 Gestion des **inscriptions** avec validation métier (âge/niveau du chien)
- 📁 Upload et gestion sécurisée de fichiers (avatars, documents) via **Nginx**
- ✉️ Envoi d'emails de validation et réinitialisation de mot de passe (**MailDev**)
- 🗄️ Initialisation automatique BDD (`data.sql` + `schema.sql`)
- ⚙️ Configuration centralisée via `.env` (dev / prod)
- 🐳 Architecture modulaire prête pour **Docker**
- 🔁 Pipeline **CI/CD** GitHub Actions

---

## 🚀 Lancer le projet

```bash
# 1. Copier et configurer les variables d'environnement
cp .env.example .env

# 2. Lancer tous les services (MySQL, phpMyAdmin, Nginx, MailDev)
docker-compose up -d

# 3. L'API est disponible sur
http://localhost:8080/api

# 4. phpMyAdmin disponible sur
http://localhost:8081

# 5. MailDev (emails de dev) disponible sur
http://localhost:1080
```

---

## 📁 Structure du Projet

```plaintext
pax-canina-backend/
├── .env.example                  # Variables d'environnement (template)
├── docker-compose.example.yml    # Template Docker Compose
├── docker-compose.yml            # Config Docker locale (ignorée par git)
├── Dockerfile                    # Image de production
├── nginx.conf                    # Config proxy Nginx
├── pom.xml                       # Dépendances Maven
├── .github/
│   └── workflows/
│       └── ci.yml                # Pipeline CI/CD GitHub Actions
├── uploads/
│   └── public/                   # Fichiers uploadés (avatars, docs)
└── src/
    ├── main/
    │   ├── java/com/flavio/paxcanina/
    │   │   ├── PaxcaninaApplication.java
    │   │   ├── annotation/       # Validation fichiers (@ValidFile)
    │   │   ├── controller/       # Endpoints REST (13 controllers)
    │   │   ├── dao/              # Accès données (Spring Data JPA)
    │   │   ├── dto/              # Objets de transfert de données
    │   │   ├── exception/        # Gestion globale des erreurs
    │   │   ├── mapper/           # Conversion entités ↔ DTOs
    │   │   ├── model/            # Entités JPA
    │   │   ├── repository/       # Repositories Spring Data
    │   │   ├── security/         # JWT, Spring Security, Filters
    │   │   └── service/          # Logique métier
    │   └── resources/
    │       ├── application.properties
    │       ├── application-dev.properties
    │       ├── application-prod.properties
    │       ├── data.sql           # Données initiales
    │       └── schema.sql         # Schéma BDD
    └── test/
        └── java/com/flavio/paxcanina/
            ├── controller/        # Tests d'intégration sécurité
            └── service/           # Tests unitaires services
```

*Chaque dossier représente une couche ou une responsabilité métier (architecture claire et évolutive).*

---

## 🧰 Technologies Utilisées

| Partie       | Technologie                        |
|--------------|------------------------------------|
| Backend      | Java 17, Spring Boot 3             |
| Auth         | JWT, Spring Security, BCrypt       |
| Base données | MySQL 8.0                          |
| ORM          | Hibernate (Spring Data JPA)        |
| Upload       | Spring Multipart + Nginx           |
| Email        | JavaMailSender + MailDev           |
| DevOps       | Docker, docker-compose, GitHub Actions |
| Build        | Maven                              |
| Tests        | JUnit 5, Mockito, MockMvc          |

---

## 🏗️ Architecture

L'application suit une architecture **multi-couche** stricte :

```
Controller → Service → DAO → Model
```

| Couche       | Rôle                                                  |
|--------------|-------------------------------------------------------|
| Controller   | Réception requêtes HTTP, validation entrées, routing  |
| Service      | Logique métier, orchestration, transactions           |
| DAO          | Accès données via Spring Data JPA                     |
| Model        | Entités JPA mappées sur la base de données            |
| DTO          | Objets de transfert — découplage API/modèle           |
| Security     | Filtres JWT, configuration Spring Security, RBAC      |
| Exception    | Gestion centralisée des erreurs (`GlobalExceptionHandler`) |

---

## 🔐 Sécurité

- **JWT** : tokens signés, expiration configurable, filtre à chaque requête (`JwtAuthenticationFilter`)
- **BCrypt** : mots de passe jamais stockés en clair
- **RBAC** : contrôle d'accès par rôle (`ROLE_ADMIN`, `ROLE_COACH`, `ROLE_OWNER`)
- **Validation fichiers** : annotations personnalisées (`@ValidFile`) avec contrôle type/taille
- **Variables sensibles** : externalisées via `.env`, jamais committées
- **RGPD** : données personnelles protégées, accès tracé

---

## ✔️ Bonnes Pratiques

- Architecture **multi-couche** claire et évolutive
- Séparation des responsabilités (**SRP**, **SOLID**)
- Conventions de nommage Java et Spring
- Endpoints protégés, contrôle par rôle (**RBAC**)
- Gestion centralisée des exceptions (`GlobalExceptionHandler`)
- Configuration par profil (`dev` / `prod`)
- Conteneurisation **Docker-ready**
- Pipeline **CI/CD** GitHub Actions
- Respect de la confidentialité (**RGPD**)

---

## 👨‍💻 Auteur

**Flavio Terenzi**  
Projet CDA – Metz Numeric School – 2025  
🌐 [flavioterenzi.vercel.app](https://flavioterenzi.vercel.app) | 💼 [LinkedIn](https://linkedin.com/in/flavioterenzi) |