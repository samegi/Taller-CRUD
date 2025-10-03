# Taller-CRUD
 Implementación de un CRUD para un "Zoo de Mascotas Fantásticas" en Spring Boot 

Ver todas las zonas:
Invoke-RestMethod -Uri "http://localhost:8080/api/zones" -Method GET

Crear una zona (solo si falta):
$zoneBody = @{
  name = "Zona Voladores"
  description = "Espacio para criaturas magicas"
  capacity = 10
} | ConvertTo-Json -Compress

$zone = Invoke-RestMethod -Uri "http://localhost:8080/api/zones" `
-Method POST `
-Headers @{ "Content-Type" = "application/json; charset=utf-8" } `
-Body $zoneBody

$zoneId = $zone.id
$zone

Nueva criatura valida → se persiste:
$creatureBody = @{
  name = "Fenix"
  species = "Ave magica"
  size = 2.5
  dangerLevel = 5
  healthStatus = "healthy"
  zone = @{ id = $zoneId }
} | ConvertTo-Json -Compress -Depth 3

$creature = Invoke-RestMethod -Uri "http://localhost:8080/api/creatures" `
-Method POST `
-Headers @{ "Content-Type" = "application/json; charset=utf-8" } `
-Body $creatureBody

$creatureId = $creature.id
$creature

Obtener lista de todas las criaturas y ver detalles por ID”.

3A. Lista
Invoke-RestMethod -Uri "http://localhost:8080/api/creatures" -Method GET

3B. Detalle
Invoke-RestMethod -Uri "http://localhost:8080/api/creatures/3" -Method GET

4. Actualizacion reflejada en BD (campos validos)

$updateBody = @{
  name = "Fenix Dorado"
  species = "Ave magica"
  size = 3.0
  dangerLevel = 6
  healthStatus = "healthy"
  zone = @{ id = 1 }
} | ConvertTo-Json -Compress -Depth 3

Invoke-RestMethod -Uri "http://localhost:8080/api/creatures/$creatureId" `
-Method PUT `
-Headers @{ "Content-Type" = "application/json; charset=utf-8" } `
-Body $updateBody
