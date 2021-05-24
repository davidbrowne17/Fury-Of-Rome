package net.davidbrowne.furyofrome.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.davidbrowne.furyofrome.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable=true;
		config.height=900;
		config.width=1200;
		config.title="Fury Of Rome";
		config.addIcon("icon.png", Files.FileType.Internal);
		new LwjglApplication(new Game(), config);
	}
}
