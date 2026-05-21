# game-of-life
Java implementation of Conway's Game of Life with JavaFX GUI, developed for the Component Programming (Programowanie komponentowe) course.

## Requirements

- Java 23+
- Maven 3.6+

## Project Structure

```
game-of-life/
├── Model/                          # Game logic module
│   ├── src/main/java/org/example/
│   │   ├── GameOfLifeBoard.java    # Board with simulation logic
│   │   ├── GameOfLifeCell.java     # Single cell (state, neighbours, rules)
│   │   ├── GameOfLifeLine.java     # Abstract row/column with live/dead counts
│   │   ├── GameOfLifeRow.java      # Row of cells
│   │   ├── GameOfLifeColumn.java   # Column of cells
│   │   ├── GameOfLifeSimulator.java# Simulator interface
│   │   ├── PlainGameOfLifeSimulator.java # Default simulator implementation
│   │   ├── Dao.java                # DAO interface for board persistence
│   │   ├── FileGameOfLifeBoardDao.java   # File-based DAO implementation
│   │   ├── GameOfLifeBoardDaoFactory.java# DAO factory
│   │   └── Main.java               # Console entry point
│   └── src/test/java/org/example/  # Unit tests for Model
├── View/                           # JavaFX GUI module
│   ├── src/main/java/org/example/view/
│   │   ├── HelloApplication.java   # JavaFX application entry point
│   │   ├── ConfigurationController.java # Configuration screen controller
│   │   ├── SimulationController.java    # Simulation screen controller
│   │   ├── LevelSize.java          # Enum: board size presets
│   │   └── LevelDensity.java       # Enum: live cell density presets
│   └── src/main/resources/org/example/view/
│       ├── configurationScene.fxml # Configuration screen layout
│       └── simulationScene.fxml    # Simulation screen layout
├── checkstyle2023.xml              # Checkstyle ruleset
└── pom.xml                         # Root multi-module Maven build
```

## Running the Application

### JavaFX GUI

```bash
mvn javafx:run -pl View
```

### Console (Model only)

```bash
mvn compile exec:java -pl Model
```

## Running Tests

```bash
mvn test
```

Or for a single module:

```bash
mvn test -pl Model
```

## Full Verification (tests + checkstyle + coverage report)

```bash
mvn verify
```

## HTML Reports

Run the following command to generate all HTML reports:

```bash
mvn site -pl Model
```

Reports are generated under `Model/target/site/`:

| Report | Path | Description |
|---|---|---|
| JaCoCo (code coverage) | `Model/target/site/jacoco/index.html` | Line/branch coverage per class |
| Checkstyle | `Model/target/site/checkstyle.html` | Code style violations |
| Project Info | `Model/target/site/index.html` | General project info (dependencies, plugins) |

> **Note:** `mvn verify` generates only the JaCoCo data file. To produce the full HTML reports, run `mvn site` (or `mvn verify site`).
