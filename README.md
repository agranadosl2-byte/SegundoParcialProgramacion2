# Gestión de Empleados - CRUD Swing + Maven

Proyecto individual que implementa un sistema CRUD para la gestión de empleados (Variante A), desarrollado con arquitectura multi-módulo en Maven y una interfaz gráfica en Java Swing.

## Arquitectura del Proyecto

El proyecto aplica el principio de separación de responsabilidades dividiéndose en dos módulos reales conectados por un POM padre:

* **`proyecto-empleados-core`**: Módulo librería (`.jar`) que contiene el Modelo (`Empleado`) y el Data Access Object (`EmpleadoDAO`). Gestiona la persistencia en MySQL usando JDBC y `PreparedStatement` para prevenir inyecciones SQL. Está completamente aislado de la interfaz gráfica.
* **`proyecto-empleados-ui`**: Módulo de aplicación (`.jar`) que consume al `core` como dependencia. Contiene la interfaz gráfica (`JFrame`, `JTable`) y ejecuta las validaciones de negocio antes de interactuar con la base de datos.

## Requisitos del Sistema
* JDK 11 o superior.
* Apache Maven.
* MySQL / MariaDB ejecutándose localmente.

## Instalación y Ejecución

1. **Base de Datos:** Ejecuta el script SQL ubicado en `proyecto-empleados-ui/sql/schema.sql` en tu servidor de base de datos para crear la tabla y estructura necesarias.
2. **Instalación de dependencias:** Compila e instala el módulo core en el repositorio local de Maven ejecutando:3. **Ejecución:** Ejecuta la clase principal `MainUI.java` ubicada en el paquete `edu.umg.programacion2.proyecto` dentro del módulo `proyecto-empleados-ui`.

## Reglas de Negocio y Validaciones

La interfaz gráfica previene envíos inválidos a la base de datos implementando las siguientes reglas:
* El nombre completo y el departamento son obligatorios y no pueden ser espacios en blanco.
* El salario mensual es validado estrictamente como un número mayor a cero (no se aceptan negativos ni texto).
* La fecha de contratación se valida contra el reloj del sistema para impedir fechas futuras.
* La acción de eliminar un empleado realiza un borrado físico de la fila en la base de datos y exige una confirmación previa mediante un cuadro de diálogo para prevenir accidentes.
* Los errores de SQL son capturados y mostrados al usuario mediante cuadros de diálogo amigables, previniendo el colapso (crash) de la aplicación.