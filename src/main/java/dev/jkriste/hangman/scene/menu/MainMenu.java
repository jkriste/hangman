package dev.jkriste.hangman.scene.menu;

import dev.jkriste.hangman.Hangman;
import dev.jkriste.hangman.entity.FixedTexture;
import dev.jkriste.hangman.entity.LightFixture;
import dev.jkriste.hangman.entity.Location;
import dev.jkriste.hangman.entity.RenderPriority;
import dev.jkriste.hangman.json.Strings;
import dev.jkriste.hangman.ui.NSFL;
import dev.jkriste.hangman.ui.Texture;
import dev.jkriste.hangman.ui.TexturePreprocessor;
import dev.jkriste.hangman.util.Constants;
import dev.jkriste.hangman.window.Scene;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;

@EqualsAndHashCode(callSuper = true)
public class MainMenu extends Menu {

    private final LightFixture light;
    private final FixedTexture hands;
    private final FixedTexture table;
    private final FixedTexture version;
    private final MenuComponent[] components;

    private static final byte SCALAR = 3;
    private static final byte COMPONENT_SIZE = 4;

    public MainMenu() {
        this.components = new MenuComponent[COMPONENT_SIZE];
        this.light = new LightFixture(this, (byte) 10, 4.1);
        boolean nsfl = Scene.config.getNSFL() == NSFL.ON;
        BufferedImage hands = new TexturePreprocessor(nsfl ? Texture.HANDS_BOUND : Texture.HANDS_UNBOUND)
                .scale(4)
                .build();
        BufferedImage table = new TexturePreprocessor(Texture.TABLE_TEXTURE)
                .scale(4)
                .build();
        BufferedImage version = new TexturePreprocessor("v" + Constants.VERSION)
                .removeBackground()
                .scale(1.4)
                .build();
        this.hands = new FixedTexture(this, hands);
        this.table = new FixedTexture(this, table);
        this.version = new FixedTexture(this, version);
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
    protected byte getComponentSpacing() {
        return 10;
    }

    @Override
    protected void onInit() {
        MenuComponent singlePlayer = new MenuComponent(this, Strings.SINGLEPLAYER, SCALAR);
        MenuComponent multiPlayer = new MenuComponent(this, Strings.MULTIPLAYER, SCALAR);
        MenuComponent preferences = new MenuComponent(this, Strings.PREFERENCES, SCALAR);
        MenuComponent exit = new MenuComponent(this, Strings.MENU_EXIT, SCALAR);
        singlePlayer.onSelect(() -> setScene(new SingleplayerMenu(this), false));
        multiPlayer.onSelect(() -> setScene(new MultiplayerMenu(this), false));
        preferences.onSelect(() -> setScene(new PreferencesMenu(this), false));
        exit.onSelect(Hangman::exit);
        this.components[0] = singlePlayer;
        this.components[1] = multiPlayer;
        this.components[2] = preferences;
        this.components[3] = exit;
        super.onInit();
        autoCenter();
        this.table.setRenderPriority(new RenderPriority(125));
        this.light.setRenderPriority(new RenderPriority(126));
        this.hands.setRenderPriority(RenderPriority.MAX);
        addRenderables(light, hands, table, version);
        hands.setLocation(Location::bottomCenter);
        light.setLocation(Location::topCenter);
        table.setLocation(Location::bottomCenter);
        version.setLocation(Location::bottomRight);
        spawnAll(light, hands, table, version);
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        disposeAll(table, light, hands, version);
    }
}