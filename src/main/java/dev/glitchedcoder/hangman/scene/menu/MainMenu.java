package dev.glitchedcoder.hangman.scene.menu;

import javax.annotation.Nullable;
import java.util.List;

public class MainMenu extends Menu {

    @Nullable
    @Override
    protected Menu getParent() {
        return null;
    }

    @Override
    protected List<MenuComponent> getComponents() {
        return null;
    }

    @Override
    protected void onUnload() {

    }
}