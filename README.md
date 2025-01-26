# CatchPokemon: Tu Pokédex Personal

![Portada de la aplicación](./screenshots/portada_app.png)

## Introducción

CatchPokemon es una aplicación Android diseñada para cualquier amante de los Pokémon que desee llevar su propia Pokédex personalizada en su bolsillo. La app permite consultar, capturar y gestionar información de Pokémon, manteniendo una lista personalizada de capturados mientras aprovechas opciones avanzadas de configuración.

## Características Principales

**CatchPokemon** ofrece una experiencia completa para los entrenadores Pokémon:

- **Autenticación:**
    - Inicia sesión de manera segura con tu cuenta de Google.
    - Mantén tu progreso sincronizado en cualquier dispositivo.

- **Pantalla de Inicio de Sesión:**
    - Opción para loguearte o registrarte.
    - Interfaz clara y sencilla para nuevos usuarios.

  

- **Pokédex:**
    - Explora una lista completa de Pokémon obtenida de la PokeAPI.
    - Visualiza detalles como el nombre, la imagen y el índice de Pokédex.
    - Captura Pokémon directamente desde la Pokédex.

  ![Pokédex](./screenshots/pokedex_screen.png)

- **Mis Pokémon:**
    - Lleva un registro de los Pokémon capturados.
    - Consulta los detalles de cada Pokémon capturado, como su tipo, peso y altura.
    - Organiza y gestiona tu colección con facilidad.

  ![Mis Pokémon](./screenshots/mis_pokemons_screen.png)

- **Ajustes:**
    - Cambia el idioma de la aplicación entre español e inglés.
    - Elimina todos los Pokémon capturados de tu lista.
    - Consulta información sobre la app en la sección "Acerca de".
    - Cierra sesión de manera segura desde cualquier dispositivo.

  ![Ajustes](./screenshots/settings_screen.png)

- **Detalles de Pokémon:**
    - Visualiza una pantalla detallada con información como el tipo, índice, peso y altura de un Pokémon capturado.

  ![Detalle de Pokémon](./screenshots/pokemon_details_screen.png)

## Tecnologías Utilizadas

La aplicación **CatchPokemon** ha sido desarrollada utilizando las siguientes herramientas y librerías:

- **Android Studio:** IDE oficial para el desarrollo de apps Android.
- **Java:** Lenguaje principal utilizado en el desarrollo.
- **Firebase:**
    - **Authentication:** Para gestionar el inicio de sesión con Google.
    - **Firestore:** Para almacenar y sincronizar los Pokémon capturados.
- **Retrofit:** Para realizar las peticiones HTTP a la PokeAPI.
- **Glide:** Para cargar y visualizar imágenes de manera eficiente.
- **RecyclerView:** Para mostrar listas de Pokémon y configuraciones de manera optimizada.
- **PokeAPI:** API REST utilizada para obtener información detallada de los Pokémon.
- **Google Sign-In:** Para autenticar a los usuarios mediante cuentas de Google.
- **PreferenceFragmentCompat:** Para gestionar y configurar los ajustes de la app.

## Instrucciones de Uso

Sigue estos pasos para instalar y ejecutar la aplicación:

1. **Clonar el Repositorio:**
   ```bash
   git clone https://github.com/tu-usuario/catchpokemon.git
   ```

2. **Abrir el Proyecto en Android Studio:**
    - Abre Android Studio.
    - Selecciona "Open an Existing Project".
    - Navega hasta la carpeta donde clonaste el repositorio y ábrelo.

3. **Configurar Firebase:**
    - Crea un proyecto en la consola de Firebase.
    - Añade una aplicación Android al proyecto de Firebase.
    - Descarga el archivo `google-services.json` y colócalo en la carpeta `app` del proyecto.
    - Habilita Firestore y Google Sign-In en la consola de Firebase.

4. **Instalar Dependencias:**
    - Android Studio debería detectar automáticamente las dependencias.
    - Si es necesario, sincroniza el proyecto con los archivos Gradle (File > Sync Project with Gradle Files).

5. **Ejecutar la Aplicación:**
    - Conecta un dispositivo Android o inicia un emulador.
    - Haz clic en el botón "Run" (icono de play verde) en Android Studio.

## Conclusiones del Desarrollador

El desarrollo de CatchPokemon ha sido una experiencia enriquecedora que me permitió:

- Integrar múltiples tecnologías (Firebase, Retrofit, Glide) para ofrecer una experiencia de usuario fluida.
- Diseñar interfaces intuitivas y adaptadas al universo Pokémon.
- Mejorar habilidades en la gestión de bases de datos en tiempo real con Firestore.
- Resolver problemas relacionados con la sincronización de datos y optimización de rendimiento.

Los mayores desafíos fueron garantizar la estabilidad de las peticiones API y la correcta integración con Firebase Authentication. Sin embargo, estas dificultades se convirtieron en aprendizajes valiosos que me ayudarán en futuros proyectos.

En resumen, CatchPokemon es una aplicación útil, divertida y diseñada para cualquier entrenador Pokémon.

---
