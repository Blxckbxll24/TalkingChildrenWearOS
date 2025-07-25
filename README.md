# TalkingChildren Wear OS

Una aplicación completa para Wear OS que replica la funcionalidad del dispositivo ESP32 TalkingChildren, permitiendo a usuarios con dificultades de comunicación reproducir mensajes de audio predefinidos y grabar mensajes personalizados desde su smartwatch.

## ✨ Características principales

### 🏠 Interfaz Principal
- **3 categorías principales**: Básico, Emociones, Necesidades
- **Diseño circular optimizado** para Wear OS
- **Navegación táctil intuitiva** con RecyclerView

### 📱 Sistema de Mensajes
- **Mensajes predefinidos** con síntesis de voz (TTS)
- **Grabación de mensajes personalizados** (formato .3gp)
- **Reproducción inmediata** de cualquier mensaje
- **Organización automática** por categorías

### 🎙️ Grabación de Audio
- **Grabador integrado** con MediaRecorder
- **Duración máxima de 10 segundos** por mensaje
- **Indicador visual** de estado de grabación
- **Timer en tiempo real** durante grabación

### 🔊 Reproducción
- **TTS en español** para mensajes predefinidos
- **MediaPlayer** para audios grabados
- **Control de volumen** del sistema
- **Feedback visual** durante reproducción

## 📋 Mensajes Predefinidos

### Categoría Básico
- "Hola, buenos días"
- "Necesito ayuda"
- "Muchas gracias"

### Categoría Emociones  
- "Me siento feliz"
- "Necesito apoyo"
- "Estoy agradecido"

### Categoría Necesidades
- "Necesito ir al baño"
- "Tengo hambre" 
- "Tengo sed"

## 🛠️ Especificaciones Técnicas

### Plataforma
- **Target**: Wear OS (API level 28+)
- **Lenguaje**: Kotlin
- **SDK mínimo**: 28
- **SDK objetivo**: 34

### Arquitectura
```
app/src/main/
├── java/com/talkingchildren/wearos/
│   ├── MainActivity.kt              # Pantalla principal con categorías
│   ├── MessagesActivity.kt          # Lista de mensajes por categoría  
│   ├── AudioRecorderActivity.kt     # Grabación de audio
│   ├── AudioManager.kt              # Gestión de archivos de audio
│   ├── adapters/
│   │   ├── CategoryAdapter.kt       # Adapter para categorías
│   │   └── MessageAdapter.kt        # Adapter para mensajes
│   └── models/
│       ├── Category.kt              # Data class categoría
│       └── Message.kt               # Data class mensaje
├── res/
│   ├── layout/                      # Layouts optimizados para Wear OS
│   ├── values/                      # Strings, colores, dimensiones
│   └── drawable/                    # Iconos y recursos gráficos
└── AndroidManifest.xml
```

### Dependencias Principales
```gradle
implementation 'androidx.wear:wear:1.3.0'
implementation 'androidx.recyclerview:recyclerview:1.3.2'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'androidx.cardview:cardview:1.0.0'
compileOnly 'com.google.android.wearable:wearable:2.9.0'
```

### Permisos
```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-feature android:name="android.hardware.type.watch" />
```

## 🎨 Diseño UI/UX

### Características del Diseño
- **Interfaz circular** adaptada a la pantalla redonda de smartwatch
- **Botones grandes** (mínimo 48dp) para fácil interacción
- **Alto contraste** para mejor legibilidad
- **Colores optimizados** para pantalla OLED
- **Feedback táctil** en todas las interacciones

### Tema de Colores
- **Fondo**: Negro (#000000) - Ahorro de batería en OLED
- **Primario**: Azul (#1976D2)
- **Acento**: Amarillo (#FFC107)
- **Superficie**: Gris oscuro (#121212)
- **Texto**: Blanco (#FFFFFF)

## 🔧 Instalación y Configuración

### Prerrequisitos
- Android Studio Arctic Fox o superior
- Android SDK 28+
- Emulador Wear OS o dispositivo físico
- Gradle 8.1+

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/Blxckbxll24/TalkingChildrenWearOS.git
cd TalkingChildrenWearOS
```

2. **Abrir en Android Studio**
- Abrir Android Studio
- Seleccionar "Open an existing project"
- Navegar a la carpeta del proyecto

3. **Configurar emulador Wear OS**
- Abrir AVD Manager
- Crear nuevo dispositivo virtual
- Seleccionar "Wear OS" como categoría
- Elegir "Wear OS Large Round" o similar

4. **Compilar y ejecutar**
```bash
./gradlew build
./gradlew installDebug
```

## 🚀 Flujo de Usuario

### Navegación Principal
1. **Inicio** → Usuario ve 3 categorías en grid
2. **Selección** → Toca categoría → Lista de mensajes
3. **Reproducción** → Toca mensaje → Audio TTS o grabado
4. **Grabación** → Toca "Grabar nuevo" → Abre grabador
5. **Guardar** → Completa grabación → Se guarda automáticamente

### Estados del Grabador
- **Idle**: Listo para grabar
- **Recording**: Grabando con timer activo
- **Recorded**: Grabación completada, puede reproducir
- **Playing**: Reproduciendo grabación

## 📁 Gestión de Archivos

### Estructura de Almacenamiento
```
/Android/data/com.talkingchildren.wearos/files/audio/
├── basic/
│   ├── recording_20240125_143022.3gp
│   └── recording_20240125_143145.3gp
├── emotions/
│   └── recording_20240125_143300.3gp
└── needs/
    └── recording_20240125_143455.3gp
```

### Formato de Audio
- **Formato**: 3GP (optimizado para voz)
- **Codec**: AMR-NB (8kHz mono)
- **Duración máxima**: 10 segundos
- **Nomenclatura**: `recording_YYYYMMDD_HHMMSS.3gp`

## 🔒 Permisos y Privacidad

### Gestión de Permisos
- **Solicitud automática** al abrir grabador
- **Verificación previa** antes de cada grabación
- **Manejo de denegación** con mensajes informativos
- **Fallback** a solo reproducción si no hay permisos

### Privacidad
- **Almacenamiento local** únicamente
- **Sin conexión a internet** requerida
- **Datos privados** en directorio de la app
- **Sin telemetría** o tracking

## 🧪 Testing

### Casos de Prueba
- [x] Compilación sin errores
- [x] Instalación en emulador Wear OS
- [x] Navegación entre pantallas
- [x] Reproducción de mensajes TTS
- [x] Grabación de audio (requiere permisos)
- [x] Reproducción de audios grabados
- [x] Persistencia de archivos
- [x] Gestión de permisos

### Comandos de Testing
```bash
# Ejecutar tests unitarios
./gradlew test

# Ejecutar tests instrumentales
./gradlew connectedAndroidTest

# Análisis de código
./gradlew lint
```

## 🤝 Contribución

### Cómo Contribuir
1. Fork del repositorio
2. Crear rama feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

### Estilo de Código
- **Kotlin**: Seguir [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- **XML**: Indentación 4 espacios
- **Comentarios**: En español para claridad del proyecto
- **Nomenclatura**: camelCase para variables, PascalCase para clases

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

## 🆘 Soporte

### Problemas Comunes

**Error de permisos de micrófono**
- Verificar que los permisos estén declarados en AndroidManifest.xml
- Solicitar permisos manualmente desde Configuración → Apps → TalkingChildren

**Audio no se reproduce**
- Verificar volumen del dispositivo
- Comprobar que el archivo existe en el directorio correcto
- Reiniciar la aplicación

**Problemas de compilación**
- Verificar Android SDK instalado
- Actualizar Android Studio a última versión
- Limpiar proyecto: `./gradlew clean`

### Contacto
- **GitHub Issues**: [Reportar problemas](https://github.com/Blxckbxll24/TalkingChildrenWearOS/issues)
- **Desarrollador**: Blxckbxll24

---

**TalkingChildren Wear OS** - Comunicación accesible desde la muñeca 🎯