package loa;

import ucb.gui.TopLevel;
import ucb.gui.LayoutSpec;

import java.awt.event.MouseEvent;

/** A top-level GUI for Canfield solitaire.
 *  @author P. N. Hilfinger
 */
class LoaGUI extends TopLevel {


    /** A new window with given TITLE and displaying GAME. */
    LoaGUI(String title, Game game) {
        super(title, true);
        _game = game;
        addButton("Quit", "quit", new LayoutSpec("y", 0, "x", 1));
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



    /** Action in response to mouse-clicking event `EVENT. */
    public synchronized void mouseClicked(MouseEvent event) {
        int x = event.getX(), y = event.getY();



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

        _display.repaint();
    }

    /** The board widget. */
    private final GameDisplay _display;

    /** The game I am consulting. */
    private final Game _game;

}
