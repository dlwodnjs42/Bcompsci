import ucb.gui.Pad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.imageio.ImageIO;

import java.io.InputStream;
import java.io.IOException;

class GameDisplay extends Pad {

	private static final Color BACKGROUND_COLOR = Color.white;
	private static final int BOARD_WIDTH = 800, BOARD_HEIGHT = 800;
	private static final int PIECE_HEIGHT = 125, PIECE_WIDTH = 90;
	public GameDisplay(Board board) {
        _board = board;
        setPreferredSize(BOARD_WIDTH, BOARD_HEIGHT);
    }

    //resources file inside loa
    private Image getImage(String name) {
        InputStream in =
            getClass().getResourceAsStream("/loa/resources/" + name);
        try {
            return ImageIO.read(in);
        } catch (IOException excp) {
            return null;
        }
    }
    //checkerpieces file inside resources inside loa
    private Image getPieceImage(Piece piece) {
        return getImage("checkerpieces/" + piece + ".png");
    }
    private void paintPieceImage(Graphics2D g, Piece piece, int x, int y) {
        if (card != null) {
            g.drawImage(getPieceImage(piece), x, y,
                        PIECE_WIDTH, PIECE_HEIGHT, null);
        }
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

    /** Board I am displaying. */
    private final Board _board;