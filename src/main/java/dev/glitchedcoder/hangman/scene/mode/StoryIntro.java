package dev.glitchedcoder.hangman.scene.mode;

import dev.glitchedcoder.hangman.entity.FixedTexture;
import dev.glitchedcoder.hangman.entity.IconOverlay;
import dev.glitchedcoder.hangman.entity.LightFixture;
import dev.glitchedcoder.hangman.entity.Location;
import dev.glitchedcoder.hangman.entity.RenderPriority;
import dev.glitchedcoder.hangman.entity.TextBox;
import dev.glitchedcoder.hangman.json.Script;
import dev.glitchedcoder.hangman.json.ScriptSection;
import dev.glitchedcoder.hangman.ui.Icon;
import dev.glitchedcoder.hangman.ui.NSFL;
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
    private final TextBox textBox;
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
        boolean nsfl = config.getNSFL() == NSFL.ON;
        BufferedImage hands = new TexturePreprocessor(nsfl ? Texture.HANDS_BOUND : Texture.HANDS_UNBOUND)
                .scale(4)
                .build();
        this.table = new FixedTexture(this, table);
        this.hands = new FixedTexture(this, hands);
    }

    @Override
    protected void onLoad() {
        overlay.setIcon(Icon.ENTER, 2, 0);
        Script script = Script.getScript();
        String crime = script.randomCrime();
        List<String> introBegin = script.getSection(ScriptSection.INTRODUCTION_BEGIN);
        byte index = 0;
        for (byte b = 0; b < introBegin.size(); b++) {
            if (introBegin.get(b).contains("%crime%"))
                index = b;
        }
        introBegin.set(index, introBegin.get(index).replace("%crime%", crime));
        textBox.addLines(introBegin);
        textBox.onFinish(() -> {
            textBox.setPortrait(Portrait.EXECUTIONER);
            textBox.addLines(script.getSection(ScriptSection.INTRODUCTION_END));
            textBox.onFinish(() -> setScene(new PhaseMode(Phase.PHASE_ONE)));
        });
        this.light.setRenderPriority(new RenderPriority(124));
        this.hands.setRenderPriority(new RenderPriority(125));
        this.textBox.setRenderPriority(RenderPriority.MAX);
        this.overlay.setLocation(Location.bottomLeft(overlay.getBounds()));
        this.textBox.setLocation(Location.bottomCenter(textBox.getBounds()));
        addRenderables(hands, table, light, overlay, textBox);
        spawnAll(hands, table, light, overlay, textBox);
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
        if (key == Key.ENTER)
            textBox.nextLine();
    }
}