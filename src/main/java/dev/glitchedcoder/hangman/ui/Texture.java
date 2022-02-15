package dev.glitchedcoder.hangman.ui;

import dev.glitchedcoder.hangman.Hangman;
import dev.glitchedcoder.hangman.util.Validator;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public enum Texture {

    A("a"),
    B("b"),
    C("c"),
    D("d"),
    E("e"),
    F("f"),
    G("g"),
    H("h"),
    I("i"),
    J("j"),
    K("k"),
    L("l"),
    M("m"),
    N("n"),
    O("o"),
    P("p"),
    Q("q"),
    R("r"),
    S("s"),
    T("t"),
    U("u"),
    V("v"),
    W("w"),
    X("x"),
    Y("y"),
    Z("z"),
    N0("0"),
    N1("1"),
    N2("2"),
    N3("3"),
    N4("4"),
    N5("5"),
    N6("6"),
    N7("7"),
    N8("8"),
    N9("9"),
    ENTER_ICON("ent"),
    ESCAPE_ICON("esc"),
    LEFT_ARROW_ICON("left_arrow"),
    RIGHT_ARROW_ICON("right_arrow"),
    UP_ARROW_ICON("up_arrow"),
    DOWN_ARROW_ICON("down_arrow"),
    EXECUTIONER("Executioner"),
    TEXT_BOX("textbox"),
    SPACE("space"),
    COMMA("comma"),
    PERIOD("period"),
    EXCLAMATION_POINT("exclamation_point"),
    QUESTION_MARK("question_mark"),
    AND("and"),
    SOLID_LEFT_ARROW("solid_left_arrow"),
    SOLID_RIGHT_ARROW("solid_right_arrow"),
    UNDERSCORE("underscore"),
    SELF_PORTRAIT("self_portrait"),
    A_TO_Z_ICON("atoz"),
    BACKSPACE_ICON("bck"),
    ZERO_TO_NINE_ICON("0to9"),
    BLANK_ICON("blank_icon"),
    APOSTROPHE("apostrophe"),
    QUESTIONMARK_PORTRAIT("questionmark");

    private final String loc;

    Texture(String loc) {
        this.loc = "/texture/" + loc + ".png";
    }

    @Nullable
    public BufferedImage asImage() {
        try (InputStream in = Hangman.class.getResourceAsStream(this.loc)) {
            Validator.requireNotNull(in, "Failed to load texture '{}'!", this.loc);
            return ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}