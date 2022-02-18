package dev.glitchedcoder.hangman.scene.menu;

import dev.glitchedcoder.hangman.Hangman;
import dev.glitchedcoder.hangman.entity.FixedTexture;
import dev.glitchedcoder.hangman.entity.LightFixture;
import dev.glitchedcoder.hangman.entity.Location;
import dev.glitchedcoder.hangman.entity.RenderPriority;
import dev.glitchedcoder.hangman.ui.NSFL;
import dev.glitchedcoder.hangman.ui.Texture;
import dev.glitchedcoder.hangman.ui.TexturePreprocessor;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;

@EqualsAndHashCode(callSuper = true)
public class MainMenu extends Menu {

    private final LightFixture light;
    private final FixedTexture hands;
    private final FixedTexture table;
    private final MenuComponent[] components;

    private static final byte SCALAR = 3;
    private static final byte COMPONENT_SIZE = 4;

    public MainMenu() {
        this.components = new MenuComponent[COMPONENT_SIZE];
        MenuComponent singlePlayer = new MenuComponent(this, "SINGLEPLAYER", SCALAR);
        MenuComponent multiPlayer = new MenuComponent(this, "MULTIPLAYER", SCALAR);
        MenuComponent preferences = new MenuComponent(this, "PREFERENCES", SCALAR);
        MenuComponent exit = new MenuComponent(this, "EXIT", SCALAR);
        singlePlayer.onSelect(() -> setScene(new SingleplayerMenu(this)));
        multiPlayer.onSelect(() -> setScene(new MultiplayerMenu(this)));
        preferences.onSelect(() -> setScene(new PreferencesMenu(this)));
        exit.onSelect(Hangman::exit);
        this.components[0] = singlePlayer;
        this.components[1] = multiPlayer;
        this.components[2] = preferences;
        this.components[3] = exit;
        this.light = new LightFixture(this, (byte) 10, 4.1);
        boolean nsfl = config.getNSFL() == NSFL.ON;
        BufferedImage hands = new TexturePreprocessor(nsfl ? Texture.HANDS_BOUND : Texture.HANDS_UNBOUND)
                .scale(4)
                .build();
        BufferedImage table = new TexturePreprocessor(Texture.TABLE_TEXTURE)
                .scale(4)
                .build();
        this.hands = new FixedTexture(this, hands);
        this.table = new FixedTexture(this, table);
        this.table.setRenderPriority(new RenderPriority(125));
        this.light.setRenderPriority(new RenderPriority(126));
        this.hands.setRenderPriority(RenderPriority.MAX);
    }

    @Nullable
    @Override
    protected Menu getParent() {
        return null;
    }

    @Override
    protected MenuComponent[] getComponents() {
        return this.components;
    }

    @Override
    protected byte getYOffset() {
        return 10;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        autoCenter();
        addRenderables(light, hands, table);
        hands.setLocation(Location.bottomCenter(hands.getBounds()));
        light.setLocation(Location.topCenter(light.getBounds()));
        table.setLocation(Location.bottomCenter(table.getBounds()));
        spawnAll(light, hands, table);
    }
}