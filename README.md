# Tchuka Ruma - Juego de Mancala

## Una tradición reinventada

🎮✨ Tchuka Ruma es una adaptación del clásico juego de mesa Mancala, un pasatiempo milenario lleno de estrategia y habilidad. En este juego, los jugadores compiten moviendo semillas entre hoyos en un tablero con el objetivo de almacenarlas todas en la "Ruma" (el almacén). Esta versión digital para Android combina la emoción del juego original con una interfaz moderna, animaciones dinámicas y opciones de personalización. 🎉

---

## Características principales

🎯 **Inicio interactivo**: Una pantalla principal intuitiva con botones para jugar o acceder a las instrucciones.

⚙️ **Configuración personalizada**: Decide el número inicial de semillas por hoyo para ajustar la dificultad.

🌟 **Animaciones atractivas**: Observa los movimientos visuales de las semillas entre los hoyos en tiempo real.

🏆 **Eventos finales**: Mensajes de victoria o derrota con opciones para reiniciar o salir.

📱 **Compatibilidad**: Un diseño responsivo adaptable a distintos tamaños de pantalla.

---

## Tecnologías utilizadas

🛠️ **Lenguaje**: Kotlin.

🎨 **Diseño**: Construido con ConstraintLayout y ViewBinding para una experiencia fluida.

🎥 **Animaciones**: Implementadas con `ObjectAnimator` y `AnimatorSet` para transiciones suaves.

🔄 **Ciclo de vida optimizado**: Uso de `lifecycleScope` y corrutinas de Kotlin para una gestión eficiente.

---

## Estructura del proyecto

🎯 El juego está organizado en tres actividades principales:

### `HomeActivity`

- Pantalla de inicio que ofrece dos opciones:
  - **Jugar**: Redirige a `MainActivity`. 🕹️
  - **Cómo jugar**: Redirige a `HowToPlayActivity`. 📖

### `MainActivity`

- Controla la experiencia de juego, incluyendo:
  - **Configuración inicial**: Determina cuántas semillas empiezan en cada hoyo. 🌱
  - **Lógica del juego**: Gestionada en la clase `MancalaGame`. 🎮
  - **Interfaz gráfica dinámica**: Actualizaciones en tiempo real. 🖌️
  - **Animaciones de movimiento**: Visualiza cómo las semillas se mueven entre los hoyos. 🔄

### `MancalaGame`

- **Modelo de juego**: Define las reglas, estados y condiciones de victoria o derrota. 🧩

---

## Manual explicativo de la aplicación

### Pasos básicos:

1. **Inicio**: Desde la pantalla principal, selecciona "Jugar" para comenzar una partida o "Cómo jugar" para leer las instrucciones. 🕹️
2. **Configuración**: Al iniciar el juego, ajusta el número inicial de semillas en un diálogo emergente. 🌱
3. **Desarrollo del juego**:
   - Selecciona un hoyo para iniciar el movimiento de las semillas. 👆
   - Observa las animaciones y analiza el estado actual del tablero. 🎥

---

## Cómo jugar al Tchuka Ruma

✨ El tablero de Tchuka Ruma consta de 5 hoyos dispuestos en fila:

- **Cuatro hoyos iniciales**: Contienen tantas semillas como se haya seleccionado.
- **La Ruma**: Un quinto hoyo más grande que funciona como almacén, inicialmente vacío.

🎯 **Objetivo del juego**:
Almacenar todas las semillas en la Ruma siguiendo una serie de movimientos estratégicos.

🌱 **Reglas del juego**:

i) Toma las semillas de un hoyo cualquiera y repartidas, una por una, en los hoyos siguientes en dirección hacia la Ruma.

ii) Si quedan semillas en la mano después de llegar a la Ruma, se continúan sembrando las semillas desde el lado opuesto del tablero.

iii) Según donde caiga la última semilla, pueden ocurrir tres situaciones:

- **a)** El hoyo tiene más semillas: se toman todas las semillas de ese hoyo y repite el proceso.
- **b)** La última semilla cae en la Ruma: se deja la semilla y elige otro hoyo para continuar.
- **c)** El hoyo está vacío: Pierdes la partida y deberás reiniciar.

iv) El juego termina cuando todas las semillas están almacenadas en la Ruma. ¡Has ganado! 🏆

