DROP TABLE IF EXISTS game_servers;
DROP TABLE IF EXISTS games_template;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS host_machine;

CREATE TABLE host_machine(
    id INT PRIMARY KEY,
    host_machine_name VARCHAR(255),
    host_machine_status VARCHAR(255),
    host_machine_ip VARCHAR(255),
    host_machine_port INT,
    host_machine_current_game_server_nb INT,
    host_machine_CPU_max INT,
    host_machine_RAM_max INT,
    host_machine_storage_max INT
);

CREATE TABLE games_template (
    id INT PRIMARY KEY,
    game_name VARCHAR(255),
    game_info VARCHAR(255),
    game_script VARCHAR(255),
    game_max_players INT,
    game_CPU_usage INT,
    game_RAM_usage INT,
    game_storage_usage INT
);

CREATE TABLE game_servers (
    id INT PRIMARY KEY,
    game_server_name VARCHAR(255),
    game_server_status VARCHAR(255),
    game_server_privacy VARCHAR(255),
    ip_adress VARCHAR(255),
    port INT,
    game_server_pwd VARCHAR(255),
    saved_world_path VARCHAR(255),
    active_players INT,
    user_id INT FOREIGN KEY REFERENCES registred_users(id),
    game_template_id INT FOREIGN KEY REFERENCES games_template(id),
    current_players_nb INT,
    max_players_slot INT
);

CREATE TABLE registred_users (
    id INT PRIMARY KEY,
    user_name VARCHAR(255),
    user_password VARCHAR(255),
    user_role VARCHAR(255)
);

INSERT INTO games_template (id, game_name, game_info, game_script, game_max_players, game_CPU_usage, game_RAM_usage, game_storage_usage) VALUES (1, 'Minecraft', 'Le jeu de construction culte.', 'script.sh', 10, 2, 4, 10);
INSERT INTO games_template (id, game_name, game_info, game_script, game_max_players, game_CPU_usage, game_RAM_usage, game_storage_usage) VALUES (2, 'Satisfactory', 'Construisez des usines sur une planète alien.', 'script.sh', 10, 2, 4, 10);
INSERT INTO games_template (id, game_name, game_info, game_script, game_max_players, game_CPU_usage, game_RAM_usage, game_storage_usage) VALUES (3, 'Valheim', 'Survie dans l''au-delà Viking.', 'script.sh', 10, 2, 4, 10);
INSERT INTO games_template (id, game_name, game_info, game_script, game_max_players, game_CPU_usage, game_RAM_usage, game_storage_usage) VALUES (4, 'Project Zomboid', 'Simulation de survie en apocalypse zombie.', 'script.sh', 10, 2, 4, 10);

INSERT INTO game_servers (id, game_server_name, game_server_status, game_server_privacy, ip_adress, port, game_server_pwd, saved_world_path, user_id, game_template_id, current_players_nb, max_players_slot) VALUES (1, 'Serveur Alpha', 'OFFLINE', 'PUBLIC', '[IP_ADDRESS]', 25565, 'password', 'path/to/world', 0, 1, 1, 0, 10);
INSERT INTO game_servers (id, game_server_name, game_server_status, game_server_privacy, ip_adress, port, game_server_pwd, saved_world_path, user_id, game_template_id, current_players_nb, max_players_slot) VALUES (2, 'Serveur Beta', 'OFFLINE', 'PRIVATE', '[IP_ADDRESS]', 25565, 'password', 'path/to/world', 0, 1, 2, 0, 10);
INSERT INTO game_servers (id, game_server_name, game_server_status, game_server_privacy, ip_adress, port, game_server_pwd, saved_world_path, user_id, game_template_id, current_players_nb, max_players_slot) VALUES (3, 'Serveur Gamma', 'OFFLINE', 'PRIVATE', '[IP_ADDRESS]', 25565, 'password', 'path/to/world', 0, 1, 3, 0, 10);

INSERT INTO registred_users(id, user_name, user_password, user_role) VALUES (1, 'admin', 'admin', 'ADMIN');
INSERT INTO registred_users(id, user_name, user_password, user_role) VALUES (2, 'user', 'user', 'USER');

INSERT INTO host_machine(id, host_machine_name, host_machine_status, host_machine_ip, host_machine_port, host_machine_current_game_server_nb, host_machine_CPU_max, host_machine_RAM_max, host_machine_storage_max) VALUES (1, 'Machine Alpha', 'OFFLINE', '[IP_ADDRESS]', 3, 1, 16, 32, 500);
-- Default user: user / password (will need hashing later)
-- For now, Hibernate will create the tables. 
-- We'll add a default user via a CommandLineRunner for easier management of hashing if needed.
