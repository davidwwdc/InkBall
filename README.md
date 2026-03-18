# InkBall

InkBall is a Java-based game inspired by the Windows XP Tablet PC game. Players use a mouse or stylus to draw lines that guide moving balls into holes of corresponding colors. The game combines simple mechanics with physics-based movement and strategic problem solving.

---

## Features

- Real-time ball movement with physics  
- Draw lines to redirect balls  
- Color-matching gameplay  
- Multiple levels (level1.txt, level2.txt, level3.txt)  
- Simple user interface  

---

## Project Structure

```
InkBall/
│── bin/                 # Compiled classes
│── build/               # Build output
│── gradle/              # Gradle wrapper files
│── src/                 # Source code
│── level1.txt           # Level configuration file
│── level2.txt           # Level configuration file
│── level3.txt           # Level configuration file
│── build.gradle         # Gradle build file
│── gradlew              # Gradle wrapper (Linux/Mac)
│── gradlew.bat          # Gradle wrapper (Windows)
│── config.json          # Game configuration
```

---

## Requirements

- Java JDK 8 or higher  
- Gradle  

---

## Installation and Setup

1. Clone the repository:

```bash
git clone https://github.com/davidwwdc/InkBall.git
cd InkBall
```

2. Build the project using Gradle:

```bash
./gradlew build   # Linux / Mac
gradlew.bat build # Windows
```

3. Run the application:

```bash
./gradlew run   # Linux / Mac
gradlew.bat run # Windows
```

---

## Controls

- Mouse or stylus: Draw lines to guide balls  
- Objective: Guide each ball into the correct colored hole  

---

## Levels

- `level1.txt`, `level2.txt`, `level3.txt` define the different stages of the game  
- You can add custom levels by creating additional `.txt` files in the same format  

---

## Future Improvements

- Add more levels and difficulty scaling  
- Improve physics interactions  
- Add sound effects and animations  
- Create a level editor for custom maps  

---

## Contributing

Contributions are welcome:

1. Fork the repository  
2. Create a new branch (`feature/your-feature`)  
3. Commit your changes  
4. Push to your branch  
5. Open a Pull Request  

---

## License

This project is licensed under the MIT License.

---

## Author

https://github.com/davidwwdc
