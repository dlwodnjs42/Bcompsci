package db61b;
import static org.junit.Assert.*;

import org.junit.Test;


public class TableTest {

    @Test
public void tabletesting() {
        Row row1 = new Row(new String[]
        {"pidgeotto", "pikachu", "gyarados", "weedle"});
        assertEquals(row1.size(), 4);

        Row row2 = new Row(new String[]
        {"pidgey", "raichu", "magickarp", "butterfree"});
        assertEquals(row2.get(2), "gyarados");

        Table table1 = new Table("pokemon", new String[]
        {"flying", "thunder", "water", "bug"});
        table1.add(row1);
        table1.add(row2);

        assertEquals(table1.size(), 2);
        assertEquals(table1.columnIndex("flying"), 0);
        assertEquals(table1.columnIndex("steel"), -1);


    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(RowTest.class));
    }
}
