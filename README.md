# API_TFG
> Autores: Alejandro del Valle Vallés, Aurora Pinar del Hoyo, Carolina Lobato Ruiz

Este proyecto corresponde a la API elaborada para el proyecto fin de grado del curso de DAM 2 del año 2025-2026.

## Versión del Proyecto:
Versión: **1.8.1**

## Herramientas
- Java 17.0.18
- Spring Boot 4.0.3
- Spring Data JPA
- PostgreSQL

### Como hacer uso de la API

Para poder hacer uso de la API es necesario definir en la raiz del proyecto un fichero .env con la URL de conexión a la BBDD,
un usuario y una contraseña, así como el driver a usar (Debe ser acorde al tipo de BBDD a usar), el dialecto, puerto, etc.<br>
También se debe definir una clave secreta para la encriptación con JWT y el tiempo que dura una sesión abierta una vez se inicia sesión.
También es necesario un correo electrónico que permita ser usado para servicio smtp para el envio de correos con la info de la compra.
Puedes consultar el fichero `.env.exmaple` para usarlo de referencia en el rellenado de estos datos.
Por defecto, la api funciona en el puerto `8443` con la raíz `/api`.
Puedes consultar la documentación hecha con swagger en cualquier navegador pulsando en el siguiente enlace (Configurado para el puerto 9090 con la raíz /api) [Documentación de Swagger](http://localhost:9090/api/swagger-ui/index.html).<br>
Esta ruta debes modificarla en base al puerto que decidas usar o la raíz de la api.
