package audio.paco.playground.core.model;

import audio.paco.playground.core.PlayNGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Logic {

    protected static final int[] DX = {-1, 0, 1, -1, 1, -1, 0, 1};
    protected static final int[] DY = {-1, -1, -1, 0, 0, 1, 1, 1};
    private PlayNGame game;

    public Logic(PlayNGame game) {
        this.game = game;
    }

    public boolean isLegalPlay(Piece color, Coordinate coordinate) {
        if (!inBounds(coordinate.x, coordinate.y) || game.pieces.containsKey(coordinate)) return false;

        // look in each direction from this piece; if we see the other piece color and then one of our
        // own, then this is a legal move
        for (int ii = 0; ii < DX.length; ii++) {
            boolean sawOther = false;
            int x = coordinate.x, y = coordinate.y;
            for (int dd = 0; dd < PlayNGame.BOARD_SIZE; dd++) {
                x += DX[ii];
                y += DY[ii];
                if (!inBounds(x, y)) break; // stop when we end up off the board
                Piece piece = game.pieces.get(new Coordinate(x, y));
                if (piece == null) break;
                else if (piece != color) sawOther = true;
                else if (sawOther) return true;
                else break;
            }
        }
        return false;
    }

    public List<Coordinate> legalPlays(Map<Coordinate, Piece> board, Piece color) {
        List<Coordinate> plays = new ArrayList<>();
        // search every board position for a legal move; the force, it's so brute!
        for (int yy = 0; yy < PlayNGame.BOARD_SIZE; yy++) {
            for (int xx = 0; xx < PlayNGame.BOARD_SIZE; xx++) {
                Coordinate coord = new Coordinate(xx, yy);
                if (board.containsKey(coord)) continue;
                if (isLegalPlay(color, coord)) plays.add(coord);
            }
        }
        return plays;
    }

    public void applyPlay(Map<Coordinate, Piece> board, Piece color, Coordinate coord) {
        List<Coordinate> toFlip = new ArrayList<>();
        // place this piece into the game state
        board.put(coord, color);
        // determine where this piece captures other pieces
        for (int ii = 0; ii < DX.length; ii++) {
            // look in this direction for captured pieces
            int x = coord.x, y = coord.y;
            for (int dd = 0; dd < PlayNGame.BOARD_SIZE; dd++) {
                x += DX[ii];
                y += DY[ii];
                if (!inBounds(x, y)) break; // stop when we end up off the board
                Coordinate fc = new Coordinate(x, y);
                Piece piece = board.get(fc);
                if (piece == null) break;
                else if (piece != color) toFlip.add(fc);
                else { // piece == color
                    for (Coordinate tf : toFlip) board.put(tf, color); // flip it!
                    break;
                }
            }
            toFlip.clear();
        }
    }

    private boolean inBounds(int x, int y) {
        return (x >= 0) && (x < PlayNGame.BOARD_SIZE) && (y >= 0) && (y < PlayNGame.BOARD_SIZE);
    }
}
