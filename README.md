# GamePulse - Game Server Launcher 🎮
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

GamePulse est une plateforme de gestion et d'administration de serveurs de jeu en ligne (Game Server Launcher). Conçue comme une application SaaS (Software as a Service) moderne, elle permet aux utilisateurs de créer, gérer et surveiller leurs instances de serveurs de jeu à travers une interface web premium et une API backend robuste et sécurisée.

## 🚀 Fonctionnalités Principales

*   **🕹️ Déploiement à la demande** : Lancez des instances de serveurs (Minecraft, Satisfactory, Palworld, etc.) basées sur des modèles prédéfinis (`GameTemplate`).
*   **⚙️ Administration des instances** :
    *   **Cycle de vie** : Démarrage asynchrone, Arrêt sécurisé avec confirmation, Redémarrage.
    *   **Paramétrage** : Mise à jour du nom de l'instance, du nombre de joueurs max, du niveau de confidentialité (Public/Privé) et du mot de passe.
    *   **Destruction** : Suppression définitive des instances obsolètes.
*   **📊 Supervision en Temps Réel** : 
    *   Un dashboard interactif interrogeant l'API sans rechargement (Polling asynchrone) affichant l'état exact des instances (`ONLINE`, `OFFLINE`, `STARTING`).
    *   Indicateurs de consommation (CPU, RAM, Stockage) gérés activement par un moteur de simulation backend intégré.
*   **🔒 Sécurité "Zero Trust" & RBAC** : 
    *   **Backend** : Authentification via **Spring Security** (HTTP Basic Auth). Gestion stricte des rôles (RBAC) différenciant les profils **ADMIN** (création/gestion totale) des profils **USER** (lecture seule).
    *   **Frontend** : Sécurisation globale via **Vue Router Navigation Guards** empêchant l'accès aux interfaces sans authentification valide, avec redirection intelligente.
*   **✨ Interface Premium** : Frontend "Glassmorphism" réactif, avec centralisation CSS, animations fluides (spinners, transitions d'état) et design system cohérent.

---

## 🛠️ Stack Technique

### Backend (Java / Spring Boot)
*   **Framework** : Spring Boot 3.3.x
*   **Langage** : Java 21
*   **Sécurité** : Spring Security (RBAC, Authentication sécurisée)
*   **Persistance** : Spring Data JPA (Hibernate)
*   **Base de Données** : PostgreSQL 15
*   **Build Tool** : Maven

### Frontend (Vue.js)
*   **Framework** : Vue.js 3 (Composition API `<script setup>`)
*   **Build Tool** : Vite
*   **Routage** : Vue Router
*   **Style** : CSS Vanilla centralisé (Glassmorphism design system, responsif)
*   **Appels API** : Axios avec Intercepteurs (Authentication Headers dynamiques)

### DevOps & Infrastructure
*   **Conteneurisation** : Docker & Docker Compose (Base de données, Backend et Web Server Frontend).

---

## 🏃 Démarrer le projet

### Étape 1 : Récupérer le projet

```bash
git clone https://github.com/votre-nom/private-game-server-project.git
cd private-game-server-project
```

### Étape 2 : Lancement express (Production / Docker)

Le fichier `docker-compose.yml` inclut les 3 services (DB, Backend, Frontend). Pour tout lancer d'un coup :

```bash
docker compose up --build -d
```
*Le frontend sera disponible sur `http://localhost:80` et le backend sur `http://localhost:8080`.*

---

### Alternative : Lancement en mode Développement (Local)

Si vous souhaitez modifier le code et voir les changements en direct :

1. **Lancer uniquement la DB et le Backend :**
```bash
docker compose up db backend --build
```
*La base de données s'initialise avec `data.sql` : création des tables, des jeux modèles et de 2 utilisateurs par défaut.*

2. **Lancer le Frontend en mode Dev :**
Ouvrez un nouveau terminal :
```bash
cd frontend
npm install
npm run dev
```
*Le frontend de développement sera accessible sur `http://localhost:5173`.*

---

## 👥 Comptes de démonstration (Local uniquement)

> [!WARNING]
> Ces identifiants sont générés par le script d'initialisation (`data.sql`) exclusivement pour tester l'application en local. Veillez à utiliser des mots de passe robustes et à modifier ces accès en environnement de production réelle.

Deux comptes sont créés automatiquement au démarrage pour tester l'application :

*   **👑 Compte Admin** (Création, édition, démarrage, arrêt et destruction) :
    *   Utilisateur : `quentin`
    *   Mot de passe : `password`
*   **👁️ Compte Visiteur** (Lecture seule, accès au dashboard de surveillance) :
    *   Utilisateur : `thomas`
    *   Mot de passe : `password`

---

## 📸 Aperçu de l'Architecture

- **Services Backend isolés** : Le code est segmenté entre Controleurs (REST API), Services (Logique métier) et Repositories (Accès DB).
- **Communication Asynchrone Frontend** : Utilisation intensive de chaînes de Promesses ES6, `async/await`, pour assurer une interface qui ne fige jamais lors des lancements d'instances de jeux.
- **Réduction du couplage UI** : Séparation stricte des composants (`ServerCard.vue`, `DashboardHeader.vue`) alimentés par un gestionnaire de service unique (`server.service.js`).

---

## 📜 Licence

Ce projet est sous licence **MIT**. Voir le fichier [LICENSE](./LICENSE) pour plus de détails.

---

## 👨‍💻 Auteur

Développé par [Quentin](https://github.com/Quentin).
*Ce projet est une vitrine de mon savoir-faire technique Fullstack : de l'architecture d'une base de données relationnelle à la manipulation réactive du DOM, en passant par la conception d'APIs sécurisées.*
