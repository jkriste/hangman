## Technical Details

This section will include all the technical details of this project.

### Assets & The Game Engine

Although I am not very good with drawing assets, all assets found [here](https://github.com/glitchedcoder/hangman/tree/main/src/main/resources/texture) were hand-drawn in Photoshop.
This game is meant to portray a sort of arcade-like feel, with a story-driven option that adds some creativity.
Other than the dependencies, the only standard library features that this game uses is JFrame (part of [Swing](https://en.wikipedia.org/wiki/Swing_(Java))) and Canvas (part of [AWT](https://en.wikipedia.org/wiki/Abstract_Window_Toolkit)).
The rest of this game is built from scratch, including [Scenes](https://github.com/glitchedcoder/hangman/blob/main/src/main/java/dev/glitchedcoder/hangman/window/Scene.java) and [Entities](https://github.com/glitchedcoder/hangman/blob/main/src/main/java/dev/glitchedcoder/hangman/entity/Entity.java).

**Note**: Some, if not most, of the code in this project uses [runtime verification](https://en.wikipedia.org/wiki/Runtime_verification).
Check the [Validator](src/main/java/dev/glitchedcoder/hangman/util/Validator.java) class in this project to view the methods that are used.
The reasoning for this type of approach is that, although I have tried my best to make sure there are close to zero errors during the development of this project, I am not infallible.
Runtime verification helps me assure that everything is running smoothly.
If not, then I have a reason as to what went wrong where.
No unit tests needed.

### Tech Stack

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

This app does indeed store preferences.

Where this data is located depends on your operating system:
- Windows: `%appdata%\Hanged Men Data`
- MacOS: `/Users/YOUR_USERNAME/Library/ApplicationSupport/Hanged Men Data`
- Linux: `/home/YOUR_USERNAME/Hanged Men Data`

As of the writing of this, the following files are stored:
- `config.json` - This file stores the following preferences:
    - API Key: Used to access the [word API](https://github.com/Jay-Madden/hackmanapi).
    - Resolution: The size of the window.
    - NSFL: Enables NSFL scenes and dialogue.
    - Played Before: Enables and disables tutorial dialogue.

### Diagrams

The following diagrams were made with [draw.io](https://draw.io).
These diagrams were made post-submission and were meant to display how the game works.

**Entity Inheritance Diagram**
![Entity Diagram](https://i.imgur.com/w7mYhTA.png)

**Scene Inheritance Diagram**
![Scene Diagram](https://i.imgur.com/7pdPA4v.png)

**Scene Activity Diagram**
![Scene Activity Diagram](https://i.imgur.com/f7Y8nQI.png)