UPDATE eh_service_module_privileges
SET module_id = 20810
WHERE module_id = 20811;


UPDATE eh_service_modules
SET STATUS =2
WHERE id = 20840;
UPDATE eh_service_modules
SET LEVEL =4
WHERE id = 20841;

UPDATE eh_service_modules
SET STATUS =2
WHERE id = 20841;