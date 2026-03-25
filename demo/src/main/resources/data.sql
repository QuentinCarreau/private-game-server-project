-- data.sql : seed data uniquement (hors utilisateurs, gérés par DataInitializer).
-- Hibernate (ddl-auto=create) recrée les tables avant ce fichier.

-- ========== GAME TEMPLATES ==========
INSERT INTO games_template (id, game_name, game_info, game_script, game_max_players, game_cpu_usage, game_ram_usage, game_storage_usage)
VALUES (1, 'Minecraft', 'Le jeu de construction culte.', 'minecraft_start.sh', 10, 2, 4, 10);

INSERT INTO games_template (id, game_name, game_info, game_script, game_max_players, game_cpu_usage, game_ram_usage, game_storage_usage)
VALUES (2, 'Satisfactory', 'Construisez des usines sur une planète alien.', 'satisfactory_start.sh', 4, 4, 8, 25);

INSERT INTO games_template (id, game_name, game_info, game_script, game_max_players, game_cpu_usage, game_ram_usage, game_storage_usage)
VALUES (3, 'Valheim', 'Survie dans l''au-delà Viking.', 'valheim_start.sh', 10, 2, 4, 10);

INSERT INTO games_template (id, game_name, game_info, game_script, game_max_players, game_cpu_usage, game_ram_usage, game_storage_usage)
VALUES (4, 'Project Zomboid', 'Simulation de survie en apocalypse zombie.', 'zomboid_start.sh', 32, 4, 8, 20);

-- ========== HOST MACHINE ==========
INSERT INTO host_machine (id, host_machine_name, host_machine_status, host_machine_ip, host_machine_port, host_machine_current_game_server_nb, host_machine_cpu_max, host_machine_ram_max, host_machine_storage_max)
VALUES (1, 'Machine Alpha', 'OFFLINE', '127.0.0.1', 8080, 0, 16, 32, 500);

-- ========== RESET DES SÉQUENCES ==========
-- Indispensable : quand on insère des IDs explicites, la séquence PostgreSQL
-- ne s'incrémente pas automatiquement. Sans ce reset, le prochain INSERT
-- auto-généré obtiendrait id=1 et provoquerait un conflit de clé dupliquée.
SELECT setval(pg_get_serial_sequence('games_template', 'id'), (SELECT MAX(id) FROM games_template));
SELECT setval(pg_get_serial_sequence('host_machine', 'id'), (SELECT MAX(id) FROM host_machine));

-- Note : les utilisateurs et les game_servers ne sont PAS insérés ici.
-- - Les utilisateurs sont créés par DataInitializer.java (avec hashage BCrypt).
-- - Les game_servers seront créés via l'interface utilisateur.
