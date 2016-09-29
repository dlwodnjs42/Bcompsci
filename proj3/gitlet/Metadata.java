package gitlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

public class Metadata implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /** Represents the head commit. **/
    String head;

    /** Represents branch we are currently on. **/
    String currBranch;

    /** Name of file to SHA of file. **/
    HashMap<String, String> stage;

    /** Name of file to SHA of file. **/
    HashMap<String, String> removed;

    /** Name of branch to SHA of commit. **/
    HashMap<String, String> branches;

    Metadata(String h, HashMap<String, String> br) {
        head = h;
        branches = br;
        stage = new HashMap<String, String>();
        removed = new HashMap<String, String>();
    }

    public void clearStage() {
        stage = new HashMap<String, String>();

    }

    public String getHead() {
        return head;
    }

    public HashMap<String, String> getBranches() {
        return branches;
    }

    public String getCurrBranch() {
        return currBranch;
    }


    public void updateCurrBranch(String br) {
        currBranch = br;
    }

    public void updateHead(String he) {
        head = he;
    }

    public HashMap<String, String> getStage() {
        return stage;
    }

    public void addtoRemoved(File file) {
        String fileSHA = calculateFileSHA(file);
        String fileName = file.getName();
        removed.put(fileName, fileSHA);
    }

    public void addtoStage(File file) {
        String fileSHA = calculateFileSHA(file);
        String fileName = file.getName();
        if (!stage.containsKey(fileName)) {
            stage.put(fileName, fileSHA);
        } else {
            if (!stage.get(fileName).equals(fileSHA)) {
                stage.remove(fileName);
                stage.put(fileName, fileSHA);
            }
        }
        serializeFilesStage(file);
    }

    public HashMap<String, String> getRemoved() {
        return removed;
    }

    public void clearRemove() {
        removed.clear();
    }

    public void removeFromStage(File file) {
        File s = new File(".gitlet/stage/" + stage.get(file.getName()));
        s.delete();
        stage.remove(file.getName());
    }

    public String pr() {
        return "metadata printed";
    }

    public String calculateFileSHA(File file) {
        byte[] fileContents = Utils.readContents(file);
        String fileName = file.getName();
        String SHA = Utils.sha1(fileContents, fileName, "file");
        return SHA;
    }

    public void addtoBranches(String name, String commitSHA) {
        branches.put(name, commitSHA);
    }

    /**
     * Serializes a file into file in .gitlet/blobs.
     *
     * @param file
     *            - files.
     **/
    public void serializeFilesStage(File file) {
        String fileSHA = calculateFileSHA(file);
        File outFile2 = new File(".gitlet/stage/" + fileSHA);

        try {
            ObjectOutputStream out2 = new ObjectOutputStream(new FileOutputStream(outFile2));
            out2.writeObject(file);
            out2.close();
        } catch (IOException excp) {
            new Error("Cannot serialize " + file + " " + excp);
        }
    }

    /**
     * Serializes a metadata object into file metadata.ser.
     *
     * @param MET
     **/
    public void serializeMetadata() {
        File outFile = new File(".gitlet/metadata.ser");
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(this);
            out.close();
        } catch (IOException excp) {
            new Error("Cannot serialize commit " + excp);
        }
    }

    /**
     * Deserializes a metadata.ser file into metadata.
     *
     * @return CURCOMMIT
     **/
    public Metadata deserializeMetadata() {
        Metadata metadata = new Metadata("", null);
        File inFile = new File(".gitlet/metadata.ser");
        try {
            ObjectInputStream inp = new ObjectInputStream(new FileInputStream(inFile));
            metadata = (Metadata) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            metadata = null;
        }
        return metadata;
    }
}
