package audio.paco.playground.core.view;

import audio.paco.playground.core.PlayNGame;
import playn.core.Surface;
import playn.scene.Layer;

public class BoardView extends Layer {

    private static final int LINE_WIDTH = 2;
    private static final int LINE_COLOR = 0xFF000000;
    public final float cellSize;

    public BoardView(PlayNGame game) {
        float maxBoardSize = Math.min(game.size.width(), game.size.height()) - 20.0f;
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
        for (int cellX = 0; cellX <= PlayNGame.BOARD_SIZE; cellX++) {
            float xpos = cellX * cellSize + 1;
            surface.drawLine(xpos, 0, xpos, height(), LINE_WIDTH);
        }
    }

    private void drawHorizontalGridLines(Surface surface) {
        for (int cellY = 0; cellY <= PlayNGame.BOARD_SIZE; cellY++) {
            float ypos = cellY * cellSize + 1;
            surface.drawLine(0, ypos, width(), ypos, LINE_WIDTH);
        }
    }
}
