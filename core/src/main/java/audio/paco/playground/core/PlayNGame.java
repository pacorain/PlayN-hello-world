package audio.paco.playground.core;

import audio.paco.playground.core.model.Coordinate;
import audio.paco.playground.core.model.Logic;
import audio.paco.playground.core.model.Piece;
import audio.paco.playground.core.view.GameView;
import playn.core.*;
import playn.scene.*;
import playn.scene.Mouse;
import playn.scene.Pointer;
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
    public final Pointer pointer;
    public final IDimension size;

    private final GameView gameView;
    private ImageLayer textImageLayer;

    public PlayNGame(Platform plat) {
        super(plat, 33);
        size = plat.graphics().viewSize;
        gameView = new GameView(this);
        pointer = new Pointer(plat, rootLayer, false);
        addElements();
        setup();
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
        rootLayer.add(gameView);
    }

    private void setup() {
        setupTurns();
        setupInput();
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

    private void setupInput() {
        plat.input().mouseEvents.connect(new Mouse.Dispatcher(rootLayer, false));
    }

    private void endGame() {
        List<Piece> winners = calculateWinners();
        String gameEndMessage = generateGameEndMessage(winners);
        renderEndText(gameEndMessage);
        registerClickEvent();
    }

    private List<Piece> calculateWinners() {
        Piece[] pieces = Piece.values();
        int[] pieceCount = countPieces();
        List<Piece> winners = new ArrayList<>();
        int highScore = 0;
        for (int pieceNumber = 0; pieceNumber < pieceCount.length; pieceNumber++) {
            int score = pieceCount[pieceNumber];
            if (score == highScore) {
                winners.add(pieces[pieceNumber]);
            } else if (score > highScore) {
                winners.clear();
                winners.add(pieces[pieceNumber]);
                highScore = score;
            }
        }
        return winners;
    }

    private int[] countPieces() {
        int[] pieceCount = new int[Piece.values().length];
        for (Piece piece : pieces.values()) ++pieceCount[piece.ordinal()];
        return pieceCount;
    }

    private String generateGameEndMessage(List<Piece> winners) {
        StringBuilder msg = new StringBuilder();
        if (winners.size() == 1) msg.append(winners.get(0)).append(" wins!");
        else {
            for (Piece piece : winners) {
                if (msg.length() > 0) msg.append(" and ");
                msg.append(piece);
            }
            msg.append(" tie.");
        }
        msg.append("\nClick to play again.");
        return msg.toString();
    }

    private void renderEndText(String message) {
        TextBlock text = new TextBlock(
                plat.graphics().layoutText(message,
                        new TextFormat(new Font("Helvitica", Font.Style.BOLD, 48)),
                        new TextWrap(size.width() - 20)));
        Canvas canvas = plat.graphics().createCanvas(text.bounds.width() + 4, text.bounds.height() + 4);
        canvas.setFillColor(0xFF0000FF).setStrokeColor(0xFFFFFFFF).setStrokeWidth(4f);
        text.stroke(canvas, TextBlock.Align.CENTER, 2, 2);
        text.fill(canvas, TextBlock.Align.CENTER, 2, 2);
        textImageLayer = new ImageLayer(canvas.toTexture());
        rootLayer.addFloorAt(textImageLayer, (size.width() - canvas.width) / 2, (size.height() - canvas.height) / 2);
    }

    private void registerClickEvent() {
        pointer.events.connect(new Slot<playn.core.Pointer.Event>() {
            @Override
            public void onEmit(playn.core.Pointer.Event event) {
                if (event.kind.isStart) {
                    textImageLayer.close();
                    reset();
                    pointer.events.disconnect(this);
                }
            }
        });
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

    private void putWhiteAt(int x, int y) {
        pieces.put(new Coordinate(x, y), Piece.WHITE);
    }

    private void putBlackAt(int x, int y) {
        pieces.put(new Coordinate(x, y), Piece.BLACK);
    }
}
