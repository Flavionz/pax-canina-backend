# PaxCanina – Backend

Ce dépôt contient la **partie backend** du mon projet fil rouge.  
Il s'agit d'une structure de départ pour une application de gestion de services canins, réalisée en Java Spring Boot.

## Statut du projet

Projet en cours de développement – structure initiale pour démarrer l’implémentation.

## Fonctionnalités principales (version de départ)

- Inscription des propriétaires avec contrôle de l’unicité de l’email
- Sécurité de base avec Spring Security (inscription ouverte, autres endpoints protégés)
- Initialisation de la base de données avec `data.sql`
- Organisation du code en couches : Controller, Service, DAO, Model
- Gestion de la configuration avec un fichier `.env` (non inclus dans le dépôt)
