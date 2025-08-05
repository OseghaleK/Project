package Common;

import java.io.Serializable;

/**
 * UCID: oka
 * Date: 2025-07-25
 * Summary: For drawing a single pixel (x,y) with a color token.
 */
public class CoordPayload extends Payload implements Serializable {
    private static final long serialVersionUID = 1L;

    private int x;
    private int y;
    private String color;

    // client -> server (DRAW), server -> client (DRAW_SYNC)
    public CoordPayload(int x, int y, String color) {
        super(PayloadType.DRAW, "x=" + x + ",y=" + y + ",color=" + color);
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public String getColor() { return color; }

    @Override
    public String toString() {
        return "CoordPayload{type=" + getType() + ", x=" + x + ", y=" + y + ", color=" + color + "}";
    }
}





