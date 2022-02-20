# Hanged Men

This repository is for the 'Hackman' Spring 2022 [Clemson CPSC Discord](https://www.discord.gg/clemsoncpsc) competition.

## About

The game hangman is a child's game where a word is chosen at random and the user guesses what this word is letter by letter or by guessing what the word is. 
For each incorrect letter or guess, a stick figure drawing representing a hanged man is drawn out, shape by shape (or by body part, it's mostly subjective).
If the user solves the word either by guessing the correct letter or by guessing the correct word before the hangman drawing is complete, they win. 
The word randomly chosen will vary by length but are usually limited to nouns and verbs.

Although I am not very good with drawing assets, all assets found [here](https://github.com/glitchedcoder/hangman/tree/main/src/main/resources/texture) were hand-drawn in Photoshop.
This game is meant to portray a sort of arcade-like feel, with (hopefully) a story-driven option that adds some creativity.
Other than the dependencies, the only standard library features that this game uses is JFrame (part of [Swing](https://en.wikipedia.org/wiki/Swing_(Java))) and Canvas (part of [AWT](https://en.wikipedia.org/wiki/Abstract_Window_Toolkit)).
The rest of this game is built from scratch, including [Scenes](https://github.com/glitchedcoder/hangman/blob/main/src/main/java/dev/glitchedcoder/hangman/window/Scene.java) and [Entities](https://github.com/glitchedcoder/hangman/blob/main/src/main/java/dev/glitchedcoder/hangman/entity/Entity.java).

I'd like to thank [Jay](https://github.com/Jay-Madden) for coming up with the competition as well as being a judge.

Additional thanks to [Korey](https://github.com/kspalm) and [Dr. Dean](https://people.cs.clemson.edu/~bcdean/) for being judges as well.

### How to Run

This game is developed in Java, specifically compiled by AdoptOpenJDK (HostSpot) version 11.0.11.
However, you do not need this specific release of Java to run this game.
Any Java release after 11 is capable of running the game thanks to backwards compatability.
In order to check if you already have the Java 11 JRE installed, you can run the command `java -version` from your preferred terminal.

If you get an output similar to the following, then you're set to start.
<br />
Make sure that the major version (highlighted below) is at or greater than 11.

![Java Version](https://i.imgur.com/KEN7pfz.png)

If not, you can download the AdoptOpenJDK v11 [here](https://adoptopenjdk.net/archive.html).
If you want your Java release straight from Oracle, you can download Java 17 [here](https://www.oracle.com/java/technologies/downloads/).

After you get the above or similar output, you get the [latest release](https://github.com/glitchedcoder/hangman/releases) by going to the assets and clicking the `Hanged Men.jar` file.
After that, you can type in `java -jar ` and drag + drop the downloaded jar to your terminal.
After that, all you need to do is press enter! The game will take care of the rest.

Lucky  for you, if this is a nuisance to you, and you don't like terminals, you can simply double-left-click the `Hangman.jar` file, and it will open.

### How to Play

After opening the game, a small intro will play, and then you will be landed on a screen asking for an API key.
This API key is given to participants of the hackman challenge**, however, you do not need your own key in order to play.
Simply typing the global key `12345` (with the alphanumeric key group and not the numeric keypad) and then pressing enter will launch you into the main menu.

From the main menu, there are four different options to choose from:
- Singleplayer
- Multiplayer
- Preferences
- Exit

All but the last option (as I hope it's obvious as to what it does) will be further explained below.

** _Unfortunately, the API to provide words to this game will not last forever. In order to preserve the game, in a future update, I will have the word list stored locally and the API Key Entry scene will be removed._

#### Singleplayer

Singleplayer is split into two different categories: Story Mode and Free Mode.

Story Mode will give you the best experience through a tutorial of the layout and even an explanation as to how you ended up in this place, so it is recommended you start on this option.
In Story Mode you will meet the Executioner and will go through three phases of difficulty, based on word length.

Free Mode is like Story Mode, but without the dialogue and instead of three phases, only has one.
The word length in Free Mode is pseudo-random, between 4 and 14.

#### Multiplayer

To preface this, this mode does not interact with any outside network nor your local network.
This mode is meant to be played between two people on one device.

Multiplayer allows you to first select the word length, between 4 and 14 letters (alphabetical letters only, sorry).
You will then be taken to a second screen where you type in this word with the length selected on the previous screen.
After, a second user can guess the word just like you would in Free Mode.

#### Preferences

In the Preferences menu, there are two sliders: Resolution and NSFL (short for Not Safe for Life, this will be explained later).
The scrollable options in the menu do not currently show the settings that you have applied.
This may be updated in the future, but I don't plan on making it a priority.

For resolutions, the following options are available (width x height - in pixels):
- 1024x576
- 1152x648
- 1280x720
- 1600x900
- 1920x1080
- 2560x1440
- 3840x2160

For NSFL, the following options are available:
- NSFL ON
- NSFL OFF

NSFL is an option due to a rather graphic scene that pops up when on the game over screen.
If you are uncomfortable with the rather dark tone of this game, you can select NSFL OFF in order to turn off more disturbing images.
In order to distinguish between NSFL being on or off, in the main menu and all games, you can distinguish between the hands:

**NSFL ON**:
![NSFL ON](https://i.imgur.com/ZY2ngsl.png)

**NSFL OFF**:
![NSFL OFF](https://i.imgur.com/13sxwxi.png)

In order for the changes to take effect (for both resolution and NSFL toggle), you need to select the `APPLY + RESTART` menu option.
This will restart the game for you with these settings applied and saved to your local configuration file.

#### Outro

That's all there is to know! Thank you so much for playing my game!

The Executioner awaits your arrival.

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

- [Google GSON v2.9.0](https://github.com/google/gson)
- [Lombok v1.18.22](https://github.com/projectlombok/lombok)
- [Google FindBugs v3.0.2](https://github.com/findbugsproject/findbugs)
- [Apache HttpComponents Client v4.5.13](https://github.com/apache/httpcomponents-client)

_Dependencies are shaded with the JAR and do not need to be downloaded._

### Local Data

This app does indeed store preferences (and possibly the last thing you were doing - check TODO).

Where this data is located depends on your OS:
- Windows: `%appdata%\Hanged Men Data`
- MacOS: `/Users/YOUR_USERNAME/Library/ApplicationSupport/Hanged Men Data`
- Linux: `/home/YOUR_USERNAME/Hanged Men Data`

As of the writing of this, the following files are stored:
- `config.json` - This file stores the following preferences:
  - API Key
  - Resolution
  - NSFL

### Diagrams

**Entity Inheritance Diagram**
![Entity Diagram](https://i.imgur.com/w7mYhTA.png)

**Scene Inheritance Diagram**
![Scene Diagram](https://i.imgur.com/5pMOV9S.png)