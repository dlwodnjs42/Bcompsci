
package loa;

import static loa.Piece.WP;
import java.util.HashSet;

/** An automated Player.
 *  @author Jae Lee */
class MachinePlayer extends Player {

    /**
     * A MachinePlayer that plays the SIDE pieces in GAME.
     */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
        _norepeatingmoves = new HashSet<>();
    }

    @Override
    Move makeMove() {
        int maxnumber = 0;
        Move thebestmove = null;
        for (Move move : getBoard()) {
            if (value(move) > maxnumber) {
                if (_norepeatingmoves.add(move)) {
                    if (_norepeatingmoves.size() == 6) {
                        _norepeatingmoves.clear();
                    }
                    thebestmove = move;
                    maxnumber = value(move);
                    if (value(thebestmove) >= cutoff) {
                        break;
                    }
                }
            }
        }
        if (getBoard().turn() == WP) {
            System.out.println("W::" + thebestmove);
        } else {
            System.out.println("B::" + thebestmove);
        }
        return thebestmove;
    }

    /** Returns some evaluation of a board state (good or bad).
     * @param board - board instance */
    public int eval(Board board) {

        if (board.piecesContiguous(board.turn())) {
            return Integer.MAX_VALUE;
        }
        int maxvalue = 0;
        for (int b = 1; b <= 8; b++) {
            for (int c = 1; c <= 8; c++) {
                if (board.get(c, b) == board.turn()) {
                    int bunchedgroups = board.pieceContiguoussize(board.turn());
                    if (bunchedgroups > maxvalue) {
                        maxvalue = bunchedgroups;
                    }

                }

            }
        }
        return maxvalue;
    }
    /** Returns some evaluation of a board state (good or bad).
     * @param move - move instance */
    public int value(Move move) {
        Board b = getBoard();
        b.makeMove(move);
        int maxvalue = eval(b);
        b.retract();
        return maxvalue;
    }

    /** No repeats. */
    private HashSet<Move> _norepeatingmoves;

    /** The cutoff. */
    private int cutoff = 5;
}

