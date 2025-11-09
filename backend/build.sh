#!/bin/bash
# Script de build para Render

echo "Iniciando build de tienda_libro backend..."

# Dar permisos de ejecución al Maven Wrapper
chmod +x mvnw

# Limpiar y compilar el proyecto (saltar tests para build más rápido)
./mvnw clean install -DskipTests

echo "Build completado exitosamente!"
