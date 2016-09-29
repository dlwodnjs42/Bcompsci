package gitlet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Driver class for Gitlet, the tiny stupid version-control system.
 *
 * @author Isabel Zhang & Jae Won Lee
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND> ....
     *
     */

    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            new Error("Please enter a command.");
            System.exit(0);
        }
        switch (args[0]) {
            case "init":
                initCase(args);
                break;
            case "add":
                addCase(args);
                break;
            case "commit":
                commitCase(args);
                break;
            case "log":
                logCase(args);
                break;
            case "rm":
                rmCase(args);
                break;
            case "global-log":
                globalCase(args);
                break;
            case "find":
                findCase(args);
                break;
            case "status":
                statusCase(args);
                break;
            case "checkout":
                checkoutCases(args);
                break;
            case "branch":
                branchCase(args);
                break;
            case "rm-branch":
                rmBranchCase(args);
                break;
            case "reset":
                resetCase(args);
                break;
            case "merge":
                mergeCase(args);
                break;
            default:
                System.out.println("No command with that name exists.");
                new Error("No command with that name exists.");
                break;
        }

    }

    /**
     * Case where "reset" is entered into the command prompt.
     *
     * @param ARGS
     */
    private static void resetCase(String... args) {
        if (args.length != 2) {
            incorrectOp();
        } else if (!hasGitlet()) {
            noGitlet();
        } else {
            Gitlet.resetStatement(args[1]);
        }
    }

    /**
     * Case where "merge" is entered into the command prompt.
     *
     * @param ARGS
     */
    private static void mergeCase(String... args) {
        if (args.length != 2) {
            incorrectOp();
        } else if (!hasGitlet()) {
            noGitlet();
        } else {
            Gitlet.mergeStatement(args[1]);
        }
    }

    /**
     * Case where "rm-branch" is entered into the command prompt.
     *
     * @param ARGS
     */
    private static void rmBranchCase(String... args) {
        if (args.length != 2) {
            incorrectOp();
        } else if (!hasGitlet()) {
            noGitlet();
        } else {
            Gitlet.rmbranchStatement(args[1]);
        }
    }

    /**
     * Case where "branch" is entered into the command prompt.
     *
     * @param ARGS
     */
    private static void branchCase(String... args) {
        if (args.length != 2) {
            incorrectOp();
        } else if (!hasGitlet()) {
            noGitlet();
        } else {
            Gitlet.branchStatement(args[1]);
        }
    }

    /**
     * Case where "checkout" is entered into the command prompt.
     *
     * @param ARGS
     */
    private static void checkoutCases(String... args) {
        if (!hasGitlet()) {
            noGitlet();
        } else if (args[1].equals("--")) {
            try {
                Gitlet.checkoutStatement1(args[2]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (args.length == 4 && args[2].equals("--")) {
            if (args[1].length() < 6) {
                System.out.println("No commit with that id exists.");
                new Error("No commit with that id exists.");
            } else {
                Gitlet.checkoutStatement2(args[1], args[3]);
            }
        } else {
            if (args.length == 2) {
                Gitlet.checkoutStatement3(args[1]);
            } else {
                incorrectOp();
            }
        }
    }

    /**
     * Case where "status" is entered into the command prompt.
     *
     * @param ARGS
     */
    private static void statusCase(String... args) {
        if (args.length != 1) {
            incorrectOp();
        } else if (!hasGitlet()) {
            noGitlet();
        } else {
            Gitlet.statusStatement();
        }
    }

    /**
     * Case where "find" is entered into the command prompt.
     *
     * @param ARGS
     */
    private static void findCase(String... args) {
        if (args.length != 2) {
            incorrectOp();
        } else if (!hasGitlet()) {
            noGitlet();
        } else {
            Gitlet.findStatement(args[1]);
        }
    }

    /**
     * Case where "global-log" is entered into the command prompt.
     *
     * @param ARGS
     */
    private static void globalCase(String... args) {
        if (args.length != 1) {
            incorrectOp();
        } else if (!hasGitlet()) {
            noGitlet();
        } else {
            Gitlet.globallogStatement();
        }
    }

    /**
     * Case where "rm-branch" is entered into the command prompt.
     *
     * @param ARGS
     */
    private static void rmCase(String... args) {
        if (args.length != 2) {
            incorrectOp();
        } else if (!hasGitlet()) {
            noGitlet();
        } else {
            Gitlet.removeStatement(args[1]);
        }
    }

    /**
     * Case where "log" is entered into the command prompt.
     *
     * @param ARGS
     */
    private static void logCase(String... args) {
        if (args.length != 1) {
            incorrectOp();
        } else if (!hasGitlet()) {
            noGitlet();
        } else {
            Gitlet.logStatement();
        }
    }

    /**
     * Case where "commit" is entered into the command prompt.
     *
     * @param ARGS
     */
    private static void commitCase(String... args) {
        if (!hasGitlet()) {
            noGitlet();
        } else if (args.length == 1
                || args[1].trim().equals("") || args[1].equals(null)) {
            System.out.println("Please enter a commit message.");
        } else {
            Gitlet.commitStatement(args[1]);
        }
    }

    /**
     * Case where "add" is entered into the command prompt.
     *
     * @param ARGS
     */
    private static void addCase(String... args) {
        if (args.length != 2) {
            incorrectOp();
        } else if (!hasGitlet()) {
            noGitlet();
        } else {
            Gitlet.addStatement(args[1]);
        }
    }

    /**
     * Case where "init" is entered into the command prompt.
     *
     * @param ARGS
     */
    private static void initCase(String... args) {
        if (args.length != 1) {
            incorrectOp();
        } else {
            try {
                Gitlet.initStatement();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if current directory has a .gitlet/ folder.
     *
     * @return BOOLEAN
     **/
    static boolean hasGitlet() {
        File f = new File(".gitlet/");
        return f.exists();
    }

    /** Reports if the current directory has a .gitlet/ folder. **/
    static void noGitlet() {
        System.out.println("Not in an initialized gitlet directory.");
        new Error("Not in an initialized gitlet directory.");
    }

    /** Reports if there are not a correct number of operands. **/
    static void incorrectOp() {
        System.out.println("Incorrect operands.");
        new Error("Incorrect operands.");
    }



}
