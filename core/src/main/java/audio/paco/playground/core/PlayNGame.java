package audio.paco.playground.core;

import audio.paco.playground.core.model.Coordinate;
import audio.paco.playground.core.model.Piece;
import playn.core.Image;
import playn.core.Platform;
import playn.scene.ImageLayer;
import playn.scene.SceneGame;
import react.RMap;
import react.Value;

public class PlayNGame extends SceneGame {

    public static final int BOARD_SIZE = 8;
    public final RMap<Coordinate, Piece> board = RMap.create();
    public final Value<Piece> turn = Value.create(null);

    public PlayNGame(Platform plat) {
        super(plat, 33); // update our "simulation" 33ms (30 times per second)

        // create and add background image layer
        Image bgImage = plat.assets().getImage("images/bg.png");
        ImageLayer bgLayer = new ImageLayer(bgImage);
        // scale the background to fill the screen
        bgLayer.setSize(plat.graphics().viewSize);
        rootLayer.add(bgLayer);
    }
}
