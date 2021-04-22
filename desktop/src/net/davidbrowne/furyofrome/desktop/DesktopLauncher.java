package net.davidbrowne.furyofrome.desktop;

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
		new LwjglApplication(new Game(), config);
	}
}
