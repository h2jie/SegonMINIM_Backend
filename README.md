# Backend segon MINIM DSA

Backend del segundo minimo de DSA

### API REST

Para ver las llamadas del API disponibles, ejecutar el proyecto y entrar a http://localhost:8080/swagger/

## Funciones implementadas

### FAQService
- GET /faqs
  - Interfaz para obtener una lista de todas las FAQs
  - Devuelve una lista de FAQs en formato JSON
  - Contiene las 10 FAQs por defecto.

- POST /faqs
  - Interfaz para añadir una nueva FAQ
  - Recibe los datos de la FAQ en formato JSON.
  - Valida los datos en el lado del servidor
  - Devuelve el código de estado 201 después de una adición exitosa

### Modelo FAQdata
- Contiene los siguientes campos
  - fecha
  - pregunta
  - respuesta
  - remitente

## Instrucciones de uso
Como en nuestro proyecto ya hemos implementado la función de datos de usuarios y artículos en la base de datos, necesitamos añadirla a la base de datos para que los usuarios puedan iniciar sesión normalmente.

Puedes crear una base de datos utilizando los archivos de la carpeta DB
