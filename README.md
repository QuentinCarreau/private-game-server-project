# GamePulse - Game Server Launcher 🎮

GamePulse est une plateforme de gestion et d'administration de serveurs de jeu en ligne (Game Server Launcher). Elle permet aux utilisateurs de créer, gérer et surveiller leurs instances de serveurs de jeu à travers une interface web moderne et une API backend robuste.

## 🚀 Fonctionnalités Principales

*   **🕹️ Déploiement à la demande** : Lancez des instances de serveurs (Minecraft, Satisfactory, etc.) basées sur des modèles prédéfinis (`GameTemplate`).
*   **⚙️ Administration des instances** :
    *   Gestion du cycle de vie : Démarrage, Arrêt, Redémarrage.
    *   Paramétrage : Nom de l'instance, nombre de joueurs max, niveau de confidentialité (Public/Privé) et gestion du mot de passe.
    *   Destruction définitive des instances.
*   **📊 Supervision en Temps Réel** : Un dashboard interactif affichant l'état des instances (ONLINE, OFFLINE, STARTING) et l'allocation des ressources (CPU, RAM, Stockage). La consommation évolue dynamiquement à l'aide d'un moteur de simulation intégré.
*   **🔒 Sécurité & Authentification** : Inscription et authentification des utilisateurs via **Spring Security** et des Jetons **JWT** (JSON Web Tokens). Chaque instance appartient à l'utilisateur qui l'a créée, garantissant la stricte confidentialité et sécurité des données.
*   **✨ Interface Premium (Glassmorphism)** : Frontend pensé pour offrir une expérience utilisateur haut-de-gamme, fluide, avec un thème sombre et des couleurs néon inspirées de l'univers gaming.

---

## 🛠️ Stack Technique

### Backend (Java / Spring Boot)
*   **Framework** : Spring Boot 3.x
*   **Langage** : Java 21
*   **Sécurité** : Spring Security & JWT
*   **Persistance** : Spring Data JPA (Hibernate)
*   **Base de Données** : PostgreSQL
*   **Build Tool** : Maven

### Frontend (Vue.js)
*   **Framework** : Vue.js 3 (Composition API)
*   **Build Tool** : Vite
*   **Routage** : Vue Router
*   **Style** : CSS Vanilla (Glassmorphism design system)
*   **Appels API** : Axios avec Intercepteurs (Authentication JWT Token Bearer)

### Infrastructure
*   **Docker** : Conteneurisation de la DB PostgreSQL (déjà prête) et du Backend via `docker-compose`.

---

## 🏃 Comment démarrer le projet en local

### Prérequis
*   [Docker](https://www.docker.com/) & [Docker Compose](https://docs.docker.com/compose/)
*   [Node.js](https://nodejs.org/) (Version 18 ou +, optionnel si vous encapsulez tout dans Docker)
*   [Java 21](https://adoptium.net/) (Pour exécuter le backend hors Docker si désiré)

### Étape 1 : Récupérer le projet

```bash
git clone https://github.com/votre-nom/private-game-server-project.git
cd private-game-server-project
```

### Étape 2 : Lancer la Base de données et le Backend

Un fichier `docker-compose.yml` est présent à la racine pour vous simplifier la vie. Il lancera à la fois PostgreSQL et l'application Spring Boot :

```bash
docker compose up --build -d
```

*Le backend sera accessible sur `http://localhost:8080/api`.*
*Par chance, un script de données (`data.sql`) pré-peuplera automatiquement votre base avec quelques utilisateurs par défaut et les `GameTemplates` de base.*

### Étape 3 : Lancer le Frontend

Ouvrez un nouveau terminal, accédez au dossier frontend, installez les dépendances et lancez le serveur de développement :

```bash
cd frontend
npm install
npm run dev
```

*Le frontend sera accessible sur `http://localhost:5173`.*

---

## 📸 Aperçu de l'Application

*(Vous pourrez intégrer ici des captures d'écran de l'application, par exemple de votre magnifique Dashboard, la page de création ou encore l'écran de Login)*

---

## 🗺️ Roadmap (Ce qu'il reste à faire)

Bien que le projet soit aujourd'hui pleinement fonctionnel comme démonstrateur, certaines évolutions futures pourraient inclure :

- [ ] L'intégration avec l'API Docker côté Backend (ProcessBuilder ou Librairie Docker Java) pour **vraiment** poper des conteneurs de serveurs (actuellement, la création/les ressources sont simulées).
- [ ] Transition de l'API REST des métriques ressources (polling) vers une vraie connexion en **WebSockets** (STOMP).
- [ ] Gérer l'export et l'import de logs de serveurs via le frontend.

---

## 👨‍💻 Auteur

Développé par [Quentin](https://github.com/Quentin).
*Ce projet est une vitrine illustrant l'implémentation Fullstack d'un Dashboard d'administration.*
