package gitlet;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

/**
 * Class that contains the structures gitlet needs to function as well as
 * methods that are needed each time gitlet is started.
 *
 * @author Isabel ZHang & Jae Won Lee
 **/

public class Gitlet {

    /** Metadata object that is initialized each time a command is made. **/
    private static Metadata metadata;

    /**
     * Serializes a commit into file commit.ser.
     *
     * @param COMMIT
     **/
    public static void serializeCommits(Commit commit) {
        File outFile = new File(".gitlet/commit" + commit.getName());
        try {
            ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(commit);
            out.close();
        } catch (IOException excp) {
            return;

        }
    }

    /**
     * Deserializes a file from .gitlet/commits/ and sets that into a Commit
     * object.
     *
     * @return NAME
     **/
    public static Commit deserializeCommits(String name) {
        Commit com;

        File inFile = new File(".gitlet/commits/" + name);
        if (!inFile.exists()) {
            return null;
        }
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(inFile));
            com = (Commit) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            com = null;
        }
        return com;
    }

    /**
     * Serializes a metadata object into file metadata.ser.
     *
     * @param MET
     **/
    public static void serializeMetadata(Metadata met) {
        File outFile = new File(".gitlet/metadata.ser");
        try {
            ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(met);
            out.close();
        } catch (IOException excp) {
            return;

        }
    }

    /**
     * Deserializes a metadata.ser file into metadata.
     *
     * @return METADATA
     **/
    public Metadata deserializeMetadata() {
        File inFile = new File(".gitlet/metadata.ser");
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(inFile));
            metadata = (Metadata) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            metadata = null;
        }
        return metadata;
    }

    /**
     * Gitlet Init statement that creates a directory .gitlet in the current
     * folder. Creates a directory commits/ and blobs/ within .gitlet. Method
     * also creates a new branch "master" and initializes the first commit which
     * is then serialized.
     *
     * @throws IOException
     */
    public static void initStatement() throws IOException {
        File dir = new File(".gitlet");
        if (dir.exists() && dir.isDirectory()) {
            System.out.println("A gitlet version-control system "
                    + "already exists in the current directory.");
            new Error("A gitlet version-control system "
                    + "already exists in the current directory.");
            return;

        } else {
            dir.mkdir();
            File commits = new File(".gitlet/commits");
            commits.mkdir();
            File blobs = new File(".gitlet/blobs");
            blobs.mkdir();
            File stage = new File(".gitlet/stage");
            stage.mkdir();
            Commit incommit =
                    new Commit("initial commit",
                            null, new HashMap<String, String>());
            metadata =
                    new Metadata(incommit.getName(),
                            new HashMap<String, String>());
            metadata.addtoBranches("master", incommit.getName());
            metadata.updateCurrBranch("master");
            metadata.updateHead(incommit.getName());
            metadata.serializeMetadata();
        }

    }

    /**
     * Adds a File to the Directory .gitlet/stage/.
     *
     * @param FILENAME
     */
    public static void addStatement(String filename) {
        metadata = new Metadata(null, null);
        metadata = metadata.deserializeMetadata();
        Commit currCommit =
                deserializeCommits(metadata.getHead());

        File f = new File(filename);
        if (f.exists()) {
            String fileSHA = calculateFileSHA(f);
            if (!currCommit.getMyFiles().containsValue(fileSHA)) {
                metadata.addtoStage(f);
            }
        } else {
            System.out.println("File does not exist.");
            new Error("File does not exist.");
            return;

        }
        metadata.serializeMetadata();
    }

    /**
     * Serializes a file into .gitlet/blobs/ by writing in the contents of the
     * file.
     *
     * @param FILE
     **/
    public static void serializeFiles(File file) {
        byte[] fileContents = Utils.readContents(file);
        String fileName = file.getName();
        String fileSHA = Utils.sha1(fileContents + fileName + "file");
        File outFile = new File(".gitlet/blobs/" + fileSHA);

        try {
            ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(fileContents);
            out.close();
        } catch (IOException excp) {
            new Error("Cannot serialize " + file + " " + excp);
        }
    }

    /**
     * Serialize files from .gitlet/blobs/.
     *
     * @param FILECONTENTS
     * @param FILENAME
     */
    public void serializeFiles(byte[] fileContents, String fileName) {
        File outFile = new File(".gitlet/blobs/" + fileName);

        try {
            ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(fileContents);
            out.close();
        } catch (IOException excp) {
            new Error("Cannot serialize " + fileName + " " + excp);
        }
    }

    /**
     * Deserializes a file from .gitlet/blobs/ into a byte[] (curFile).
     *
     * @param FILEHASH
     * @return CURBYTE
     * @throws IOException
     * @throws FileNotFoundException
     **/
    public static byte[] deserializeFile(String fileHash) {
        byte[] curByte;
        File inFile = new File(".gitlet/blobs/" + fileHash);
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(inFile));
            curByte = (byte[]) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            curByte = null;
        }
        return curByte;
    }

    /**
     * Commits all tracked files.
     *
     * @param MSG
     */
    public static void commitStatement(String msg) {
        metadata = new Metadata(null, null);
        metadata = metadata.deserializeMetadata();
        if (metadata.getStage().size() != 0
                || metadata.getRemoved().size() != 0) {
            Commit recentCommit =
                    new Commit(msg, metadata.getHead(), metadata.getStage());
            for (String key : metadata.getRemoved().keySet()) {
                if (recentCommit.getMyFiles().containsKey(key)) {
                    recentCommit.getMyFiles().remove(key);
                }
            }
            metadata.updateHead(recentCommit.getName());
            metadata.addtoBranches(metadata.getCurrBranch(),
                    recentCommit.getName());
            recentCommit.serializeCommit(recentCommit);
        } else {
            System.out.println("No changes added to the commit.");
            new Error("No changes added to the commit.");
        }
        metadata.clearRemove();
        metadata.clearStage();
        serializeMetadata(metadata);
    }

    /** Prints the history of the Commits. */
    public static void logStatement() {
        metadata = new Metadata(null, null);
        metadata = metadata.deserializeMetadata();
        String head = metadata.getHead();
        Commit curr = deserializeCommits(head);
        while (curr.getParent() != null) {
            curr.printCommit();
            if (curr.getParent() == null) {
                break;
            }
            curr = deserializeCommits(curr.getParent());
        }
        System.out.println("===");
        System.out.println("Commit " + curr.getName());
        System.out.println(curr.getTimeStamp());
        System.out.println(curr.getMessage());
        metadata.serializeMetadata();

    }

    /** Like log, except displays information about all commits ever made. */
    public static void globallogStatement() {
        metadata = new Metadata(null, null);
        metadata = metadata.deserializeMetadata();
        String head = metadata.getHead();
        HashSet<String> printed = new HashSet<String>();
        File commitsDir = new File(".gitlet/commits/");
        File[] allCommits = commitsDir.listFiles();
        for (String key : metadata.getBranches().keySet()) {
            Commit curr =
                    deserializeCommits(metadata.getBranches().get(key));
            while (curr != null && !printed.contains(curr.getName())) {
                curr.printCommit();
                printed.add(curr.getName());
                if (curr.getParent() == null) {
                    break;
                }
                curr = deserializeCommits(curr.getParent());
            }
        }
        for (File f : allCommits) {
            String commitName = f.getName();
            if (!printed.contains(commitName)) {
                Commit c = deserializeCommits(commitName);
                c.printCommit();
                printed.add(commitName);
            }
        }
        metadata.serializeMetadata();
    }

    /**
     * Removes a Commit.
     *
     * @param FILENAME
     */
    public static void removeStatement(String filename) {
        metadata = new Metadata(null, null);
        metadata = metadata.deserializeMetadata();
        Commit current = deserializeCommits(metadata.getHead());
        HashMap<String, String> currentFiles = current.getMyFiles();
        File f = new File(filename);
        if (current.getMyFiles().containsKey(filename) || f.exists()) {
            metadata.addtoRemoved(f);
        }

        if (f.exists()) {
            if (metadata.getStage().containsKey(f.getName())
                    && currentFiles.containsKey(f.getName())) {
                metadata.removeFromStage(f);

                for (String key : metadata.getRemoved().keySet()) {
                    if (currentFiles.containsKey(key)) {
                        Utils.restrictedDelete(key);
                    }
                }
            }
            if (!metadata.getStage().containsKey(f.getName())
                    && !currentFiles.containsKey(f.getName())) {
                System.out.println("No reason to remove the file.");
                new Error("No reason to remove the file.");
                return;
            }
            if (metadata.getStage().containsKey(f.getName())
                    && !currentFiles.containsKey(f.getName())) {
                metadata.removeFromStage(f);
            }
            if (!metadata.getStage().containsKey(f.getName())
                    && currentFiles.containsKey(f.getName())) {
                for (String key : metadata.getRemoved().keySet()) {
                    if (currentFiles.containsKey(key)) {
                        Utils.restrictedDelete(key);
                    }
                }
            }

        } else {

            if (!metadata.getStage().containsKey(filename)
                    && !currentFiles.containsKey(filename)) {
                System.out.println("No reason to remove the file.");
                new Error("No reason to remove the file.");
                return;
            }
        }
        metadata.serializeMetadata();
    }

    /**
     * Prints out the ids of all commits that have the given commit message, one
     * per line.
     *
     * @param MESSAGE
     */
    public static void findStatement(String message) {
        metadata = new Metadata(null, null);
        metadata = metadata.deserializeMetadata();
        String head = metadata.getHead();
        Commit curr = deserializeCommits(head);
        ArrayList<String> matches = new ArrayList<String>();
        File f = new File(".gitlet/commits/");
        File[] allCommits = f.listFiles();
        for (File commitFile : allCommits) {
            Commit c = deserializeCommits(commitFile.getName());
            if (c.getMessage().equals(message)) {
                String name = c.getName();
                System.out.println(name);
                matches.add(name);
            }
        }

        if (matches.size() == 0) {
            System.out.println("Found no commit with that message.");
            new Error("Found no commit with that message.");

        }
        metadata.serializeMetadata();
    }

    /** Displays what branches currently exist. */
    public static void statusStatement() {
        metadata = new Metadata(null, null);
        metadata = metadata.deserializeMetadata();
        String head = metadata.getHead();
        Commit currHead = deserializeCommits(head);
        TreeMap<String, String> removed =
                new TreeMap<String, String>(metadata.getRemoved());
        TreeMap<String, String> stage =
                new TreeMap<String, String>(metadata.getStage());
        TreeMap<String, String> branches =
                new TreeMap<String, String>(metadata.getBranches());
        TreeMap<String, String> myFiles =
                new TreeMap<String, String>(currHead.getMyFiles());
        printBranches(branches);
        printStagedfiles(stage);
        File dir = new File(".");
        List<String> workingDirectory =
                Utils.plainFilenamesIn(dir);
        printRemovedfiles(removed, myFiles, workingDirectory);
        printModifiedbutnotstagednoec();
        printUntrackedfilesnoec();
        metadata.serializeMetadata();
    }

    /**
     * Print modified but not staged for status.
     *
     * @param REMOVED
     * @param STAGE
     * @param MYFILES
     * @param WORKINGDIRECTORY
     */
    private static void printModifiedbutnotstaged(
            TreeMap<String, String> removed,
            TreeMap<String, String> stage, TreeMap<String, String> myFiles,
            List<String> workingDirectory) {
        System.out.println(
                "=== Modifications Not Staged For Commit ===");
        for (String f : workingDirectory) {
            if (myFiles.containsKey(f)) {
                if (!stage.containsKey(f)) {
                    File currFile = new File(f);
                    String fileHash = calculateFileSHA(currFile);
                    if (!myFiles.containsValue(fileHash)) {
                        System.out.println(f + " (modified)");
                    }
                    if (removed.containsKey(f)) {
                        System.out.println(f + " (deleted) a");
                    }
                }
                if (stage.containsKey(f)) {
                    File currFile = new File(f);
                    String fileHash = calculateFileSHA(currFile);
                    if (!myFiles.containsValue(fileHash)) {
                        System.out.println(f + " (modified)");
                    }
                    if (removed.containsKey(f)) {
                        System.out.println(f + " (deleted) b");
                    }
                }
            }
        }
        for (String f : myFiles.keySet()) {
            if (stage.containsKey(f)) {
                if (removed.containsKey(f) && !workingDirectory.contains(f)) {
                    System.out.println(f + " (deleted)");
                }
            }
            if (!stage.containsKey(f)) {
                if (removed.containsKey(f) && !workingDirectory.contains(f)) {
                    System.out.println(f + " (deleted)");
                }
            }
        }
        System.out.print("\n");
    }

    /**
     * Print modified but not staged for status.
     *
     * @param REMOVED
     * @param STAGE
     * @param MYFILES
     * @param WORKINGDIRECTORY
     */
    private static void printModifiedbutnotstagednoec() {
        System.out.println(
                "=== Modifications Not Staged For Commit ===");
        System.out.print("\n");
    }

    /**
     * Print untracked files for status.
     *
     * @param STAGE
     * @param MYFILES
     * @param WORKINGDIRECTORY
     */
    private static void printUntrackedfilesnoec() {
        System.out.println("=== Untracked Files ===");
    }

    /**
     * Print untracked files for status.
     *
     * @param STAGE
     * @param MYFILES
     * @param WORKINGDIRECTORY
     */
    private static void printUntrackedfiles(TreeMap<String, String> stage,
                                            TreeMap<String, String> myFiles, List<String> workingDirectory) {
        System.out.println("=== Untracked Files ===");
        for (String f : workingDirectory) {
            if (!myFiles.containsKey(f) && !stage.containsKey(f)) {
                System.out.println(f);
            }
        }
    }

    /**
     * Prints Removed Files for status.
     *
     * @param REMOVED
     * @param MYFILES
     * @param WORKINGDIRECTORY
     */
    private static void printRemovedfiles(TreeMap<String, String> removed,
                                          TreeMap<String, String> myFiles, List<String> workingDirectory) {
        System.out.println("=== Removed Files ===");
        for (String re : removed.keySet()) {
            if (!workingDirectory.contains(re)
                    && myFiles.containsKey(re)) {
                System.out.println(re);
            }
        }
        System.out.print("\n");
    }

    /**
     * Print staged files for status.
     *
     * @param STAGE
     */
    private static void printStagedfiles(TreeMap<String, String> stage) {
        System.out.println("=== Staged Files ===");
        for (String sta : stage.keySet()) {
            System.out.println(sta);
        }
        System.out.print("\n");
    }

    /**
     * Print branches for status.
     *
     * @param BRANCHES
     */
    private static void printBranches(TreeMap<String, String> branches) {
        System.out.println("=== Branches ===");
        System.out.println("*" + metadata.getCurrBranch());
        for (String br : branches.keySet()) {
            if (!br.equals(metadata.getCurrBranch())) {
                System.out.println(br);
            }
        }
        System.out.print("\n");
    }

    /**
     * Calculates the SHA-1 for a file.
     *
     * @param FILE
     * @return SHA
     **/
    public static String calculateFileSHA(File file) {
        byte[] fileContents = Utils.readContents(file);
        String fileName = file.getName();
        String sha = Utils.sha1(fileContents, fileName, "file");
        return sha;
    }

    /**
     * Creates a new branch with the given name.
     *
     * @param BRANCHNAME
     */
    public static void branchStatement(String branchName) {
        metadata = new Metadata(null, null);
        metadata = metadata.deserializeMetadata();
        if (!metadata.getBranches().containsKey(branchName)) {
            metadata.getBranches().put(branchName, metadata.getHead());
        } else {
            System.out.println("A branch with that name already exists.");
            new Error("A branch with that name already exists.");
        }
        metadata.serializeMetadata();
    }

    /**
     * Deletes the branch with the given name.
     *
     * @param BRANCHNAME
     */
    public static void rmbranchStatement(String branchName) {
        metadata = new Metadata(null, null);
        metadata = metadata.deserializeMetadata();
        if (metadata.getCurrBranch().equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            new Error("Cannot remove the current branch.");
        }
        if (metadata.getBranches().containsKey(branchName)) {
            metadata.getBranches().remove(branchName);
        } else {
            System.out.println("A branch with that name does not exist.");
            new Error("A branch with that name does not exist.");
        }
        metadata.serializeMetadata();
    }

    /**
     * Checks out all the files tracked by the given commit.
     *
     * @param COMMITID
     */
    public static void resetStatement(String commitID) {
        metadata = new Metadata(null, null);
        metadata = metadata.deserializeMetadata();
        commitID = abbrev(commitID);
        if (commitID.equals("none")) {
            System.out.println("No commit with that id exists.");
            new Error("No commit with that id exists.");
            return;
        }
        File dir = new File(".");
        List<String> workingDirectory =
                Utils.plainFilenamesIn(dir);
        String head = metadata.getHead();
        Commit currHead = deserializeCommits(head);
        File a = new File(".gitlet/commits/" + commitID);
        if (!a.exists()) {
            System.out.println("No commit with that id exists.");
            new Error("No commit with that id exists.");
            return;
        }
        Commit resetCommit = deserializeCommits(commitID);

        for (String f : workingDirectory) {
            if (!currHead.getMyFiles().containsKey(f)
                    && !metadata.getStage().containsKey(f)) {
                if (!f.startsWith(".") && !f.equals("Makefile")) {
                    System.out.println(
                            "There is an untracked file in"
                                    + " the way; delete it or add it first.");
                    new Error("There is an untracked file in"
                            + " the way; delete it or add it first.");
                    return;
                }
            }
        }

        for (String f : workingDirectory) {
            if (!resetCommit.getMyFiles().containsKey(f)) {
                if (!f.startsWith(".") && !f.equals("Makefile")) {
                    File x = new File(f);
                    x.delete();
                }
            }
        }
        Commit currCommit = deserializeCommits(commitID);
        for (String t : currCommit.getMyFiles().keySet()) {
            String fileSHA = currCommit.getMyFiles().get(t);
            byte[] fileBytes = deserializeFile(fileSHA);
            Utils.writeContents(new File(t), fileBytes);
        }
        metadata.updateHead(commitID);
        metadata.getBranches().put(metadata.getCurrBranch(), commitID);
        metadata.clearStage();
        serializeMetadata(metadata);
    }

    /**
     * Merges files from the given branch into the current branch.
     *
     * @param BRANCHNAME
     */
    public static void mergeStatement(String branchName) {
        metadata = new Metadata(null, null);
        metadata = metadata.deserializeMetadata();
        HashMap<String, String> stage = metadata.getStage();
        String currBranch = metadata.getCurrBranch();
        String head = metadata.getHead();
        Commit currCommit = deserializeCommits(head);
        HashMap<String, String> branches = metadata.getBranches();
        boolean conflict = false;
        File dir = new File(".");
        List<String> workingDirectory = Utils.plainFilenamesIn(dir);
        mergeHelperfirst(currCommit, workingDirectory);
        mergeErrors(branchName);
        String otherBranchHead = branches.get(branchName);
        Commit otherCommit = deserializeCommits(otherBranchHead);
        String splitPoint = null;
        Commit splitCommit = null;
        HashSet<String> otherCommitHistory = new HashSet<String>();
        while (otherCommit != null) {
            otherCommitHistory.add(otherCommit.getName());
            otherCommit = deserializeCommits(otherCommit.getParent());
        }
        otherCommit = deserializeCommits(otherBranchHead);
        while (currCommit != null) {
            if (otherCommitHistory.contains(currCommit.getName())) {
                splitPoint = currCommit.getName();
                splitCommit = deserializeCommits(splitPoint);
                break;
            }
            currCommit = deserializeCommits(currCommit.getParent());
        }
        currCommit = deserializeCommits(head);
        mergeCases2(head, otherBranchHead, splitPoint);
        boolean useless = false;
        HashMap<String, String> otherCommitFiles = otherCommit.getMyFiles();
        HashMap<String, String> currCommitFiles = currCommit.getMyFiles();
        HashMap<String, String> splitPointFiles = splitCommit.getMyFiles();
        HashMap<String, String> newFiles = new HashMap<String, String>();
        conflict = mergeSplitpoint(stage, conflict, otherBranchHead,
                otherCommitFiles, currCommitFiles, splitPointFiles, newFiles);
        conflict = mergeCurrcommit(conflict, otherCommitFiles, currCommitFiles,
                splitPointFiles, newFiles);

        conflict = mergeOthercommit(stage, conflict, otherBranchHead,
                otherCommitFiles, currCommitFiles, splitPointFiles, newFiles);
        if (!conflict) {
            commitforMerge("Merged " + currBranch + " with "
                    + branchName + ".", metadata);
            return;
        }
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
        metadata.serializeMetadata();
    }

    /**
     * Iterates through working directory.
     *
     * @param CURRCOMMIT
     * @param WORKINGDIRECTORY
     */
    private static void mergeHelperfirst(Commit currCommit,
                                         List<String> workingDirectory) {
        for (String f : workingDirectory) {
            if (!currCommit.getMyFiles().containsKey(f)
                    && !metadata.getStage().containsKey(f)) {
                if (!f.startsWith(".") && !f.equals("Makefile")) {
                    System.out.println(
                            "There is an untracked file in"
                                    + " the way; delete it or add it first.");
                    new Error("There is an untracked file in"
                            + " the way; delete it or add it first.");
                    System.exit(0);
                }
            }
        }
    }

    /**
     * First 2 merge cases.
     *
     * @param HEAD
     * @param OTHERBRANCHHEAD
     * @param SPLITPOINT
     */
    private static void mergeCases2(String head,
                                    String otherBranchHead, String splitPoint) {
        if (splitPoint.equals(otherBranchHead)) {
            System.out.println(
                    "Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        if (splitPoint.equals(head)) {
            metadata.updateHead(otherBranchHead);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
    }

    /**
     * Merge Errors.
     *
     * @param BRANCHNAME
     */
    private static void mergeErrors(String branchName) {
        if (metadata.getRemoved().size() != 0
                || metadata.getStage().size() != 0) {
            System.out.println("You have uncommitted changes.");
            new Error("You have uncommitted changes.");
            System.exit(0);
        }
        if (!metadata.getBranches().containsKey(branchName)) {
            System.out.print("A branch with that name does not exist.");
            new Error("A branch with that name does not exist.");
            System.exit(0);
        }
        if (metadata.getCurrBranch().equals(branchName)) {
            System.out.print("Cannot merge a branch with itself.");
            new Error("Cannot merge a branch with itself.");
            System.exit(0);
        }
    }

    /**
     * Merge helper that iterates through the othercommit files.
     *
     * @param STAGE
     * @param CONFLICT
     * @param OTHERBRANCHHEAD
     * @param OTHERCOMMITFILES
     * @param CURRCOMMITFILES
     * @param SPLITPOINTFILES
     * @param NEWFILES
     * @return
     */
    private static boolean mergeOthercommit(
            HashMap<String, String> stage,
            boolean conflict,
            String otherBranchHead,
            HashMap<String, String> otherCommitFiles,
            HashMap<String, String> currCommitFiles,
            HashMap<String, String> splitPointFiles,
            HashMap<String, String> newFiles) {
        for (String fileName : otherCommitFiles.keySet()) {

            if (!splitPointFiles.containsKey(fileName)
                    && !currCommitFiles.containsKey(fileName)) {
                checkoutStatement2(otherBranchHead, fileName);
                stage.put(fileName, otherCommitFiles.get(fileName));
                newFiles.put(fileName, otherCommitFiles.get(fileName));
            }
            if (!splitPointFiles.containsKey(fileName)
                    && currCommitFiles.containsKey(fileName)
                    && conflict) {
                String currSHA = currCommitFiles.get(fileName);
                String otherSHA = otherCommitFiles.get(fileName);
                if (!currSHA.equals(otherSHA)) {
                    conflict = true;
                    try {
                        mergeConflict(new File(".gitlet/blobs/" + currSHA),
                                new File(".gitlet/blobs/" + otherSHA), fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return conflict;
    }

    /**
     * Merge helper that iterates through the othercommit files.
     *
     * @param CONFLICT
     * @param OTHERCOMMITFILES
     * @param CURRCOMMITFILES
     * @param SPLITPOINTFILES
     * @param NEWFILES
     * @return
     */
    private static boolean mergeCurrcommit(boolean conflict,
                                           HashMap<String, String> otherCommitFiles,
                                           HashMap<String, String> currCommitFiles,
                                           HashMap<String, String> splitPointFiles,
                                           HashMap<String, String> newFiles) {
        for (String fileName : currCommitFiles.keySet()) {
            if (!splitPointFiles.containsKey(fileName)
                    && !otherCommitFiles.containsKey(fileName)) {
                newFiles.put(fileName, currCommitFiles.get(fileName));
                break;
            }
            if (!splitPointFiles.containsKey(fileName)
                    && otherCommitFiles.containsKey(fileName)) {
                String currSHA = currCommitFiles.get(fileName);
                String otherSHA = otherCommitFiles.get(fileName);
                if (!currSHA.equals(otherSHA)) {
                    conflict = true;
                    try {
                        mergeConflict(new File(".gitlet/blobs/" + currSHA),
                                new File(".gitlet/blobs/" + otherSHA), fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return conflict;
    }

    /**
     * Merge helper that iterates through the splitPoint files.
     *
     * @param STAGE
     * @param CONFLICT
     * @param OTHERBRANCHHEAD
     * @param OTHERCOMMITFILES
     * @param CURRCOMMITFILES
     * @param SPLITPOINTFILES
     * @param NEWFILES
     * @return
     */
    private static boolean mergeSplitpoint(
            HashMap<String, String> stage,
            boolean conflict,
            String otherBranchHead,
            HashMap<String, String> otherCommitFiles,
            HashMap<String, String> currCommitFiles,
            HashMap<String, String> splitPointFiles,
            HashMap<String, String> newFiles) {
        boolean useless;
        conflict = mergeHelper1(stage, conflict, otherBranchHead,
                otherCommitFiles, currCommitFiles,
                splitPointFiles, newFiles);
        return conflict;
    }

    /**
     * Helps find merge Split point.
     *
     * @param STAGE
     * @param CONFLICT
     * @param OTHERBRANCHHEAD
     * @param OTHERCOMMITFILES
     * @param CURRCOMMITFILES
     * @param SPLITPOINTFILES
     * @param NEWFILES
     * @return
     */
    private static boolean mergeHelper1(HashMap<String, String> stage,
                                        boolean conflict,
                                        String otherBranchHead, HashMap<String, String> otherCommitFiles,
                                        HashMap<String, String> currCommitFiles,
                                        HashMap<String, String> splitPointFiles,
                                        HashMap<String, String> newFiles) {
        boolean useless;
        for (String fileName : splitPointFiles.keySet()) {
            String spSHA = splitPointFiles.get(fileName);
            String currSHA = currCommitFiles.get(fileName);
            String otherSHA = otherCommitFiles.get(fileName);
            if (currSHA == null) {
                currSHA = "none";
            }
            if (spSHA == null) {
                spSHA = "none";
            }
            if (otherSHA == null) {
                otherSHA = "none";
            }
            if (spSHA.equals(currSHA) && !spSHA.equals(otherSHA)) {
                if (otherCommitFiles.containsKey(fileName)) {
                    checkoutStatement2(otherBranchHead, fileName);
                    stage.put(fileName, otherSHA);
                    newFiles.put(fileName, otherSHA);
                }
            }
            if (spSHA.equals(otherSHA)
                    && !spSHA.equals(currSHA)) {
                newFiles.put(fileName, currSHA);
            }
            if (spSHA.equals(currSHA)
                    && !otherCommitFiles.containsKey(fileName)) {
                removeStatement(fileName);
            }

            if (spSHA.equals(otherSHA)
                    && !currCommitFiles.containsKey(fileName)) {
                useless = true;
            }
            if (!spSHA.equals(otherSHA)
                    && !spSHA.equals(currSHA)
                    && !currSHA.equals(otherSHA)) {
                conflict = true;
                try {
                    mergeConflict(new File(".gitlet/blobs/" + currSHA),
                            new File(".gitlet/blobs/" + otherSHA), fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            conflict = mergeHelper3(conflict, otherCommitFiles,
                    fileName, spSHA, currSHA, otherSHA);
            conflict = mergeHelper2(conflict, currCommitFiles,
                    fileName, spSHA, currSHA, otherSHA);
        }
        return conflict;
    }

    /**
     * MergeHelper3.
     *
     * @param CONFLICT
     * @param OTHERCOMMITFILES
     * @param FILENAME
     * @param SPSHA
     * @param CURRSHA
     * @param OTHERSHA
     * @return
     */
    private static boolean mergeHelper3(boolean conflict,
                                        HashMap<String, String> otherCommitFiles,
                                        String fileName, String spSHA, String currSHA, String otherSHA) {
        if (!spSHA.equals(currSHA)
                && !otherCommitFiles.containsKey(fileName)) {
            conflict = true;
            try {
                mergeConflict(new File(".gitlet/blobs/" + currSHA),
                        new File(".gitlet/blobs/" + otherSHA), fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return conflict;
    }

    /**
     * MergeHelper2.
     *
     * @param CONFLICT
     * @param CURRCOMMITFILES
     * @param FILENAME
     * @param SPSHA
     * @param CURRSHA
     * @param OTHERSHA
     * @return
     */
    private static boolean mergeHelper2(boolean conflict,
                                        HashMap<String, String> currCommitFiles,
                                        String fileName, String spSHA, String currSHA, String otherSHA) {
        if (!spSHA.equals(otherSHA)
                && !currCommitFiles.containsKey(fileName)) {
            conflict = true;
            try {
                mergeConflict(new File(".gitlet/blobs/" + currSHA),
                        new File(".gitlet/blobs/" + otherSHA), fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return conflict;
    }

    /**
     * Fixes mergeConflicts by overwriting a file with both the current and
     * given contents.
     *
     * @param CURR
     * @param OTHER
     * @param FILENAME
     * @throws IOException
     */
    public static void mergeConflict(File curr,
                                     File other, String fileName) throws IOException {
        File file = new File(fileName);
        if (curr.exists() && other.exists()) {
            byte[] currContents = Utils.readContents(curr);
            byte[] otherContents = Utils.readContents(other);

            FileWriter myWriter = new FileWriter(file, true);
            myWriter.write("<<<<<<< HEAD" + "\n");
            myWriter.close();
            Utils.conflictContents(new File(fileName), currContents);

            FileWriter myWriter1 = new FileWriter(file, true);
            myWriter1.write("=======" + "\n");
            myWriter1.close();
            Utils.conflictContents(file, otherContents);

            FileWriter addwriter = new FileWriter(file, true);
            addwriter.write(">>>>>>>" + "\n");
            addwriter.close();
        } else {
            FileWriter myWriter = new FileWriter(file, true);
            myWriter.write("<<<<<<< HEAD" + "\n");
            myWriter.close();

            FileWriter myWriter1 = new FileWriter(file, true);
            myWriter1.write("=======" + "\n");
            myWriter1.close();

            FileWriter addwriter = new FileWriter(file, true);
            addwriter.write(">>>>>>>" + "\n");
            addwriter.close();
        }
}

    /**
     * Testing for mergeConflict from Main.
     *
     * @param CURR
     * @param OTHER
     * @param FILENAME
     * @throws IOException
     */
    public static void mergeConflict(String curr, String other, String fileName)
            throws IOException {
        File f = new File(curr);
        File g = new File(other);
        mergeConflict(f, g, fileName);
    }

    /**
     * Commits all tracked files.
     *
     * @param MSG
     * @param META
     */
    public static void commitforMerge(String msg, Metadata meta) {
        Commit recentCommit = new Commit(msg, meta.getHead(), meta.getStage());
        if (meta.getStage().size() != 0 || meta.getRemoved().size() != 0) {

            for (String key : meta.getRemoved().keySet()) {
                if (recentCommit.getMyFiles().containsKey(key)) {
                    recentCommit.getMyFiles().remove(key);
                }
            }
            recentCommit.serializeCommit(recentCommit);
        }
        meta.updateHead(recentCommit.getName());
        meta.addtoBranches(meta.getCurrBranch(),
                recentCommit.getName());

        meta.clearRemove();
        meta.clearStage();
        meta.serializeMetadata();
    }

    /**
     * Case 1 of checkout where we checkout a file from the head commit.
     *
     * @param FILENAME
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void checkoutStatement1(String fileName)
            throws FileNotFoundException, IOException {
        metadata = new Metadata(null, null);
        metadata = metadata.deserializeMetadata();
        Commit currCommit = deserializeCommits(metadata.getHead());
        if (currCommit.getMyFiles().containsKey(fileName)) {
            String fileSHA = currCommit.getMyFiles().get(fileName);
            byte[] fileBytes = deserializeFile(fileSHA);
            Utils.writeContents(new File(fileName), fileBytes);
        } else {
            System.out.println("File does not exist in that commit.");
            new Error("File does not exist in that commit.");
        }
        metadata.serializeMetadata();

    }

    /**
     * Case 2 of checkout that checks out a file from a certain commit.
     *
     * @param COMMITID
     * @param FILENAME
     */
    public static void checkoutStatement2(String commitID,
                                          String fileName) {
        metadata = new Metadata(null, null);
        metadata = metadata.deserializeMetadata();
        commitID = abbrev(commitID);
        if (commitID.equals("none")) {
            System.out.println("No commit with that id exists.");
            new Error("No commit with that id exists.");
            return;
        }
        Commit c = deserializeCommits(commitID);
        if (c == null) {
            System.out.println("No commit with that id exists.");
            new Error("No commit with that id exists.");
            return;
        }
        HashMap<String, String> myFiles = c.getMyFiles();
        if (!myFiles.containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            new Error("File does not exist in that commit.");
            return;
        }
        String fileSHA = myFiles.get(fileName);
        byte[] fileBytes = deserializeFile(fileSHA);
        Utils.writeContents(new File(fileName), fileBytes);
        metadata.serializeMetadata();
    }

    /**
     * Checks out an entire branch. Errors if you're being dumb.
     *
     * @param BRANCHNAME
     */
    public static void checkoutStatement3(String branchName) {
        metadata = new Metadata(null, null);
        metadata = metadata.deserializeMetadata();
        String currHead = metadata.getHead();
        String currBranch = metadata.getCurrBranch();
        HashMap<String, String> branches = metadata.getBranches();
        Commit currCommit = deserializeCommits(currHead);
        File dir = new File(".");
        List<String> workingDirectory = Utils.plainFilenamesIn(dir);
        HashSet<String> tracked = new HashSet<String>();
        for (String f : workingDirectory) {
            Commit curr = deserializeCommits(metadata.getHead());
            while (!tracked.contains(f) && curr != null) {
                if (curr.getMyFiles().containsKey(f)
                        || curr.getStage().containsKey(f)) {
                    curr = deserializeCommits(curr.getParent());
                    tracked.add(f);
                    break;
                }
                curr = deserializeCommits(curr.getParent());
            }
            if (!tracked.contains(f)
                    && !f.equals("Makefile") && !f.startsWith(".")) {
                System.out.println(
                        "There is an untracked file"
                                + " in the way; delete it or add it first.");
                new Error("There is an untracked file"
                        + " in the way; delete it or add it first.");
                return;
            }
        }
        if (!branches.containsKey(branchName)) {
            System.out.println("No such branch exists.");
            new Error("No such branch exists.");
            return;
        }
        String branchCommit = branches.get(branchName);
        Commit branchHead = deserializeCommits(branchCommit);
        HashMap<String, String> myFiles = branchHead.getMyFiles();
        checkoutHelper1(workingDirectory, branchHead);
        if (currBranch.equals(branchName)) {
            System.out.println("No need to checkout"
                    + " the current branch.");
            new Error("No need to checkout the current branch.");
            return;
        }
        checkoutHelper2(myFiles);
        metadata.updateCurrBranch(branchName);
        metadata.updateHead(branchCommit);
        File stagePath = new File(".gitlet/stage/");
        File[] stagedFiles = stagePath.listFiles();
        for (File f : stagedFiles) {
            f.delete();
        }
        metadata.clearStage();
        metadata.clearRemove();
        metadata.serializeMetadata();
    }

    /**
     * Extracted method for checkout3.
     *
     * @param MYFILES
     */
    private static void checkoutHelper2(HashMap<String, String> myFiles) {
        for (String fileName : myFiles.keySet()) {
            String fileSHA = myFiles.get(fileName);
            byte[] fileBytes = deserializeFile(fileSHA);
            Utils.writeContents(new File(fileName), fileBytes);
        }
    }

    /**
     * Extracted method for checkout3.
     *
     * @param WORKINGDIRECTORY
     * @param BRANCHHEAD
     */
    private static void checkoutHelper1(List<String> workingDirectory,
                                        Commit branchHead) {
        for (String f : workingDirectory) {
            if (!branchHead.getMyFiles().containsKey(f) && !f.equals("Makefile")
                    && !f.startsWith(".")) {
                File x = new File(f);
                x.delete();
            }
        }
    }

    /**
     * Finds the name of the actual commitID if a shortened ID is provided.
     *
     * @param SHORTENEDID
     * @return
     */
    public static String abbrev(String shortenedID) {
        File commits = new File(".gitlet/commits/");
        File[] x = commits.listFiles();
        String result = "none";
        for (int i = 0; i < x.length; i++) {
            File f = x[i];
            if (f.getName().startsWith(shortenedID)) {
                result = f.getName();
            }
        }
        return result;
    }
}
