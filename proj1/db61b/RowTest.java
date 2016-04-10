package db61b;
import static org.junit.Assert.*;
import org.junit.Test;

/** @author Jae Lee */

public class RowTest {

    @Test
public void rowtesting() {
        Row row1 = new Row(new String[]
        {"pikachu", "raichu", "gyarados", "weedle"});
        assertEquals(row1.size(), 4);

        Row row2 = new Row(new String[]
        {"pidgey", "raichu", "gyarados", "weedle"});
        assertEquals(row2.get(2), "gyarados");
    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(RowTest.class));
    }
}
