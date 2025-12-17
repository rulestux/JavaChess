# ♟️ JavaChess Project (Console Application)

This project is a **console-based chess game implemented in Java**, developed as a personal extension of the architecture proposed by **Nélio Alves**.
The main goal was to reinforce **object-oriented design, clean architecture principles, and separation of concerns**, while experimenting with a richer **CLI-based user interface**.

## 🧱 Architecture Overview

The project follows a **layered design inspired by MVC principles**:

* **Model**: game rules and domain logic (`ChessMatch`, `Board`, `Piece`, `ChessPiece`)
* **View**: terminal-based UI responsible only for input/output (`UI`)
* **Controller**: application flow and orchestration (`Program`)

The domain layer is fully decoupled from the presentation layer, allowing the game logic to remain independent from the UI implementation.

## ✨ Features

* Fully playable **chess game in the terminal**
* Clear separation between **domain logic and UI**
* **ANSI-colored CLI interface** for better readability
* Visual indication of:

  * current turn and active player
  * check and checkmate states
  * possible moves for selected pieces
* Dynamic listing of **captured pieces**
* Clean, modular code focused on **low coupling and high cohesion**

## 🛠️ Technologies & Tools

* **Java**
* Object-Oriented Programming (OOP)
* MVC-inspired layered architecture
* ANSI escape codes for terminal UI

### ✍️ Development Environment

All source files were written **entirely using terminal-based editors**:

* **Vim**
* **Neovim**
* **coc.nvim** with **coc-java** for Java language support (LSP, autocompletion, diagnostics)

No IDEs were used during development — the project was built using a **CLI-first workflow**, reinforcing familiarity with terminal tooling and editor-driven development.

## 🎯 Learning Outcomes

This project helped consolidate practical knowledge of:

* Object-oriented design (encapsulation, inheritance, polymorphism)
* Clean Code and separation of concerns
* Domain-driven thinking in small applications
* Building maintainable **backend-style logic** independent of presentation
* Working efficiently with **Vim/Neovim + LSP** for Java development

## 🚀 Next Steps (Ideas)

* Add full chess rules (castling, en passant, promotion)
* Refactor UI to support different frontends
* Create automated tests for domain logic

---

Feel free to explore, fork, or suggest improvements!

