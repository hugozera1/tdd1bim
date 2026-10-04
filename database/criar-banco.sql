-- Execute como administrador no PostgreSQL (psql), fora de transacao.
CREATE USER todo WITH PASSWORD 'todo';
CREATE DATABASE todo OWNER todo;
-- A tabela e criada por src/main/resources/schema.sql ao iniciar a API.
