package audio.paco.playground.android;

import playn.android.GameActivity;

import audio.paco.playground.core.PlayNGame;

public class PlayNGameActivity extends GameActivity {

  @Override public void main () {
    new PlayNGame(platform());
  }
}
