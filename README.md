# Hangman

This repository is for the 'Hackman' Spring 2022 [Clemson CPSC Discord](https://www.discord.gg/clemsoncpsc) competition.

## About

The game hangman is a child's game where a word is chosen at random and the user guesses what this word is letter by letter or by guessing what the word is. 
For each incorrect letter or guess, a stick figure drawing representing a hanged man is drawn out, shape by shape (or by body part, it's mostly subjective).
If the user solves the word either by guessing the correct letter or by guessing the correct word before the hangman drawing is complete, they win. 
The word randomly chosen will vary by length but are usually limited to nouns and verbs.

## Planned Features

- Singleplayer option where a word is chosen from an existing list by difficulty (easy, medium, hard).
- Multiplayer option where a user can input a word and another guesses the word put in.
- Custom-built (this means it's going to be bad) game engine by yours truly.
- Custom font and game assets also by yours truly.

## Possible Features

- A story-driven Inscryption-like theme where YOU, the user, are the man to be hanged, while your antagonist is the executioner.
- 

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

