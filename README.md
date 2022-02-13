# Hanged Men

This repository is for the 'Hackman' Spring 2022 [Clemson CPSC Discord](https://www.discord.gg/clemsoncpsc) competition.

## About

The game hangman is a child's game where a word is chosen at random and the user guesses what this word is letter by letter or by guessing what the word is. 
For each incorrect letter or guess, a stick figure drawing representing a hanged man is drawn out, shape by shape (or by body part, it's mostly subjective).
If the user solves the word either by guessing the correct letter or by guessing the correct word before the hangman drawing is complete, they win. 
The word randomly chosen will vary by length but are usually limited to nouns and verbs.

Although I am not very good with drawing assets, all of the assets found [here](https://github.com/glitchedcoder/hangman/tree/main/src/main/resources/texture) were hand-drawn in Photoshop.
This game is meant to portray a sort of arcade-like feel, with (hopefully) a story-driven option that adds some creativity.
Other than the dependencies, the only standard library features that this game uses is JFrame (part of [Swing](https://en.wikipedia.org/wiki/Swing_(Java))) and Canvas (part of [AWT](https://en.wikipedia.org/wiki/Abstract_Window_Toolkit)).
The rest of this game is built from scratch, including [Scenes](https://github.com/glitchedcoder/hangman/blob/main/src/main/java/dev/glitchedcoder/hangman/window/Scene.java) and [Entities](https://github.com/glitchedcoder/hangman/blob/main/src/main/java/dev/glitchedcoder/hangman/entity/Entity.java).

I'd like to thank [Jay](https://github.com/Jay-Madden) for coming up with the competition as well as being a judge.

Additional thanks to [Korey](https://github.com/kspalm) and [Dr. Dean](https://people.cs.clemson.edu/~bcdean/) for being judges as well.

### How to Run

This game is developed in Java, specifically compiled by AdoptOpenJDK (HostSpot) version 11.0.11.
However, you do not need this specific release of Java 11 to run this game.
In order to check if you already have the Java 11 JRE installed, you can run the command `java -version` from your preferred terminal.

If you get an output similar to the following, then you're set to start.
![Java Version](https://i.imgur.com/CnZo635.png)

If not, you can download the AdoptOpenJDK v11 [here](https://adoptopenjdk.net/archive.html).

After you get the above output, you get the [latest releases](https://github.com/glitchedcoder/hangman/releases) by going to the assets and clicking the `Hanged Men.jar` file.
After that, you can type in `java -jar ` and drag + drop the downloaded jar to your terminal.
After that, all you need to do is press enter! The game will take care of the rest.

## Planned Features

- Single-player:
  - Story Mode: The user vs. The Executioner
  - Free Mode: A word is chosen based on either random or specified length.
- Multiplayer option where a user can input a word and another guesses the word put in.
- Custom-built (this means it's going to be bad) game engine by yours truly.
- Custom font and game assets also by yours truly.

## Possible Features

- Items awarded for progressing such as hints and extra lives?

## Nerd Stuff

This section of the README will include all the technical details of this project.

**Language**: Java
<br />
**JDK**: AdoptOpenJDK (HostSpot) version 11.0.11
<br />
**Build System**: Maven
<br />
**Dependencies**:

- [Google GSON v2.8.9](https://github.com/google/gson)
- [Lombok v1.18.22](https://github.com/projectlombok/lombok)
- [Google FindBugs v3.0.2](https://github.com/findbugsproject/findbugs)
- [Apache HttpComponents Client v4.5.13](https://github.com/apache/httpcomponents-client)

_Dependencies are shaded with the JAR and do not need to be downloaded._

### Local Data

This app does indeed store preferences (and possibly the last thing you were doing - check TODO).

Where this data is located depends on your OS:
- Windows: `C:\Users\YOUR_USERNAME\AppData\Roaming\Hanged Men Data`
- MacOS: `/Users/YOUR_USERNAME/Library/ApplicationSupport/Hanged Men Data`
- Linux: `/home/YOUR_USERNAME/Hanged Men Data`

As of the writing of this, the following files are stored:
- `config.json` - This file stores the following preferences:
  - API Key
  - Resolution
  - Volume

### TODO

- Change game loop to correctly count ticks. ✔️
- Start on API requests using some kind of library?
- Either use a `LineListener` or multithreading to play sounds.
- Work on texture pre-processing (most likely limited to buffered images). ✔️
- Start on menus and menu components. ✔️
  - Main menu.
  - Preferences menu.
- Entity types:
  - FadeIn (implement w/ Callback) ✔️
  - FadeOut (implement w/ Callback) ✔️
  - FixedTexture ✔️
  - TextInput ✔️
  - TextBox
- Assets for various scenes.
  - Maybe a GitHub logo?
- Work on a script/gameplay for the story mode. 
- Documentation!
  - Make sure to get methods that use `Validator#...` and add `@throws IllegalArgumentException ...` to it.
- Possibly make Scene serializable and store & load the previous scene from local data (i.e., a `CONTINUE` menu option)?