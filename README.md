# Hanged Men

This repository is for the 'Hackman' Spring 2022 [Clemson CPSC Discord](https://www.discord.gg/clemsoncpsc) competition.

## About

The game hangman is a child's game where a word is chosen at random and the user guesses what this word is letter by letter or by guessing what the word is. 
For each incorrect letter or guess, a stick figure drawing representing a hanged man is drawn out, shape by shape (or by body part, it's mostly subjective).
If the user solves the word either by guessing the correct letter or by guessing the correct word before the hangman drawing is complete, they win. 
The word randomly chosen will vary by length but are usually limited to nouns and verbs.

## Planned Features

- Story Mode: The user v. The Executioner
- Singleplayer option where a word is chosen based on either random or specified length.
- Multiplayer option where a user can input a word and another guesses the word put in.
- Custom-built (this means it's going to be bad) game engine by yours truly.
- Custom font and game assets also by yours truly.

## Possible Features

- A story-driven Inscryption-like theme where YOU, the user, are the man to be hanged, while your antagonist is the executioner.

## Nerd Stuff

This section of the README will include all the technical details of this project.

**Language**: Java 11 
<br />
**Build System**: Maven
<br />
**Dependencies**:

- [Google GSON v2.8.9](https://github.com/google/gson)
- [Lombok v1.18.22](https://github.com/projectlombok/lombok)
- [Google FindBugs v3.0.2](https://github.com/findbugsproject/findbugs)

### Local Data

This app does indeed store preferences (and possibly the last thing you were doing - check TODO).

Where this data is located depends on your OS:
- Windows: `C:\Users\YOUR_USERNAME\AppData\Roaming\Hanged Men Data`
- MacOS: `/Users/YOUR_USERNAME/Library/ApplicationSupport/Hanged Men Data`
- Linux: `/home/YOUR_USERNAME/.Hanged Man Data`

As of the writing of this, the following files are stored:
- `config.json` - This file stores preferences such as API Key and resolution.

### TODO

- Change game loop to correctly count ticks.
- Start on API requests using some kind of library?
- Figure out what is going on with `AudioSystem.getClip()`
  - `IOException: Stream closed`
  - `Unsupported control type: Master Gain`
- Work on texture pre-processing (most likely limited to buffered images).
- Start on menus and menu components.
  - Main menu.
  - Preferences menu.
  - Credits
- Entity types:
  - FadeIn (implement w/ Callback)
  - FadeOut (implement w/ Callback)
  - Text
  - TextBox
- Assets for various scenes.
  - Maybe a GitHub logo?
- Possibly make Scene serializable and store & load the previous scene from local data (i.e., a `CONTINUE` menu option)?