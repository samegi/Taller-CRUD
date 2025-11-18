# Taller-CRUD

### Pasos para hacer las pruebas con JMeter
## Configurar un "Thread Group"
Define cuantos usuarios virtuales van a enviar solicitudes al endpoint de la API REST

Se configuraron 10, 50, 100 usuarios simulados que envian solicitudes de creacion de criaturas.

## HTTP Request Defaults
Protocol: http
IP: localhost
Port Number: 8080

## HTTP Request
POST
Path: /api/creatures
Body data:
{
  "name": "Dragon Escarlata",
  "species": "Dragón",
  "size": 12.5,
  "dangerLevel": 9,
  "healthStatus": "Herido"
}

## HTTP Header Manager
Content-Type | application/json

Una vez configurado JMeter, desde la terminal utilizar el comando 
## mvn spring-boot:run

Y correr JMeter

