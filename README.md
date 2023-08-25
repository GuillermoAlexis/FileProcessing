# FileProcessing

## Descripción
Este proyecto implementa una aplicación Java que proporciona una API RESTful para el procesamiento de archivos, con características de autenticación, autorización y validación.

## Entidades

### 1. **AppUser**
- Representa a un usuario de la aplicación.
- **Relaciones**:
  - Muchos-a-muchos con Role.
- **DTOs**:
  - **AppUserDTO**
    - `id`: ID del usuario.
    - `userName`: Nombre de usuario.
    - `email`: Email del usuario.
    - `password`: Contraseña del usuario.
    - `roles`: Lista de roles asignados al usuario.
    - `token`: Token de autenticación del usuario.
  - **AppUserWithoutRolesDTO**
    - `id`: ID del usuario.
    - `userName`: Nombre de usuario.
    - `email`: Email del usuario.
    - `password`: Contraseña del usuario.

### 2. **Feature**
- Características específicas que pueden estar asociadas a diferentes roles.
- **Relaciones**:
  - Muchos-a-muchos con Role.
- **DTOs**:
  - **FeatureDTO**
    - `id`: ID de la característica.
    - `name`: Nombre de la característica.

### 3. **File**
- Representa un archivo que será procesado.
- **Relaciones**:
  - Muchos-a-uno con AppUser.
- **DTOs**:
  - **FileDTO**
    - `id`: ID del archivo.
    - `fileName`: Nombre del archivo.
    - `status`: Estado del archivo.
    - `processedAt`: Fecha y hora de procesamiento.
    - `user`: Usuario que cargó el archivo.

### 4. **Role**
- Representa un rol que puede ser asignado a los usuarios.
- **Relaciones**:
  - Muchos-a-muchos con AppUser.
  - Muchos-a-muchos con Feature.
- **DTOs**:
  - **RoleDTO**
    - `id`: ID del rol.
    - `name`: Nombre del rol.
    - `features`: Lista de características asociadas al rol.

### 5. **ValidationDetail**
- Detalles específicos de validación para un archivo procesado.
- **Relaciones**:
  - Muchos-a-uno con File.
- **DTOs**:
  - **ValidationDetailDTO**
    - `id`: ID del detalle de validación.
    - `file`: Archivo asociado al detalle.
    - `lineNumber`: Número de línea en el archivo.
    - `fieldName`: Nombre del campo.
    - `errorCode`: Código de error.
    - `errorMessage`: Mensaje de error asociado.

## Repositorios

1. **AppUserRepository**
   - Permite operaciones CRUD en la entidad AppUser.
   - **Funciones personalizadas**:
     - `findByEmail`: Encuentra un usuario basado en el correo electrónico.

2. **FileRepository**
   - Permite operaciones CRUD en la entidad File.
   - **Funciones personalizadas**:
     - `countByFileName`: Cuenta cuántos archivos tienen un nombre específico.

3. **RoleRepository**
   - Permite operaciones CRUD en la entidad Role.

4. **ValidationDetailRepository**
   - Permite operaciones CRUD en la entidad ValidationDetail.

## Mappers

1. **AppUserMapper**
    - Transforma entre `AppUser` y `AppUserDTO`.
    - Utiliza `RoleMapper` para mapear roles.
    
2. **FeatureMapper**
    - Transforma entre `Feature` y `FeatureDTO`.
    - Ignora la propiedad `roles` al mapear a la entidad.

3. **FileMapper**
    - Transforma entre `File` y `FileDTO`.
    - Ignora la propiedad `user.roles` al mapear a la entidad.

4. **RoleMapper**
    - Transforma entre `Role` y `RoleDTO`.
    - Utiliza `FeatureMapper` para mapear características.
    - Ignora la propiedad `users` al mapear a la entidad.

5. **ValidationDetailMapper**
    - Transforma entre `ValidationDetail` y `ValidationDetailDTO`.
    - Utiliza `FileMapper` para mapear archivos.

## Seguridad

1. **AppUserDetails**
   - Implementa la interfaz UserDetails de Spring Security.
   - Proporciona detalles sobre un usuario, como sus roles, contraseña, nombre de usuario y otros atributos relacionados con la seguridad.
   - Convierte roles de usuarios en autoridades otorgadas para Spring Security.

2. **CustomUserDetailsService**
   - Implementa la interfaz UserDetailsService de Spring Security.
   - Proporciona una manera de cargar detalles de un usuario por su email.
   - Si no se encuentra el usuario, se lanza una excepción.

3. **JwtEntryPoint**
   - Implementa la interfaz AuthenticationEntryPoint de Spring Security.
   - Maneja errores de autenticación, por ejemplo, cuando un usuario intenta acceder a un recurso sin estar autenticado.

4. **JwtFilter**
   - Extiende OncePerRequestFilter para garantizar que se ejecute una vez por solicitud.
   - Se encarga de filtrar y validar las solicitudes entrantes, extrayendo y verificando el JWT proporcionado en el encabezado de autorización.

5. **JwtProvider**
   - Facilita la creación, validación y manejo de JWT.
   - Utiliza la librería Jwts para manejar JWT.
   - Extrae detalles como el email y la fecha de expiración del token.

6. **SecurityConfig**
   - Configura cómo deberían ser autenticadas las solicitudes al sistema.
   - Establece las rutas que no requieren autenticación.
   - Configura el filtro JWT para que se ejecute antes de que la autenticación de Spring Security se active.

## Proceso de Autenticación

1. El usuario envía una solicitud de inicio de sesión con sus credenciales.
2. Si las credenciales son correctas, se genera un JWT para el usuario y se envía de vuelta.
3. Para solicitudes futuras, el usuario debe incluir este JWT en el encabezado de autorización de su solicitud.
4. JwtFilter extrae y valida el JWT de la solicitud. Si el token es válido, se extraen los detalles del usuario y se establecen en el contexto de seguridad de Spring.
5. Si el token no es válido o ha expirado, se lanza una excepción y el usuario recibe un mensaje de error.

## Notas Importantes

- La autenticación se basa en JWT, lo que significa que el servidor no mantiene ningún estado sobre los usuarios que han iniciado sesión. Cada solicitud se valida independientemente.
- Se asume que los usuarios ya están registrados en el sistema. El registro de usuarios no es parte del alcance de esta aplicación.

## Requisitos

1. **Java**: JDK 11 o superior.
2. **Base de datos**: PostgreSQL 12 o superior.
3. **Herramientas de construcción**: Maven 3.6 o superior.

## Instalación

1. Clona el repositorio a tu máquina local.
2. Configura una base de datos PostgreSQL y actualiza `application.properties` con las credenciales de la base de datos.
3. En la raíz del proyecto, ejecuta `mvn clean install` para compilar el proyecto.
4. Ejecuta `java -jar target/fileprocessing-0.0.1-SNAPSHOT.jar` para iniciar la aplicación.
5. Accede a `http://localhost:9093/FileProcessing-ms/swagger-ui/index.html#` en tu navegador para verificar que la aplicación esté funcionando.