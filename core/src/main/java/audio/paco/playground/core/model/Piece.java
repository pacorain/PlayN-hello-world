package audio.paco.playground.core.model;

public enum Piece {
    BLACK(0xFF000000, 0xFFFFFFFF), WHITE(0xFFFFFFFF, 0xFF000000);

    public final int fillColor;
    public final int strokeColor;

    Piece(int fillColor, int strokeColor) {
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
    }
}
