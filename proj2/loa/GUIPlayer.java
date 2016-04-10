package loa;

/** A type of player that gets input from the mouse, and reports
 *  game positions and reports errors on a GUI.
 *  @author P. N. Hilfinger
 */
class GUIPlayer extends Game {

    /** A GUIPlayer that makes moves on GAME. */
    GUIPlayer(Game game) {
        super();
        _display = new LoaGUI("LoaGUI", this);
    }

    /** Displays the playing surface. */
    private LoaGUI _display;

}
