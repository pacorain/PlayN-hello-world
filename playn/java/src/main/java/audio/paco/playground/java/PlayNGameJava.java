package audio.paco.playground.java;

import playn.java.LWJGLPlatform;

import audio.paco.playground.core.PlayNGame;

public class PlayNGameJava {

  public static void main (String[] args) {
    LWJGLPlatform.Config config = new LWJGLPlatform.Config();
    // use config to customize the Java platform, if needed
    LWJGLPlatform plat = new LWJGLPlatform(config);
    new PlayNGame(plat);
    plat.start();
  }
}
