# Slang Dictionary Manager (Java Swing)

## Introduction

This project is a comprehensive Slang Word dictionary application built using **Java** with **Java Swing** for the graphical user interface (GUI). It provides a full set of functionalities allowing users to search, manage (add, edit, delete), get a random slang, review history, and engage in quizzes based on the stored Slang Word data.

The dictionary data is managed efficiently using a `Map<String, TreeSet<String>>` structure for high-performance lookups and is persisted across sessions by writing to a designated file.

-----

## Key Features

The application is structured into three main sections, providing 10 core functionalities:

### 1\. Search and Management

| ID | Feature | Description |
| :---: | :--- | :--- |
| **Q1** | **Search by Slang Word** | Search for a specific Slang Word (case-insensitive) and display all its associated definitions. |
| **Q2** | **Search by Definition** | Find all Slang Words whose definitions contain a given keyword (case-insensitive). |
| **Q4** | **Add Slang Word** | Add a new Slang Word and its definition. If the word exists, the user is prompted to choose between **Overwrite** (replace all old definitions) or **Add Definition** (append the new definition). |
| **Q5** | **Edit Definition** | Allows the user to select an existing definition to edit. If the new definition is empty, the old one is deleted. If a Slang Word runs out of definitions, the word is deleted from the dictionary. |
| **Q6** | **Delete Slang Word** | Permanently remove a Slang Word and all its definitions from the dictionary. |
| **Q8** | **Random Slang Word** | Displays a randomly selected Slang Word and its definitions (the "Slang of the Day" feature). |

### 2\. History and Maintenance

| ID | Feature | Description |
| :---: | :--- | :--- |
| **Q3** | **View Search History** | Display a list of all successfully searched Slang Words, sorted by **most recent first**. The history is persistent across application restarts. |
| **Q7** | **Reset Dictionary** | Restores the dictionary to its **original source state** (`slang.txt`). This action deletes the current save file (`current_slang.txt`) and clears the search history. |

### 3\. Quiz (Fun Mode)

| ID | Feature | Description |
| :---: | :--- | :--- |
| **Q9** | **Quiz: Slang → Definition** | Displays one random Slang Word and four definition choices. One choice is correct, and three are distractors taken from three different Slang Words. |
| **Q10** | **Quiz: Definition → Slang** | Displays one random definition and four Slang Word choices. One choice is correct, and three are distractors. |

-----

## Technology and Project Structure

* **Language:** Java
* **GUI Library:** Java Swing (All UI manipulations are handled safely on the Event Dispatch Thread - EDT).
* **Main Data Structure:** `Map<String, TreeSet<String>>` for efficient, sorted storage of definitions.

### Project Directory

```
├── data/
│   ├── slang.txt           # ORIGINAL SOURCE FILE (Q7 Reset)
│   ├── current_slang.txt   # Current working dictionary (saved state)
│   └── history.txt         # Search history file
├── src/
│   ├── Main.java           # Contains the main() method to launch the application
│   ├── model/
│   │   ├── SlangDictionary.java  # Core business logic and data persistence
│   │   ├── HistoryManager.java   # Manages search history (load/save)
│   │   └── QuizQuestion.java     # Quiz data structure
│   └── view/
│       ├── MainFrame.java        # Main window and tab navigation
│       ├── SlangPanel.java       # Panel for Q1, Q2, Q4, Q5, Q6, Q8
│       ├── HistoryPanel.java     # Panel for Q3
│       └── QuizPanel.java        # Panel for Q9, Q10
```

-----

## Setup and Usage

### Requirements

* Java Development Kit (JDK) 8 or newer.

### Running the Application

1.  Ensure you have the `data` folder and the source file `slang.txt` correctly placed at the project root.
2.  Compile all Java files.
3.  Execute the main class `Main.java`.

```bash
# Example compilation (adjust path/classpath as needed)
javac -d bin src/**/*.java

# Example run
java -cp bin Main
```

### Exiting Safely

The application uses a `WindowListener` to manage shutdown. When the user closes the main window, the application automatically calls the `quit()` method in `SlangDictionary` to save all current changes and history before exiting.
