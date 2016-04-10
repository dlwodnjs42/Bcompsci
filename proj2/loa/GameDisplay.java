package loa;

import ucb.gui.Pad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.imageio.ImageIO;

import java.io.InputStream;
import java.io.IOException;

/** A widget that displays a Pinball playfield.
 *  @author P. N. Hilfinger
 */
class GameDisplay extends Pad {

    /** Color of display field. */
    private static final Color BACKGROUND_COLOR = Color.green;

    /* Coordinates and lengths in pixels unless otherwise stated. */

    /** Preferred dimensions of the playing surface. */
    private static final int BOARD_WIDTH = 800, BOARD_HEIGHT = 800;

    /** Displayed dimensions of a card image. */
    private static final int PIECE_HEIGHT = 40, PIECE_WIDTH = 40;


    /** A graphical representation of GAME. */
    public GameDisplay(Game game) {
        _game = game;
        setPreferredSize(BOARD_WIDTH, BOARD_HEIGHT);
    }

    /** Return an Image read from the resource named NAME. */
    private Image getImage(String name) {
        InputStream in =
            getClass().getResourceAsStream("/loa/" + name);
        try {
            return ImageIO.read(in);
        } catch (IOException excp) {
            return null;
        }
    }

    /** Return an Image of CARD. */
    private Image getPieceImage(Piece piece) {
        return getImage(piece + ".png");
    }

    private Image getBoardImage() { return getImage("checkers_board.png");}

    private void paintBoard(Graphics2D g, int x, int y) {
            g.drawImage(getBoardImage(), x, y,
                    0, 0, null);
        }
    /** Draw CARD at X, Y on G. */
    private void paintCard(Graphics2D g, Piece piece, int x, int y) {
        if (piece != null) {
            g.drawImage(getPieceImage(piece), x, y,
                        0, 0, null);
        }
    }




    @Override
    public synchronized void paintComponent(Graphics2D g) {
        g.setColor(BACKGROUND_COLOR);
        (paintBoard(g, 0, 0)

    }

    /** Game I am displaying. */
    private final Game _game;

}
