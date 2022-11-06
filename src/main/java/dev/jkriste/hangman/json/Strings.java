package dev.jkriste.hangman.json;

public enum Strings {

    MENU_EXIT("menu.generic.exit"),
    MENU_BACK("menu.generic.back"),
    SINGLEPLAYER("menu.main.single"),
    MULTIPLAYER("menu.main.multi"),
    PREFERENCES("menu.main.settings"),
    MULTI_PROMPT("menu.multi.prompt"),
    NEXT("menu.multi.next"),
    RESUME("menu.pause.resume"),
    MAIN_MENU("menu.pause.main"),
    CHANGE_KEY("menu.pref.changekey"),
    APPLY("menu.pref.apply"),
    STORY("menu.single.story"),
    FREE("menu.single.free"),
    WORD_PROMPT("menu.word.prompt"),
    SUBMIT("menu.word.submit"),
    WIN("story.end.win"),
    LOSS("story.end.loss"),
    KEY_PROMPT("menu.key.prompt"),
    KEY_FAIL("menu.key.fail");

    private final String id;

    Strings(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}