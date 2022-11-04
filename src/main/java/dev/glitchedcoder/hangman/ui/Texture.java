package dev.glitchedcoder.hangman.ui;

import dev.glitchedcoder.hangman.Hangman;
import dev.glitchedcoder.hangman.util.Validator;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public enum Texture {

    UA("a"),
    UB("b"),
    UC("c"),
    UD("d"),
    UE("e"),
    UF("f"),
    UG("g"),
    UH("h"),
    UI("i"),
    UJ("j"),
    UK("k"),
    UL("l"),
    UM("m"),
    UN("n"),
    UO("o"),
    UP("p"),
    UQ("q"),
    UR("r"),
    US("s"),
    UT("t"),
    UU("u"),
    UV("v"),
    UW("w"),
    UX("x"),
    UY("y"),
    UZ("z"),
    LA("_A"),
    LB("_B"),
    LC("_C"),
    LD("_D"),
    LE("_E"),
    LF("_F"),
    LG("_G"),
    LH("_H"),
    LI("_I"),
    LJ("_J"),
    LK("_K"),
    LL("_L"),
    LM("_M"),
    LN("_N"),
    LO("_O"),
    LP("_P"),
    LQ("_Q"),
    LR("_R"),
    LS("_S"),
    LT("_T"),
    LU("_U"),
    LV("_V"),
    LW("_W"),
    LX("_X"),
    LY("_Y"),
    LZ("_Z"),
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
    COLON("colon"),
    SELF_PORTRAIT("self_portrait"),
    JKRISTE_DEV("jkriste_dev"),
    A_TO_Z_ICON("atoz"),
    BACKSPACE_ICON("bck"),
    ZERO_TO_NINE_ICON("0to9"),
    BLANK_ICON("blank_icon"),
    APOSTROPHE("apostrophe"),
    QUESTIONMARK_PORTRAIT("questionmark"),
    LIGHT_FIXTURE_ON("light_fixture_on"),
    LIGHT_FIXTURE_OFF("light_fixture_off"),
    HANDS_BOUND("hands_bound"),
    HANDS_UNBOUND("hands_unbound"),
    TABLE_TEXTURE("table"),
    HANGING_MAN_1("hanging_man_1"),
    HANGING_MAN_2("hanging_man_2"),
    HANGING_MAN_3("hanging_man_3"),
    HANGING_MAN_4("hanging_man_4"),
    HANGING_MAN_5("hanging_man_5"),
    HANGING_MAN_6("hanging_man_6"),
    HANGING_MAN_7("hanging_man_7"),
    HANGING_MAN_8("hanging_man_8");

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