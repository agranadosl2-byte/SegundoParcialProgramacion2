CREATE DATABASE IF NOT EXISTS empresa_db;
USE empresa_db;
 -- Creamos la tabla principal 
 CREATE TABLE empleados(
 -- El sistema lo asigna y no se repite
 id INT AUTO_INCREMENT PRIMARY KEY,
 nombre_completo VARCHAR(100) NOT NULL,
 departamento VARCHAR(50) NOT NULL, 
 -- DECIMAL(10,2) ES IDEAL PARA DINERO
 -- Significa: 10 dígitos en total, de los cuales 2 son centavos (ej: 12345678.90).
 salario_mensual DECIMAL(10,2) NOT NULL,
 -- DATE guarda el año-mes-dia
 fecha_contratacion DATE NOT NULL,
 -- BOOLEAN en MySQL se guarda como TINYINT(1).
 -- DEFAULT TRUE significa que al hacer un INSERT sin este campo, entrará como activo (1).
 activo BOOLEAN DEFAULT TRUE
 );
