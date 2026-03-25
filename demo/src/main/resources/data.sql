INSERT INTO games (name, description) VALUES ('Minecraft', 'Le jeu de construction culte.');
INSERT INTO games (name, description) VALUES ('Satisfactory', 'Construisez des usines sur une planète alien.');
INSERT INTO games (name, description) VALUES ('Valheim', 'Survie dans l''au-delà Viking.');
INSERT INTO games (name, description) VALUES ('Project Zomboid', 'Simulation de survie en apocalypse zombie.');

INSERT INTO game_servers (name, status, current_players, max_players) VALUES ('Serveur Alpha', 'OFFLINE', 0, 0);
INSERT INTO game_servers (name, status, current_players, max_players) VALUES ('Serveur Beta', 'OFFLINE', 0, 0);
INSERT INTO game_servers (name, status, current_players, max_players) VALUES ('Serveur Gamma', 'OFFLINE', 0, 0);

-- Default user: user / password (will need hashing later)
-- For now, Hibernate will create the tables. 
-- We'll add a default user via a CommandLineRunner for easier management of hashing if needed.
