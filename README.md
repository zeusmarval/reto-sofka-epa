# Reto diagnóstico: Ferretería EPA

## Table of contents
1. [Database](#Database)
2. [Swagger](#swagger)
3. [URLs Locales](#urls-locales)
4. [Explicación de Parámetros](#explicación-de-parámetros)

## Database

En la configuración de la base de datos, se establecen los siguientes parámetros para la conexión con MongoDB:

- `spring.data.mongodb.host=127.0.0.1`: Especifica la dirección IP del servidor de MongoDB al que se conectará la aplicación.
- `spring.data.mongodb.port=27017`: Indica el puerto en el que el servidor de MongoDB está escuchando para conexiones.
- `spring.data.mongodb.database=facturacion_ferreteria_epa`: Define el nombre de la base de datos que se utilizará para la facturación de la ferretería EPA.
- `spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration`: Excluye la configuración automática del origen de datos JDBC, lo que indica que no se utilizará una base de datos relacional a través de JDBC en esta configuración.


## Swagger
- URL: `http://localhost:8080/swagger-ui/index.html`
- Descripción: Documentación en Swagger

## URLs Locales

- **allSelled:**
    - URL: `localhost:8080/product/all-selled`
    - Descripción: Esta solicitud obtiene todos los productos vendidos.

- **most-selled-products:**
    - URL: `localhost:8080/product/most-selled-products?startDate=2011-01-01&endDate=2015-12-31&limit=10`
    - Descripción: Esta solicitud obtiene los productos más vendidos dentro de un rango de fechas y con un límite de cantidad.

- **get-invoices:**
    - URL: `localhost:8080/sales/get-invoices?page=0&size=100`
    - Descripción: Esta solicitud obtiene las facturas con paginación, mostrando 100 resultados por página.

- **sales:**
    - URL: `localhost:8080/sales`
    - Descripción: Esta solicitud registra una nueva venta con la información proporcionada en el cuerpo de la solicitud.

## Explicación de Parámetros

- **most-selled-products:**
    - URL: `localhost:8080/product/most-selled-products?startDate=2011-01-01&endDate=2015-12-31&limit=10`
    - Descripción: Esta solicitud obtiene los productos más vendidos dentro de un rango de fechas y con un límite de cantidad.
    - Parámetros:
        - `startDate`: Fecha de inicio de la búsqueda.
        - `endDate`: Fecha de fin de la búsqueda.
        - `limit`: Cantidad máxima de productos a mostrar.

- **get-invoices:**
    - URL: `localhost:8080/sales/get-invoices?page=0&size=100`
    - Descripción: Esta solicitud obtiene las facturas con paginación, mostrando 100 resultados por página.
    - Parámetros:
        - `page`: Número de página para la paginación.
        - `size`: Cantidad de resultados por página.

- **sales:**
    - URL: `localhost:8080/sales`
    - Descripción: Esta solicitud registra una nueva venta con la información proporcionada en el cuerpo de la solicitud.
    - Parámetros en el cuerpo de la solicitud:
        - `items`: Lista de productos vendidos.
        - `storeLocation`: Ubicación de la tienda.
        - `customer`: Información del cliente.
        - `couponUsed`: Indica si se utilizó un cupón en la compra.
        - `purchaseMethod`: Método de compra (por ejemplo, "Online" o "In-store").
