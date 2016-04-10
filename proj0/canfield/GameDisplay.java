package canfield;

import ucb.gui.Pad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

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
    private static final int BOARD_WIDTH = 800, BOARD_HEIGHT = 600;

    /** Displayed dimensions of a card image. */
    private static final int CARD_HEIGHT = 125, CARD_WIDTH = 90;

    /** Saved value 100. */
    private static final int HDRD = 100;

    /** Saved value 190. */
    private static final int HDRDNTY = 190;

    /** Saved value 200. */
    private static final int TWOHDRD = 200;

    /** Saved value 300. */
    private static final int TREHDRD = 300;

    /** Saved value 0. */
    private static final int ZERO = 0;

    /** Saved value 505. */
    private static final int FVEHDRDFVE = 505;

    /** Saved value 380. */
    private static final int TREHDRDETY = 380;

    /** Saved value 1. */
    private static final int ONE = 1;

    /** Saved value 250. */
    private static final int TWOHDRDFTY = 250;

    /** Saved value 20. */
    private static final int TWTY = 20;

    /** Saved value 375. */
    private static final int TREHDRDSVTYFVE = 375;

    /** Saved value 390. */
    private static final int TREHDRDNTY = 390;

    /** Saved value 5. */
    private static final int FVE = 5;

    /** Saved value 225. */
    private static final int TWOHDRDTWTYFVE = 225;

    /** A graphical representation of GAME. */
    public GameDisplay(Game game) {
        _game = game;
        setPreferredSize(BOARD_WIDTH, BOARD_HEIGHT);
    }

    /** Return an Image read from the resource named NAME. */
    private Image getImage(String name) {
        InputStream in =
            getClass().getResourceAsStream("/canfield/resources/" + name);
        try {
            return ImageIO.read(in);
        } catch (IOException excp) {
            return null;
        }
    }

    /** Return an Image of CARD. */
    private Image getCardImage(Card card) {
        return getImage("playing-cards/" + card + ".png");
    }

    /** Return an Image of the back of a card. */
    private Image getBackImage() {
        return getImage("playing-cards/blue-back.png");
    }

    /** Draw CARD at X, Y on G. */
    private void paintCard(Graphics2D g, Card card, int x, int y) {
        if (card != null) {
            g.drawImage(getCardImage(card), x, y,
                        CARD_WIDTH, CARD_HEIGHT, null);
        }
    }

    /** Draw card back at X, Y on G. */
    private void paintBack(Graphics2D g, int x, int y) {
        g.drawImage(getBackImage(), x, y, CARD_WIDTH, CARD_HEIGHT, null);
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {
        g.setColor(BACKGROUND_COLOR);
        Rectangle b = g.getClipBounds();
        g.fillRect(ZERO, ZERO, b.width, b.height);
        g.setColor(Color.black);
        int k = ONE;
        int x = TREHDRD;
        for (; k < FVE; k++, x += 100) {
            int i = _game.tableauSize(k) - ONE;
            int t = TWOHDRDFTY;
            for (; i >= ZERO; i--) {
                paintCard(g, _game.getTableau(k, i), x, t);
                g.drawRect(x, t, CARD_WIDTH, CARD_HEIGHT);
                t += TWTY;
            }
        }
        int t = TREHDRD;
        for (k = ONE; k < FVE; k++) {
            paintCard(g, _game.topFoundation(k), t, 100);
            g.drawRect(t, 100, CARD_WIDTH, CARD_HEIGHT);
            t += 100;
        }
        paintCard(g, _game.topWaste(), TWOHDRD, TREHDRDETY);
        g.drawRect(TWOHDRD, TREHDRDETY, CARD_WIDTH, CARD_HEIGHT);
        paintCard(g, _game.topReserve(), HDRD, TWOHDRDFTY);
        g.drawRect(HDRD, TWOHDRDFTY, CARD_WIDTH, CARD_HEIGHT);
        if (_game.stockEmpty()) {
            g.drawRect(HDRD, TREHDRDETY, CARD_WIDTH, CARD_HEIGHT);
        } else {
            paintBack(g, HDRD, TREHDRDETY);
        }
    }

    /** Game I am displaying. */
    private final Game _game;

}
