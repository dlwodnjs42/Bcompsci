package db61b;

/** @author Jae Lee */

public class Tablereadtest {

    /** Checks table methods.
    @param args */
    public static void main(String[] args) {

        Row row1 = new Row(new String[]
        {"pidgeotto", "pikachu", "gyarados", "weedle"});

        Row row2 = new Row(new String[]
        {"pidgey", "raichu", "magikarp", "butterfree"});

        Table table1 = new Table("pokemon", new String[]
        {"flying", "thunder", "water", "bug"});
        table1.add(row1);

        table1.add(row2);
        table1.writeTable("pokemon");
        Table.readTable("pokemon").print();



    }
}
