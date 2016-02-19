package audio.paco.playground.core.view;

import audio.paco.playground.core.PlayNGame;
import playn.core.Surface;
import playn.scene.Layer;
import pythagoras.f.IDimension;

public class BoardView extends Layer {

    private static final int LINE_WIDTH = 2;
    private static final int LINE_COLOR = 0xFF000000;
    private final PlayNGame game;
    public final float cellSize;

    public BoardView(PlayNGame game, IDimension viewSize) {
        this.game = game;
        float maxBoardSize = Math.min(viewSize.width(), viewSize.height()) - 20.0f;
        cellSize = (float) Math.floor(maxBoardSize / PlayNGame.BOARD_SIZE);
    }

    @Override
    public float width() {
        return windowSize();
    }

    @Override
    public float height() {
        return windowSize();
    }

    private float windowSize() {
        return cellSize * PlayNGame.BOARD_SIZE + LINE_WIDTH;
    }

    @Override
    protected void paintImpl(Surface surface) {
        surface.setFillColor(LINE_COLOR);
        drawVerticalGridLines(surface);
        drawHorizontalGridLines(surface);
    }

    private void drawVerticalGridLines(Surface surface) {
        for (int xx = 0; xx <= PlayNGame.BOARD_SIZE; xx++) {
            float xpos = xx * cellSize + 1;
            surface.drawLine(xpos, 0, xpos, height(), LINE_WIDTH);
        }
    }

    private void drawHorizontalGridLines(Surface surface) {
        for (int yy = 0; yy <= PlayNGame.BOARD_SIZE; yy++) {
            float ypos = yy * cellSize + 1;
            surface.drawLine(0, ypos, width(), ypos, LINE_WIDTH);
        }
    }
}
