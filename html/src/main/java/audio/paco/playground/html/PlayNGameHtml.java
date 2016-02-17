package audio.paco.playground.html;

import com.google.gwt.core.client.EntryPoint;
import playn.html.HtmlPlatform;
import audio.paco.playground.core.PlayNGame;

public class PlayNGameHtml implements EntryPoint {

  @Override public void onModuleLoad () {
    HtmlPlatform.Config config = new HtmlPlatform.Config();
    // use config to customize the HTML platform, if needed
    HtmlPlatform plat = new HtmlPlatform(config);
    plat.assets().setPathPrefix("hello-world-playn/");
    new PlayNGame(plat);
    plat.start();
  }
}
