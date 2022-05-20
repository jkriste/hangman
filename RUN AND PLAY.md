## How to Run

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

Lucky for you, if this is a nuisance to you, and you don't like terminals, you can simply double-left-click the `Hangman.jar` file, and it will open.

## How to Play

After opening the game, a small intro will play, and then you will be landed on a screen asking for an API key.
This API key is given to participants of the hackman challenge**, however, you do not need your own key in order to play.
Simply typing the global key `12345` (with the alphanumeric key group (not the numeric keypad)) and then pressing enter will launch you into the main menu.

From the main menu, there are four different options to choose from:
- [Singleplayer](./RUN%20AND%20PLAY.md#singleplayer)
- [Multiplayer](./RUN%20AND%20PLAY.md#multiplayer)
- [Preferences](./RUN%20AND%20PLAY.md#preferences)
- Exit

All but the last option (as I hope it's obvious as to what it does) will be further explained below.

** _Unfortunately, the API to provide words to this game will not last forever. In order to preserve the game, in a future update, I will have the word list stored locally and the API Key Entry scene will be removed._

### Singleplayer

Singleplayer is split into two different categories: Story Mode and Free Mode.

Story Mode will give you the best experience through a tutorial of the layout and even an explanation as to how you ended up in this place, so it is recommended you start on this option.
In Story Mode you will meet the Executioner and will go through three phases of difficulty, based on word length.

Free Mode is like Story Mode, but without the dialogue and instead of three phases, only has one.
The word length in Free Mode is pseudo-random, between 4 and 14.

### Multiplayer

To preface this, this mode does not interact with any outside network nor your local network.
This mode is meant to be played between two people on one device.

Multiplayer allows you to first select the word length, between 4 and 14 letters (alphabetical letters only, sorry).
You will then be taken to a second screen where you type in this word with the length selected on the previous screen.
After, a second user can guess the word just like you would in Free Mode.

### Preferences

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

### Outro

That's all there is to know! Thank you so much for playing my game!

The Executioner awaits your arrival.