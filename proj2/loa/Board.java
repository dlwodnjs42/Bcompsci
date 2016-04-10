
package loa;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Formatter;
import java.util.NoSuchElementException;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Direction.*;
import java.util.Iterator;

/** Represents the state of a game of Lines of Action.
 *  @author jae
 */
class Board implements Iterable<Move> {

    /** Size of a board. */
    static final int M = 8;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /** A Board whose initial contents are taken from INITIALCONTENTS
     *  and in which the player playing TURN is to move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row-1][col-1]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is MxM.
     *
     *  CAUTION: The natural written notation for arrays initializers puts
     *  the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /** A new board in the standard initial position. */
    Board() {
        clear();
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        copyFrom(board);
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        _moves.clear();

        for (int r = 1; r <= M; r += 1) {
            for (int c = 1; c <= M; c += 1) {
                set(c, r, contents[r - 1][c - 1]);
            }
        }
        _turn = side;
    }


    /** Set me to the initial configuration. */
    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        }
        _moves.clear();
        _moves.addAll(board._moves);
        _turn = board._turn;
        currentstate = board.currentstate;
    }

    /** Return the contents of column C, row R, where 1 <= C,R <= 8,
     *  where column 1 corresponds to column 'a' in the standard
     *  notation. */
    Piece get(int c, int r) {
        return currentstate[r - 1][c - 1];
    }

    /** Return the contents of the square SQ.  SQ must be the
     *  standard printed designation of a square (having the form cr,
     *  where c is a letter from a-h and r is a digit from 1-8). */
    Piece get(String sq) {
        return get(col(sq), row(sq));
    }

    /** Return the column number (a value in the range 1-8) for SQ.
     *  SQ is as for {@link get(String)}. */
    static int col(String sq) {
        if (!ROW_COL.matcher(sq).matches()) {
            throw new IllegalArgumentException("bad square designator");
        }
        return sq.charAt(0) - 'a' + 1;
    }

    /** Return the row number (a value in the range 1-8) for SQ.
     *  SQ is as for {@link get(String)}. */
    static int row(String sq) {
        if (!ROW_COL.matcher(sq).matches()) {
            throw new IllegalArgumentException("bad square designator");
        }
        return sq.charAt(1) - '0';
    }

    /** Set the square at column C, row R to V, and make NEXT the next side
     *  to move, if it is not null. */
    void set(int c, int r, Piece v, Piece next) {
        currentstate[r - 1][c - 1] = v;
        if (next != null) {
            _turn = next;
        }
    }

    /** Set the square at column C, row R to V. */
    void set(int c, int r, Piece v) {
        set(c, r, v, null);
    }

    /** Assuming isLegal(MOVE), make MOVE. */
    void makeMove(Move move) {
        assert isLegal(move);
        _moves.add(move);
        Piece replaced = move.replacedPiece();
        int c0 = move.getCol0(), c1 = move.getCol1();
        int r0 = move.getRow0(), r1 = move.getRow1();
        if (replaced != EMP) {
            set(c1, r1, EMP);
        }
        set(c1, r1, move.movedPiece());
        set(c0, r0, EMP);
        _turn = _turn.opposite();
    }


    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        Move move = _moves.remove(_moves.size() - 1);
        Piece replaced = move.replacedPiece();
        int c0 = move.getCol0(), c1 = move.getCol1();
        int r0 = move.getRow0(), r1 = move.getRow1();
        Piece movedPiece = move.movedPiece();
        set(c1, r1, replaced);
        set(c0, r0, movedPiece);
        _turn = _turn.opposite();
    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff MOVE is legal for the player currently on move. */
    boolean isLegal(Move move) {
        if (blocked(move) || pieceCountAlong(move) != move.length()) {
            return false;
        }
        return true;
    }

    /** Return a sequence of all legal moves from this position. */
    Iterator<Move> legalMoves() {
        return new MoveIterator();
    }

    @Override
    public Iterator<Move> iterator() {
        return legalMoves();
    }

    /** Return true if there is at least one legal move for the player
     *  on move. */
    public boolean isLegalMove() {
        return iterator().hasNext();
    }

    /** Return true iff either player has all his pieces continguous. */
    boolean gameOver() {
        return piecesContiguous(BP) || piecesContiguous(WP);
    }
    /** Return true iff SIDE's pieces are continguous. */
    int pieceContiguoussize(Piece side) {
        piecesContiguous(side);
        return _x;
    }
    /** Return the number of contiguous pieces.
     * @param side - side */
    int pieceContiguouscount(Piece side) {
        piecesContiguous(side);
        return _y;
    }




    /** Return true iff SIDE's pieces are continguous. */
    boolean piecesContiguous(Piece side) {

        HashSet<Integer> seen = new HashSet<Integer>();
        ArrayDeque<Integer> queue = new ArrayDeque<Integer>();
        int count = 0;
        for (int b = 1; b <= 8; b++) {
            for (int c = 1; c <= 8; c++) {
                if (get(b, c) == side) {
                    count++;
                }
            }
        }
        outerloop:
            for (int t = 1; t <= 8; t++) {
                for (int i = 1; i <= 8; i++) {
                    if (get(t, i) == side) {
                        seen.add(9 * t + i);
                        queue.add(9 * t + i);
                        break outerloop;

                    }
                }
            }
        while (!queue.isEmpty()) {
            int curr = queue.pop();
            int p = curr / 9, o = curr % 9;
            for (int x = p - 1; x < p + 2; x++) {
                for (int j = o - 1; j < o + 2; j++) {
                    if (!(x < 1 || x > 8 || j < 1 || j > 8)) {
                        if (get(x, j) == side) {
                            if (seen.add(9 * x + j)) {
                                queue.add(9 * x + j);
                            }
                        }
                    }
                }
            }
        }
        _x = seen.size();
        _y = count;
        if (count == seen.size()) {
            return true;
        }
        return false;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        for (int x = 1; x < b.currentstate.length; x++) {
            for (int t = 1; t < b.currentstate[x].length; t++) {
                if (!(b.currentstate[t - 1][x - 1]
                        == this.currentstate[t - 1][x - 1])) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int p = 0;

        int currentpieces = _t * Arrays.deepHashCode(currentstate);
        int moves = _t * _moves.hashCode();
        int turn = _t * _turn.hashCode();
        p = p + moves + turn + currentpieces;
        return p;
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = M; r >= 1; r -= 1) {
            out.format("    ");
            for (int c = 1; c <= M; c += 1) {
                out.format("%s ", get(c, r).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /** Finds the direction.
     * @param move - a move.
     * @return Direction. */
    public Direction finddirection(Move move) {
        int c0 = move.getCol0(), c1 = move.getCol1();
        int r0 = move.getRow0(), r1 = move.getRow1();
        int slopex = c1 - c0, slopey = r1 - r0;
        if (slopex == 0 && slopey == 0) {
            return NOWHERE;
        } else if (slopex == 0) {
            if (r1 > r0) {
                return N;
            } else {
                return S;
            }
        } else if (slopey == 0) {
            if (c1 > c0) {
                return E;
            } else {
                return W;
            }
        } else if (slopey / slopex == 1) {
            if (r1 > r0) {
                return NE;
            } else {
                return SW;
            }
        } else if (slopey / slopex == -1) {
            if (r1 > r0) {
                return NW;
            } else {
                return SE;
            }
        }
        return SE;
    }

    /** Return the number of pieces in the line of action indicated by MOVE. */
    public int pieceCountAlong(Move move) {
        Direction d = finddirection(move);
        return pieceCountAlong(move.getCol0(), move.getRow0(), d);
    }

    /** Call piece count along with coordinates.
     * @param c - column.
     * @param r - row.
     * @param dir - direction.
     * @return int. */
    public int getpieceCountAlong(int c, int r, Direction dir) {
        return pieceCountAlong(c, r, dir);
    }
    /** Call piece count along with move.
     * @param move - move.
     * @return int. */
    public int get1pieceCountAlong(Move move) {
        return pieceCountAlong(move);
    }
    /** Return the number of pieces in the line of action in direction DIR and
     *  containing the square at column C and row R. */
    private int pieceCountAlong(int c, int r, Direction dir) {
        int count = 0;
        if (dir == NOWHERE) {
            return 0;
        }
        int col = c + dir.dc;
        int ro = r + dir.dr;

        for (int column = col, row = ro; column <= 8 && row <= 8
                && column >= 1 && row >= 1; row += dir.dr, column += dir.dc) {

            if (get(column, row) == WP || get(column, row) == BP) {
                count++;
            }
        }
        for (int column = c, row = r; column <= 8 && row <= 8
                && column >= 1 && row >= 1; row -= dir.dr, column -= dir.dc) {
            if (get(column, row) == WP || get(column, row) == BP) {
                count++;
            }
        }
        return count;
    }
    /** @param move - a move.
     * @return move.
     * Calls blocked. */
    public boolean getblocked(Move move) {
        return blocked(move);
    }
    /** Return true iff MOVE is blocked by an opposing piece or by a
     *  friendly piece on the target square. */
    private boolean blocked(Move move) {
        int c = move.getCol0(), r = move.getRow0();
        int c1 = move.getCol1(), r1 = move.getRow1();
        Direction dir = finddirection(move);
        if (get(c, r).equals(get(c1, r1))) {
            return true;
        }
        for (int column = c, row = r; column != c1
                || row != r1; row += dir.dr, column += dir.dc) {
            if (get(column, row).fullName().equals
                    (turn().opposite().fullName())) {
                return true;
            }
        }
        return false;
    }
    /** The standard initial configuration for Lines of Action. */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };
    /** Game state of the board. */
    private Piece[][] currentstate = {
            { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /** Current side on move. */
    private Piece _turn;
    /** Arbitrary number. */
    private final int _t = 65;
    /** Count number of piecesContiguous. */
    private int _x;
    /** Count. */
    private int _y = 0;

    /** An iterator returning the legal moves from the current board. */
    private class MoveIterator implements Iterator<Move> {
        /**
         * Current piece under consideration.
         */
        private int _c, _r;
        /**
         * Next direction of current piece to return.
         */
        private Direction _dir;
        /**
         * Next move.
         */
        private Move _move;
        /**
         * To index.
         */
        private int q = 0;



        /**
         * A new move iterator for turn().
         */
        MoveIterator() {
            _c = 1;
            _r = 1;
            _dir = N;
            incr();
        }


        @Override
        public boolean hasNext() {
            return _move != null;
        }

        @Override
        public Move next() {
            if (_move == null) {
                throw new NoSuchElementException("no legal move");
            }
            Move move = _move;
            incr();
            return move;
        }

        @Override
        public void remove() {
        }

        /** Helper function for incr. */
        private void incrhelper() {
            _moves.clear();
            for (int i = 1; i <= 8; i++) {
                for (int t = 1; t <= 8; t++) {
                    _dir = N;
                    if (get(i, t).fullName().equals(_turn.fullName())) {
                        while (_dir != null) {
                            int distance = pieceCountAlong(i, t, _dir);
                            Move move = Move.create
                                    (i, t, distance, _dir, Board.this);
                            if (move != null) {
                                if (isLegal(move)) {
                                    _moves.add(move);
                                }
                            }
                            _dir = _dir.succ();
                        }
                    }
                }
            }
        }
        /** Advance to the next legal move. */
        private void incr() {
            incrhelper();
            if (q != _moves.size()) {
                _move = _moves.get(q);
                q++;
            } else {
                _move = null;
            }

        }
    }

}

