package loa;

import java.util.Iterator;
import static loa.Piece.BP;
import static loa.Piece.EMP;
import static loa.Piece.WP;
import static loa.Direction.*;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Created by J1 on 11/12/15.
 */

public class TestingBoard {
    @Test
    public void testget() {
        Piece[][] one = {
                {EMP, BP, BP, BP, BP, BP, BP, EMP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {EMP, BP, BP, BP, BP, BP, BP, EMP}
        };
        Board testt = new Board(one, BP);
        assertEquals(EMP, testt.get("a1"));
        assertEquals(WP, testt.get("a6"));
    }
    @Test
    public void piecescontiguous() {
        Piece[][] one = {
                {EMP, BP, BP, BP, BP, BP, BP, EMP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {EMP, BP, BP, BP, BP, BP, BP, EMP}
        };
        Piece[][] two = {
                {EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP},
                {WP, EMP, EMP, BP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, BP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, BP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, BP, EMP, EMP, WP},
                {WP, EMP, EMP, BP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, BP, EMP, EMP, EMP, WP},
                {EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP}
        };
        Piece[][] three = {
                {EMP, BP, BP, EMP, BP, BP, BP, EMP},
                {WP, BP, EMP, BP, EMP, EMP, EMP, WP},
                {WP, BP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, BP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP}
        };
        Piece[][] four = {
                {EMP, WP, WP, EMP, EMP, BP, EMP, BP},
                {EMP, EMP, EMP, EMP, WP, EMP, BP, WP},
                {EMP, EMP, EMP, EMP, WP, EMP, BP, WP},
                {EMP, EMP, EMP, WP, EMP, EMP, BP, BP},
                {EMP, EMP, EMP, EMP, EMP, BP, EMP, WP},
                {EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP},
                {EMP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {EMP, EMP, EMP, EMP, EMP, EMP, BP, EMP}
        };
        Board testt = new Board(one, BP);
        Board testt2 = new Board(two, BP);
        Board testt3 = new Board(three, BP);
        Board testt4 = new Board(four, BP);
        assertEquals(testt.piecesContiguous(BP), false);
        assertEquals(testt2.piecesContiguous(BP), true);
        assertEquals(testt3.piecesContiguous(BP), false);
        assertEquals(testt4.piecesContiguous(BP), false);
        assertEquals(testt4.getpieceCountAlong(7, 1, N), 4);
        assertEquals(testt4.pieceContiguoussize(BP), 7);
        assertEquals(testt4.pieceContiguouscount(BP), 8);
    }

    @Test
    public void hashCodeTest() {
        Piece[][] one = {
                {EMP, BP, BP, BP, BP, BP, BP, EMP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {EMP, BP, BP, BP, BP, BP, BP, EMP}
        };
        Piece[][] two = {
                {EMP, BP, BP, BP, BP, BP, BP, EMP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {EMP, BP, BP, BP, BP, BP, BP, EMP}
        };
        Piece[][] three = {
                {EMP, BP, BP, BP, BP, BP, BP, EMP},
                {WP, BP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {EMP, BP, BP, BP, BP, BP, BP, EMP}
        };
        Board testt = new Board(one, BP);
        Board testt2 = new Board(two, BP);
        Board testt3 = new Board(three, BP);
        int x = testt.hashCode();
        int t = testt2.hashCode();
        assertEquals(x, t);
        assertEquals(testt.equals(testt2), true);
        assertEquals(testt.equals(testt3), false);
    }
    @Test
    public void islegaltest() {
        Piece[][] one = {
                {EMP, BP, BP, BP, BP, BP, BP, EMP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, WP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, WP, EMP, EMP, WP},
                {WP, EMP, EMP, BP, EMP, EMP, EMP, WP},
                {WP, EMP, BP, BP, EMP, EMP, EMP, WP},
                {EMP, BP, BP, WP, BP, BP, BP, EMP}
        };
        Board testt = new Board(one, BP);
        Move t = Move.movegetter(3, 8, 6, 5, BP, EMP);
        assertEquals(testt.isLegal(t), true);
    }
    @Test
    public void blockedtestdiagonals() {
        Piece[][] one = {
                {EMP, BP, BP, BP, BP, BP, BP, EMP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, WP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, WP, EMP, EMP, WP},
                {WP, EMP, EMP, BP, EMP, EMP, EMP, WP},
                {WP, EMP, BP, BP, EMP, EMP, EMP, WP},
                {EMP, BP, BP, WP, BP, BP, BP, EMP}
        };
        Board testt = new Board(one, BP);
        Move t = Move.movegetter(3, 8, 6, 5, BP, EMP);
        assertEquals(testt.getblocked(t), false);

        Move t2 = Move.movegetter(2, 8, 7, 3, BP, EMP);
        assertEquals(testt.getblocked(t2), true);
        Piece[][] two = {
                {BP, BP, BP, BP, BP, BP, BP, EMP},
                {WP, BP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, WP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, WP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, BP, EMP, EMP, WP},
                {WP, EMP, EMP, BP, EMP, EMP, EMP, WP},
                {WP, EMP, BP, BP, EMP, EMP, EMP, WP},
                {EMP, BP, BP, WP, BP, BP, BP, EMP}
        };
        Board testt2 = new Board(two, BP);
        Move t3 = Move.movegetter(3, 7, 8, 2, BP, EMP);
        assertEquals(testt2.getblocked(t3), false);

        Move t4 = Move.movegetter(5, 5, 1, 1, BP, EMP);
        assertEquals(testt2.getblocked(t4), true);




    }

    @Test
    public void blockedtest() {
        Piece[][] one = {
                {EMP, BP, BP, BP, BP, BP, BP, EMP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, WP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, BP, EMP, EMP, EMP, WP},
                {EMP, BP, BP, WP, BP, BP, BP, EMP}
        };
        Piece[][] two = {
                {EMP, BP, BP, EMP, BP, BP, BP, EMP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, BP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, WP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, BP, EMP, EMP, EMP, WP},
                {EMP, BP, BP, WP, BP, BP, BP, EMP}
        };
        Piece[][] three = {
                {EMP, BP, BP, EMP, BP, BP, BP, EMP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, BP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, WP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, WP, EMP, EMP, EMP, WP},
                {EMP, BP, BP, WP, BP, BP, BP, EMP}
        };
        Board testt = new Board(one, WP);
        Board testt2 = new Board(two, WP);
        Board testt3 = new Board(three, WP);
        Move t = Move.movegetter(4, 8, 4, 4, WP, WP);
        Move t2 = Move.movegetter(4, 8, 4, 4, WP, BP);
        Move t3 = Move.movegetter(4, 8, 4, 4, WP, BP);
        Move t4 = Move.movegetter(8, 4, 4, 4, WP, BP);
        Move t5 = Move.movegetter(4, 4, 4, 8, BP, WP);
        assertEquals(true, testt.getblocked(t));
        assertEquals(true, testt2.getblocked(t2));
        assertEquals(testt3.getblocked(t4), false);
        assertEquals(testt3.getblocked(t5), true);
        assertEquals(testt3.getblocked(t3), false);



    }
    @Test
    public void pieceCountAlongDirTest() {
        Piece[][] one = {
                {BP, BP, BP, BP, BP, BP, BP, EMP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, BP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {BP, BP, EMP, BP, BP, BP, BP, EMP}
        };
        Direction d = Direction.N;
        Board testt = new Board(one, BP);
        assertEquals(testt.getpieceCountAlong(1, 1, d), 8);
        assertEquals(testt.getpieceCountAlong(2, 1, d), 2);

        Piece[][] two = {
                {EMP, WP, EMP, EMP, EMP, EMP, EMP, EMP},
                {EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {EMP, BP, EMP, EMP, EMP, EMP, EMP, EMP},
        };
        Move p = Move.movegetter(2, 8, 2, 1, BP, EMP);
        Board testt2 = new Board(two, BP);
        assertEquals(testt2.getpieceCountAlong(2, 1, N), 2);
    }

    @Test
    public void pieceCountAlongTest() {
        Piece[][] one = {
                {BP, BP, BP, BP, BP, BP, BP, EMP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {BP, BP, EMP, BP, BP, BP, BP, EMP}
        };
        Move t = Move.movegetter(3, 3, 6, 3, BP, EMP);
        Move x = Move.movegetter(1, 8, 2, 7, BP, EMP);
        Move o = Move.movegetter(6, 8, 1, 3, BP, WP);
        Move q = Move.movegetter(7, 1, 4, 1, BP, WP);

        Board testt = new Board(one, BP);

        assertEquals(testt.get1pieceCountAlong(x), 1);
        assertEquals(testt.get1pieceCountAlong(o), 2);

        assertEquals(testt.get1pieceCountAlong(q), 7);
        assertEquals(testt.get1pieceCountAlong(t), 2);

        Piece[][] two = {
                {EMP, WP, EMP, EMP, EMP, EMP, EMP, EMP},
                {EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {EMP, BP, EMP, EMP, EMP, EMP, EMP, EMP},
        };
        Move p = Move.movegetter(2, 8, 2, 1, BP, EMP);

        Board testt2 = new Board(two, BP);
        assertEquals(testt2.pieceCountAlong(p), 2);
    }
    @Test
    public void incrTesting() {
        Piece[][] one = {
                {EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {EMP, EMP, EMP, EMP, EMP, EMP, BP, EMP},
        };
        Piece[][] two = {
                {EMP, BP, EMP, EMP, EMP, EMP, EMP, EMP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP},
        };
        Piece[][] three = {
                {EMP, BP, BP, BP, BP, BP, BP, EMP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
                {EMP, BP, BP, BP, BP, BP, BP, EMP},
        };

        Board a = new Board(one, BP);
        Board b = new Board(two, BP);
        Board c = new Board(three, BP);
        Iterator allmoves = c.iterator();
        int count = 0;
        while (allmoves.hasNext()) {
            count++;
            allmoves.next();
        }
        Iterator pieces = b.iterator();
        int p = 0;
        while (pieces.hasNext()) {
            p++;
            pieces.next();
        }
        Iterator layers = a.iterator();
        int q = 0;
        while (layers.hasNext()) {
            q++;
            layers.next();
        }
        assertEquals(p, 4);
        assertEquals(count, 36);
        assertEquals(q, 4);
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Board.class));
    }
}

