package dev.glitchedcoder.hangman.scene.mode;

import dev.glitchedcoder.hangman.entity.FadeIn;
import dev.glitchedcoder.hangman.entity.FadeOut;
import dev.glitchedcoder.hangman.entity.FixedTexture;
import dev.glitchedcoder.hangman.entity.IconOverlay;
import dev.glitchedcoder.hangman.entity.LightFixture;
import dev.glitchedcoder.hangman.entity.Location;
import dev.glitchedcoder.hangman.entity.RenderPriority;
import dev.glitchedcoder.hangman.entity.TextBox;
import dev.glitchedcoder.hangman.json.Script;
import dev.glitchedcoder.hangman.json.ScriptSection;
import dev.glitchedcoder.hangman.ui.Icon;
import dev.glitchedcoder.hangman.ui.Portrait;
import dev.glitchedcoder.hangman.ui.Texture;
import dev.glitchedcoder.hangman.ui.TexturePreprocessor;
import dev.glitchedcoder.hangman.window.Scene;
import dev.glitchedcoder.hangman.window.key.Key;
import dev.glitchedcoder.hangman.window.key.KeySelector;
import lombok.EqualsAndHashCode;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public class StoryIntro extends Scene {

    private final Set<Key> keys;
    private final FadeIn fadeIn;
    private final TextBox textBox;
    private final FadeOut fadeOut;
    private final LightFixture light;
    private final FixedTexture table;
    private final FixedTexture hands;
    private final IconOverlay overlay;

    public StoryIntro() {
        this.keys = KeySelector.create().with(Key.ENTER).build();
        this.overlay = new IconOverlay(this, Color.WHITE, 3);
        this.textBox = new TextBox(this, Portrait.UNKNOWN, Color.WHITE);
        this.light = new LightFixture(this, (byte) 10, 4.1);
        BufferedImage table = new TexturePreprocessor(Texture.TABLE_TEXTURE)
                .scale(4)
                .build();
        BufferedImage hands = new TexturePreprocessor(config.getNSFL().isOn() ? Texture.HANDS_BOUND : Texture.HANDS_UNBOUND)
                .scale(4)
                .build();
        this.table = new FixedTexture(this, table);
        this.hands = new FixedTexture(this, hands);
        this.fadeIn = new FadeIn(this, Color.BLACK, (byte) 11);
        this.fadeOut = new FadeOut(this, Color.BLACK, (byte) 11);
    }

    @Override
    protected void onLoad() {
        fadeIn.onFinish(() -> {
            textBox.spawn();
            fadeIn.dispose();
        });
        fadeOut.onFinish(() -> setScene(new StoryMode(Phase.PHASE_ONE)));
        overlay.setIcon(Icon.ENTER, 2, 0);
        Script script = Script.getScript();
        String crime = script.randomCrime();
        List<String> introBegin = script.getSection(ScriptSection.INTRODUCTION);
        byte index = 0;
        for (byte b = 0; b < introBegin.size(); b++) {
            if (introBegin.get(b).contains("%crime%"))
                index = b;
        }
        introBegin.set(index, introBegin.get(index).replace("%crime%", crime));
        textBox.addLines(introBegin);
        textBox.onFinish(() -> {
            textBox.setPortrait(Portrait.EXECUTIONER);
            textBox.addLines(script.getSection(
                    config.hasPlayedBefore() ? ScriptSection.INTRODUCTION_NO_TUTORIAL : ScriptSection.INTRODUCTION_TUTORIAL
            ));
            textBox.onFinish(fadeOut::spawn);
        });
        this.light.setRenderPriority(new RenderPriority(124));
        this.hands.setRenderPriority(new RenderPriority(125));
        this.textBox.setRenderPriority(RenderPriority.MAX);
        this.hands.setLocation(Location.bottomCenter(hands.getBounds()));
        this.overlay.setLocation(Location.bottomLeft(overlay.getBounds()));
        this.textBox.setLocation(Location.bottomCenter(textBox.getBounds()));
        addRenderables(hands, table, light, overlay, textBox, fadeIn, fadeOut);
        spawnAll(hands, table, light, overlay, fadeIn);
    }

    @Override
    protected void onUnload() {
        // do nothing
    }

    @Override
    protected Set<Key> getKeyListeners() {
        return this.keys;
    }

    @Override
    protected void onKeyPress(Key key) {
        if (fadeIn.shouldDraw() || fadeOut.shouldDraw())
            return;
        if (key == Key.ENTER)
            textBox.nextLine();
    }
}