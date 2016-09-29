package loa;

import ucb.gui.TopLevel;
import ucb.gui.LayoutSpec;

import java.awt.event.MouseEvent;

/** A top-level GUI for Canfield solitaire.
 *  @author P. N. Hilfinger
 */
class LinesofActionGUI extends TopLevel {

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
    /** Saved value 290. */
    private static final int TWOHDRDNTY = 290;
    /** Saved value 375. */
    private static final int TREHDRDSVTYFVE = 375;
    /** Saved value 390. */
    private static final int TREHDRDNTY = 390;
    /** Saved value 5. */
    private static final int FVE = 5;
    /** Saved value 225. */
    private static final int TWOHDRDTWTYFVE = 225;

    /** A new window with given TITLE and displaying GAME. */
    LinesofActionGUI(String title, Game game) {
        super(title, true);
        _game = game;
        addButton("Quit", "quit", new LayoutSpec("y", ZERO, "x", 1));
        addButton("Undo", "undo", new LayoutSpec("y", ZERO, "x", 2));
        _display = new GameDisplay(game);
        add(_display, new LayoutSpec("y", 2, "width", 2));
        _display.setMouseHandler("click", this, "mouseClicked");
        _display.setMouseHandler("release", this, "mouseReleased");
        _display.setMouseHandler("drag", this, "mouseDragged");
        _display.setMouseHandler("press", this, "mousePressed");


        display(true);
    }

    /** Respond to "Quit" button. */
    public void quit(String dummy) {
        System.exit(1);
    }
    /** Respond to "Undo" button. */
    public void undo(String dummy) {
        _game.undo();
        _display.repaint();
    }

    /** Action in response to mouse-clicking event `EVENT. */
    public synchronized void mouseClicked(MouseEvent event) {
        int x = event.getX(), y = event.getY();
        if (x >= HDRD && x <= HDRDNTY && y >= TREHDRDETY && y <= FVEHDRDFVE) {
            _game.stockToWaste();
        }
        _display.repaint();

    }

     /** Saved x value. */
    private static int xsaved;
    /** Saved y value. */
    private static int ysaved;

    /** Action in response to mouse-pressing event `EVENT. */
    public synchronized void mousePressed(MouseEvent event) {

        xsaved = event.getX();
        ysaved = event.getY();

        _display.repaint();
    }
    /** Action in response to mouse-releasing event `EVENT. */
    public synchronized void mouseReleased(MouseEvent event) {
        int x = event.getX(), y = event.getY();
        if (insidereserve(x, y)) {
            _game.reserveToFoundation();
        }
        if (insidewaste(xsaved, ysaved)) {
            if (whereF(x, y) != ZERO) {
                _game.wasteToFoundation();
            }
        }
        if (insidewaste(xsaved, ysaved)) {
            if (whereT(x, y) != ZERO) {
                _game.wasteToTableau(whereT(x, y));
            }
        }
        if (insidereserve(xsaved, ysaved)) {
            if (whereT(x, y) != ZERO) {
                _game.reserveToTableau(whereT(x, y));
            }
        }
        if (whereT(xsaved, ysaved) != ZERO) {
            if (whereF(x, y) != ZERO) {
                _game.tableauToFoundation(whereT(xsaved, ysaved));
            }
        }
        if (whereF(xsaved, ysaved) != ZERO) {
            if (whereT(x, y) != ZERO) {
                _game.foundationToTableau(whereF(xsaved, ysaved),
                    whereT(x, y));
            }
        }
        if (whereT(xsaved, ysaved) != ZERO) {
            if (whereT(x, y) != ZERO) {
                if (whereT(xsaved, ysaved) != whereT(x, y)) {
                    _game.tableauToTableau(whereT(xsaved, ysaved),
                        whereT(x, y));
                }
            }
        }
        _display.repaint();
    }
    /** which tableau is it?
    @param x the x-coordinate, measured in pixels
    @param y the y-coordinate, measured in pixels
    @return z the tableau number given
    */
    public int whereT(int x, int y) {
        for (int t = TREHDRD, n = TREHDRDNTY, z = ONE; z < FVE;
             z++, t += HDRD, n += HDRD) {
            if (x >= t && x <= n && y >= TWOHDRDFTY && y <= TREHDRDSVTYFVE) {
                return z;
            }
        }
        return ZERO;
    }

    /** is it in a waste?
    @param x the x-coordinate, measured in pixels
    @param y the y-coordinate, measured in pixels
    @return True if inside the parameters
    */
    public boolean insidewaste(int x, int y) {
        return (x >= TWOHDRD && x <= TWOHDRDNTY
                && y >= TREHDRDETY && y <= FVEHDRDFVE);
    }
    /** is it in a reserve? @param: int x, int y
    @param x the x-coordinate, measured in pixels
    @param y the y-coordinate, measured in pixels
    @return True if inside the parameters
    */
    public boolean insidereserve(int x, int y) {
        return (x >= HDRD && x <= HDRDNTY
            && y >= TWOHDRDFTY && y <= TREHDRDSVTYFVE);
    }
    /** which foundation is it?
    @param x the x-coordinate, measured in pixels
    @param y the y-coordinate, measured in pixels
    @return True if inside the parameters
    */
    public int whereF(int x, int y) {
        for (int t = TREHDRD, n = TREHDRDNTY, z = ONE; z < FVE;
            z++, t += HDRD, n += HDRD) {
            if (x >= t && x <= n && y >= HDRD && y <= TWOHDRDTWTYFVE) {
                return z;
            }
        }
        return ZERO;
    }

    /** Action in response to mouse-dragging event EVENT. */
    public synchronized void mouseDragged(MouseEvent event) {
        _display.repaint();
    }

    /** The board widget. */
    private final GameDisplay _display;

    /** The game I am consulting. */
    private final Game _game;

}
