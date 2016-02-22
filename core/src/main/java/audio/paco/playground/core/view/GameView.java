package audio.paco.playground.core.view;

import audio.paco.playground.core.PlayNGame;
import audio.paco.playground.core.model.Coordinate;
import audio.paco.playground.core.model.Piece;
import playn.core.Canvas;
import playn.core.Texture;
import playn.core.Tile;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import react.RMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameView extends GroupLayer {
    private final PlayNGame game;
    private final BoardView board;
    private final GroupLayer pieces = new GroupLayer();
    private final Tile[] pieceTiles = new Tile[Piece.values().length];
    private final Map<Coordinate, ImageLayer> pieceImages = new HashMap<>();
    private Canvas pieceCanvas;
    private float pieceSize;

    public GameView(PlayNGame game) {
        this.game = game;
        this.board = new BoardView(game);
        addCenterAt(board, game.size.width() / 2, game.size.height() / 2);
        addAt(pieces, board.tx(), board.ty());
        drawPieces();
        connectPieces();
    }

    private void drawPieces() {
        pieceSize = board.cellSize - 2;
        pieceCanvas = game.plat.graphics().createCanvas(2 * pieceSize, pieceSize);
        drawBlackPiece();
        drawWhitePiece();
        texturizePieces();
    }

    private void connectPieces() {
        game.pieces.connect(new RMap.Listener<Coordinate, Piece>() {
            @Override
            public void onPut(Coordinate c, Piece p) {
                setPiece(p, c);
            }

            @Override
            public void onRemove(Coordinate c) {
                clearPiece(c);
            }
        });
    }

    private void drawBlackPiece() {
        final float hsize = pieceSize / 2;
        pieceCanvas.setFillColor(Piece.BLACK.fillColor).fillCircle(hsize, hsize, hsize);
        pieceCanvas.setStrokeColor(Piece.BLACK.strokeColor).setStrokeWidth(2).strokeCircle(hsize, hsize, hsize);
    }

    private void drawWhitePiece() {
        final float hsize = pieceSize / 2;
        pieceCanvas.setFillColor(Piece.WHITE.fillColor).fillCircle(pieceSize + hsize, hsize, hsize);
        pieceCanvas.setStrokeColor(Piece.WHITE.strokeColor).setStrokeWidth(2).strokeCircle(pieceSize + hsize, hsize, hsize);
    }

    private void texturizePieces() {
        Texture pieceTexture = pieceCanvas.toTexture(Texture.Config.UNMANAGED);
        pieceTiles[Piece.BLACK.ordinal()] = pieceTexture.tile(0, 0, pieceSize, pieceSize);
        pieceTiles[Piece.WHITE.ordinal()] = pieceTexture.tile(pieceSize, 0, pieceSize, pieceSize);
        onDisposed(pieceTexture.disposeSlot());
    }

    private void setPiece(Piece piece, Coordinate at) {
        ImageLayer pieceView = pieceImages.get(at);
        if (pieceView == null) {
            pieceImages.put(at, addPiece(piece, at));
        } else {
            pieceView.setTile(pieceTiles[piece.ordinal()]);
        }
    }

    private void clearPiece(Coordinate at) {
        ImageLayer pieceView = pieceImages.remove(at);
        if (pieceView != null)
            pieceView.close();
    }

    private ImageLayer addPiece(Piece piece, Coordinate at) {
        ImageLayer pieceView = new ImageLayer(pieceTiles[piece.ordinal()]);
        pieceView.setOrigin(Origin.CENTER);
        pieces.addAt(pieceView, board.cellLocation(at.x), board.cellLocation(at.y));
        return pieceView;
    }

    public void showPlays(List<Coordinate> coordinates, Piece color) {
        final List<ImageLayer> plays = new ArrayList<>();
        for (Coordinate coordinate : coordinates) {
            ImageLayer pieceView = addPiece(color, coordinate);
            pieceView.setAlpha(0.3f);
            // TODO: Listen for click to make move
            plays.add(pieceView);
        }
    }

    @Override
    public void close() {
        super.close();
        pieceTiles[0].texture().close();
    }
}
