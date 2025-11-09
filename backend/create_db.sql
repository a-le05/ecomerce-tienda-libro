-- Script para crear la base de datos
-- Ejecutar: psql -U postgres -f create_db.sql

CREATE DATABASE tienda_libro
    WITH
    TEMPLATE = template0
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'es-MX'
    LC_CTYPE = 'es-MX'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Después de crear la BD, inicia el backend de Spring Boot
-- Hibernate creará automáticamente todas las tablas
