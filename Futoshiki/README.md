# Juego Futoshiki

## Descripción
Futoshiki es un juego de rompecabezas lógico donde los jugadores deben llenar una cuadrícula con números mientras siguen restricciones de desigualdad entre celdas adyacentes. El juego se originó en Japón y fue desarrollado por Tamaki Seto en 2001.

## Estructura del Proyecto
El proyecto consta de las siguientes partes:

src/
├── main/
    ├── java/
        ├── futoshiki/          # Paquete principal de la aplicación
        ├── controller/         # Contiene controladores para el patrón MVC
        │   ├── config/         # Controladores de configuración
        │   ├── game/          # Controladores de lógica del juego
        │   └── top10/         # Controladores de tabla de puntuaciones
        ├── model/             # Modelos de datos y lógica de negocio
        │   ├── config/        # Modelos de configuración
        │   ├── game/          # Modelos de estado y tablero del juego
        │   └── player/        # Modelos de gestión de jugadores
        ├── view/              # Componentes de interfaz de usuario
        │   ├── components/    # Componentes UI reutilizables
        │   ├── dialogs/       # Ventanas de diálogo
        │   └── game/         # Componentes principales de la interfaz del juego
        └── util/              # Clases utilitarias
            └── constants/     # Constantes y enumeraciones

## Ejecución
Para ejecutar el juego, se debe compilar el proyecto y ejecutar el archivo Main.java.

### Para compilar
```	
javac -d target/classes src/main/java/futoshiki/Main.java
```

### Para ejecutar
```
java -cp target/classes futoshiki.Main
```