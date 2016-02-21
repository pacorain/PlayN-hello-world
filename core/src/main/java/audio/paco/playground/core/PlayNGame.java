package audio.paco.playground.core;

import audio.paco.playground.core.model.Coordinate;
import audio.paco.playground.core.model.Piece;
import audio.paco.playground.core.view.BoardView;
import audio.paco.playground.core.view.GameView;
import playn.core.Platform;
import playn.core.Surface;
import playn.scene.Layer;
import playn.scene.SceneGame;
import pythagoras.f.IDimension;
import react.RMap;
import react.Value;

public class PlayNGame extends SceneGame {

    public static final int BOARD_SIZE = 8;
    public final RMap<Coordinate, Piece> pieces = RMap.create();
    public final Value<Piece> turn = Value.create(null);

    public final IDimension size;

    public PlayNGame(Platform plat) {
        super(plat, 33);
        size = plat.graphics().viewSize;
        addElements();
        reset();
    }

    private void addElements() {
        addBackground();
        addBoard();
    }

    private void addBackground() {
        rootLayer.add(new Layer() {
            @Override
            protected void paintImpl(Surface surface) {
                surface.setFillColor(0xFFCCCCCC).fillRect(0, 0, size.width(), size.height());
            }
        });
    }

    private void addBoard() {
        rootLayer.add(new GameView(this));
    }

    private void reset() {
        pieces.clear();
        int half = BOARD_SIZE / 2;
        putWhiteAt(half - 1, half - 1);
        putBlackAt(half, half - 1);
        putBlackAt(half - 1, half);
        putWhiteAt(half, half);
    }

    private void putWhiteAt(int x, int y) {
        pieces.put(new Coordinate(x, y), Piece.WHITE);
    }

    private void putBlackAt(int x, int y) {
        pieces.put(new Coordinate(x, y), Piece.BLACK);
    }
}
