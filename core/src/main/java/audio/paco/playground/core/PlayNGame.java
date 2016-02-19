package audio.paco.playground.core;

import audio.paco.playground.core.model.Coordinate;
import audio.paco.playground.core.model.Piece;
import audio.paco.playground.core.view.BoardView;
import playn.core.Image;
import playn.core.Platform;
import playn.core.Surface;
import playn.scene.ImageLayer;
import playn.scene.Layer;
import playn.scene.SceneGame;
import pythagoras.f.IDimension;
import react.RMap;
import react.Value;

public class PlayNGame extends SceneGame {

    public static final int BOARD_SIZE = 8;
    public final RMap<Coordinate, Piece> board = RMap.create();
    public final Value<Piece> turn = Value.create(null);
    private final IDimension size;

    public PlayNGame(Platform plat) {
        super(plat, 33);
        size = plat.graphics().viewSize;
        addElements();
    }

    private void addElements() {
        addBackground();
        addBoard();
    }

    private void addBackground() {
        rootLayer.add(new ColorLayer(0xFFCCCCCC));
    }

    private class ColorLayer extends Layer {
        private final int color;

        public ColorLayer(int color) {
            this.color = color;
        }

        @Override
        protected void paintImpl(Surface surface) {
            surface.setFillColor(color).fillRect(0, 0, size.width(), size.height());
        }
    }

    private void addBoard() {
        rootLayer.addCenterAt(new BoardView(this, size), size.width() / 2, size.height() / 2);
    }
}
