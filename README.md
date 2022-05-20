# Hanged Men

This repository is for the 'Hackman' Spring 2022 [Clemson CPSC Discord](https://www.discord.gg/clemsoncpsc) competition.

⭐ Won **first** place! ⭐

## Table of Contents

- [About](./README.md#about)
  - [Thanks](./README.md#thanks)
- [Features](./README.md#features)
- [Possible Features](./README.md#possible-features)
- [How to...](./RUN%20AND%20PLAY.md)
  - [How to Run](./RUN%20AND%20PLAY.md#how-to-run)
  - [How to Play](./RUN%20AND%20PLAY.md#how-to-play)
- [Technical Details](./TECHNICAL%20DETAILS.md)
  - [Assets & The Game Engine](./TECHNICAL%20DETAILS.md#assets--the-game-engine)
  - [Tech Stack](./TECHNICAL%20DETAILS.md#tech-stack)
  - [Local Data](./TECHNICAL%20DETAILS.md#local-data)
  - [Diagrams](./TECHNICAL%20DETAILS.md#diagrams)

## About

The game hangman is a child's game where a word is chosen at random and the user guesses what this word is letter by letter or by guessing what the word is. 
For each incorrect letter or guess, a stick figure drawing representing a hanged man is drawn out, shape by shape (or by body part, it's mostly subjective).
If the user solves the word either by guessing the correct letter or by guessing the correct word before the hangman drawing is complete, they win. 
The word randomly chosen will vary by length but are usually limited to nouns and verbs.

**Hanged Men** takes this child's game and puts a [Saw](https://en.wikipedia.org/wiki/Saw_(2004_film))-like spin on it.

### Thanks 

I'd like to thank [Jay](https://github.com/Jay-Madden) for coming up with the competition as well as being a judge.

Additional thanks to [Korey](https://github.com/kspalm) and [Dr. Dean](https://people.cs.clemson.edu/~bcdean/) for being judges as well.

## Features

- Single-player:
  - Story Mode: The user vs. The Executioner
  - Free Mode: A word is chosen based on either random or specified length.
- Multiplayer option where a user can input a word and another guesses the word put in.
- Custom-built (this means it's going to be bad) game engine by yours truly.
- Custom font and game assets also by yours truly.

## Possible Features

- Items awarded for progressing such as hints and extra lives?
