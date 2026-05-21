# game-of-life
Java implementation of Conway's Game of Life with JavaFX GUI, developed for the Component Programming (Programowanie komponentowe) course.

## Requirements

- Java 21+
- Maven 3.6+

## Project Structure

```
game-of-life/
├── src/
│   ├── main/java/org/example/
│   │   ├── Cell.java               # Represents a single cell (alive/dead state, transition rules)
│   │   └── GameOfLifeBoard.java    # Board logic, simulation loop, console rendering
│   └── test/java/org/example/
│       ├── CellTest.java           # Unit tests for Cell
│       └── GameOfLifeBoardTest.java# Unit tests for the board
├── checkstyle2023.xml              # Checkstyle ruleset
└── pom.xml                         # Maven build configuration
```

## Running the Application

```bash
mvn compile exec:java
```

## Running Tests

```bash
mvn test
```

## Full Verification (tests + checkstyle + coverage report)

```bash
mvn verify
```

## HTML Reports

Run the following command to generate all HTML reports:

```bash
mvn site
```

Reports are generated under `target/site/`:

| Report | Path | Description |
|---|---|---|
| JaCoCo (code coverage) | `target/site/jacoco/index.html` | Line/branch coverage per class |
| Checkstyle | `target/site/checkstyle.html` | Code style violations |
| Project Info | `target/site/index.html` | General project info (dependencies, plugins) |

> **Note:** `mvn verify` generates only the JaCoCo data file. To produce the full HTML reports, run `mvn site` (or `mvn verify site`).
