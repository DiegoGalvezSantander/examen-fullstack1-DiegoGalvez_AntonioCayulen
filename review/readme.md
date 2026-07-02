# Sistema de Gestión de Destinos Turísticos y Reseñas

Este proyecto consiste en una arquitectura de microservicios desarrollada con Java 21 y Spring Boot 4.0.7, diseñada para gestionar destinos turísticos y sus respectivas reseñas bajo un entorno protegido por autenticación centralizada.

## Arquitectura del Sistema

La solución se compone de los siguientes módulos distribuidos:

* **Eureka Server (Discovery Service):** Servidor de descubrimiento donde se registran de forma dinámica todos los microservicios de la arquitectura.
* **Gateway Service:** Punto de entrada único para las peticiones externas, encargado del enrutamiento inteligente.
* **Login Service (User Service):** Microservicio que gestiona el registro, autenticación de usuarios y la validación de tokens JWT.
* **Destination Service:** Microservicio encargado de mantener el catálogo y disponibilidad de los destinos turísticos.
* **Review Service:** Microservicio que registra las opiniones de los usuarios, consumiendo de forma síncrona los servicios de Login y Destination para validar la consistencia de los datos.

---

## Mapa de Puertos y Servicios

| Microservicio | Puerto por Defecto | Responsabilidad Primaria |
| :--- | :--- | :--- |
| `eureka-server` | `8761` | Servidor de Descubrimiento de Servicios |
| `gateway-service` | `8080` | Enrutador y API Gateway |
| `login-service` | `9001` | Autenticación y Emisión/Validación JWT |
| `destination-service`| `9002` | Mantenimiento de Destinos Turísticos |
| `review-service` | `9003` | CRUD de Reseñas y Comentarios |

---

## Requisitos Previos

* Java Development Kit (JDK) 21
* Apache Maven 3.9 o superior
* Servidor MySQL ejecutándose (Puerto local o contenedor dedicado. Se recomienda aprovechar el docker-compose.yml)

---

## Configuración de Bases de Datos

Cada microservicio operativo cuenta con su propio esquema independiente administrado mediante Flyway (`ddl-auto: none`). Asegúrese de tener creadas las siguientes bases de datos en su instancia de MySQL (Puerto 3313) antes de iniciar los servicios:

1. **Esquema de Login:** `jdbc:mysql://localhost:3313/login`
2. **Esquema de Destination:** `jdbc:mysql://localhost:3313/destination`
3. **Esquema de Review:** `jdbc:mysql://localhost:3313/review`

*Recomendacion adicional: Las credenciales de acceso (usuario y contraseña) deberian ser configuradas localmente en el archivo de propiedades o variables de entorno de cada microservicio antes de su despliegue.*

## Orden de Encendido del Ecosistema

Para garantizar la correcta inicialización y resolución de nombres lógicos entre los servicios, siga estrictamente este orden de despliegue:

1. **`eureka-server`:** Espere a que la interfaz en `http://localhost:8761` esté completamente activa.
2. **`login-service`:** Permite que se registre en Eureka antes de levantar los servicios dependientes.
3. **`destination-service`** y **`review-service`:** Pueden iniciarse en paralelo una vez que el servidor de descubrimiento esté operativo.
4. **`gateway-service`:** Inicializar al final para asegurar el correcto mapeo de rutas hacia los servicios disponibles en el panel de Eureka.

---

## Comunicación Inter-Servicio (Flujo de Reseñas)

El microservicio de reseñas (`review-service`) realiza llamadas síncronas de tipo bloqueo utilizando `WebClient` configurado con `@LoadBalanced` hacia las siguientes rutas internas expuestas en el servidor Eureka:

* **Validación de Token:** Consulta a `http://login/api/v1/users/validate?token=` enviando la cadena JWT recibida en la cabecera.
* **Validación de Existencia:** Consulta a `http://destination/api/v1/destination/destinations/exists?id=` enviando el identificador del destino turístico antes de guardar la reseña.

---

## Acceso a la Documentación (Swagger)

Cada microservicio expone de forma individual su documentación autogenerada bajo la especificación OpenAPI v3. Puede probar e interactuar con los endpoints de manera aislada ingresando a:

* **Swagger Review Service:** `http://localhost:9003/swagger-ui/index.html`
* **Swagger Destination Service:** `http://localhost:9002/swagger-ui/index.html`
* **Swagger Login Service:** `http://localhost:9001/swagger-ui/index.html`