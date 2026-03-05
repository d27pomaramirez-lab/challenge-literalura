# 📚 LiterAlura - Catálogo de Libros

LiterAlura es una aplicación de consola desarrollada en Java con **Spring Boot** que permite a los usuarios buscar libros a través de la API pública de [Gutendex](https://gutendex.com/), procesar la información y almacenarla en una base de datos relacional **PostgreSQL**. 

Este proyecto aplica principios avanzados de Programación Orientada a Objetos (POO), consumo de APIs REST, mapeo objeto-relacional (ORM) y consultas derivadas (Derived Queries) para gestionar y consultar un catálogo literario de forma eficiente.

## 🚀 Funcionalidades (Menú Interactivo)

El sistema ofrece una interfaz de línea de comandos (CLI) con las siguientes opciones:

1. **Buscar libro por título:** Consume la API de Gutendex, formatea el JSON y guarda el libro junto con su autor en la base de datos (evitando duplicados).
2. **Listar libros registrados:** Muestra todos los libros almacenados en la base de datos local.
3. **Listar autores registrados:** Despliega los autores guardados y la lista de libros asociados a cada uno de ellos.
4. **Listar autores vivos en un determinado año:** Utiliza *Derived Queries* de Spring Data JPA para filtrar autores según su año de nacimiento y fallecimiento.
5. **Listar libros por idioma:** Permite buscar dentro de la base de datos local los libros categorizados por su idioma original (ej. Español, Inglés, etc.).
6. **Top 10 libros más descargados:** Consulta que ordena y limita los resultados para mostrar los libros más populares registrados.

## 🛠️ Tecnologías y Arquitectura

* **Lenguaje:** Java 17 (o superior)
* **Framework:** Spring Boot (vía `CommandLineRunner`)
* **Persistencia de Datos:** Spring Data JPA / Hibernate
* **Base de Datos:** PostgreSQL
* **Cliente HTTP:** `java.net.http.HttpClient` (Nativo)
* **Procesamiento JSON:** Librería **Jackson** (uso de `Records`, `@JsonAlias` y `@JsonIgnoreProperties`)

### 🧠 Decisiones de Diseño Destacadas

* **Separación de Responsabilidades (SoC):** La lógica de consumo de API (`ConsumoAPI`) está totalmente aislada de la lógica de deserialización JSON (`ConvierteDatos`), empleando interfaces genéricas para máxima reutilización.
* **Manejo de Datos Faltantes (Null Object Pattern):** Se implementó un flujo específico para manejar libros de la API que no poseen autor registrado, asignándolos automáticamente a un registro unificado "Anónimo" en la base de datos. Esto protege la integridad referencial y facilita las consultas.
* **Seguridad:** Las credenciales de la base de datos están protegidas mediante el uso de **Variables de Entorno**.

## ⚙️ Configuración y Ejecución

1. Clona este repositorio en tu máquina local:
   ```bash
   git clone [https://github.com/tu-usuario/literalura.git](https://github.com/tu-usuario/literalura.git)

2. Asegúrate de tener PostgreSQL instalado y ejecutándose.
3. Crea una base de datos vacía (por ejemplo, literalura).
4. Configura las siguientes Variables de Entorno en tu IDE o sistema operativo:
  * DB_NAMES: Nombre de tu base de datos.
  * DB_USER: Tu usuario de PostgreSQL.
  * DB_PASSWORD: Tu contraseña de PostgreSQL.
5. Ejecuta la clase principal LiteraluraApplication.java.
6. Spring Boot creará las tablas automáticamente y el menú interactivo aparecerá en tu consola

### Tecnologías utilizadas
<img width="40" height="40" alt="intellij" src="https://github.com/user-attachments/assets/b0894a96-b4c2-46b9-96f9-407a3fd1caee" /> <img width="40" height="40" alt="java" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSJEYp53vIyZaY3wclh9alfY8bK1UxUTFJ6XA&s" /> <img width="80" height="40" alt="java" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRFI6sUZcQuM50ahwUCFQCOZFoNLhFXEWRpdg&s"/>

### Desarrollador

| [<img src="https://avatars.githubusercontent.com/u/224181779?s=400&u=b542509272eef999a81a70ad84b7084ea4ab8740&v=4" width=115><br><sub>Poma Ramirez Diego</sub>](https://github.com/d27pomaramirez-lab) 
