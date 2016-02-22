package audio.paco.playground.core;

import audio.paco.playground.core.model.Coordinate;
import audio.paco.playground.core.model.Logic;
import audio.paco.playground.core.model.Piece;
import audio.paco.playground.core.view.BoardView;
import audio.paco.playground.core.view.GameView;
import playn.core.Platform;
import playn.core.Surface;
import playn.scene.ImageLayer;
import playn.scene.Layer;
import playn.scene.SceneGame;
import pythagoras.f.IDimension;
import react.RMap;
import react.Slot;
import react.Value;

import java.util.ArrayList;
import java.util.List;

public class PlayNGame extends SceneGame {

    public static final int BOARD_SIZE = 8;
    public final RMap<Coordinate, Piece> pieces = RMap.create();
    public final Value<Piece> turn = Value.create(null);
    public final Logic logic = new Logic(this);
    public final IDimension size;

    private final GameView gameView;

    public PlayNGame(Platform plat) {
        super(plat, 33);
        size = plat.graphics().viewSize;
        gameView = new GameView(this);
        addElements();
        setup();
        reset();
    }

    private void addElements() {
        addBackground();
        addBoard();
    }

    private void setup() {
        setupTurns();
    }

    private void reset() {
        pieces.clear();
        int half = BOARD_SIZE / 2;
        putWhiteAt(half - 1, half - 1);
        putBlackAt(half, half - 1);
        putBlackAt(half - 1, half);
        putWhiteAt(half, half);
        turn.updateForce(Piece.BLACK);
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
        rootLayer.add(gameView);
    }

    private void setupTurns() {
        turn.connect(new Slot<Piece>() {
            private boolean lastPlayerPassed = false;

            @Override
            public void onEmit(Piece color) {
                List<Coordinate> plays = logic.legalPlays(pieces, color);
                if (!plays.isEmpty()) {
                    lastPlayerPassed = false;
                    gameView.showPlays(plays, color);
                } else if (lastPlayerPassed) {
                    endGame();
                } else {
                    lastPlayerPassed = true;
                    turn.update(color.next());
                }
            }
        });
    }

    private void putWhiteAt(int x, int y) {
        pieces.put(new Coordinate(x, y), Piece.WHITE);
    }

    private void putBlackAt(int x, int y) {
        pieces.put(new Coordinate(x, y), Piece.BLACK);
    }

    private void endGame() {
        // TODO
    }
}
