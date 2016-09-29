package gitlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Commit object stores all the information a single commit needs to know.
 *
 * @author Isabel Zhang & Jae Won Lee
 *
 */
public class Commit implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Data Structure to keep track of file name mapped to file's SHA-1. Ex:
     * Wug1.txt -> a12352352 (SHA-1 of Wug1.txt)
     **/
    private HashMap<String, String> myFiles;

    /** Log Message the user inputs. **/
    private String logMsg;

    /** Auto-generated time stamp that comes with each commit. **/
    private String timeStamp;

    /** String pointer to last commit. **/
    private String parent;

    /** Name of commit. **/
    private String name;

    /** Staging Area Hashmap. **/
    private HashMap<String, String> stage;

    /**
     * All commits but the first.
     *
     * @param MSG
     * @param PAR
     * @param STA
     **/
    Commit(String msg, String par, HashMap<String, String> sta) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        timeStamp = sdf.format(date);

        stage = sta;
        parent = par;
        try {
            fileInstantiation();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logMsg = msg;
        namingCommit();
        serializeCommit(this);
    }


    /**
     * Serializes a commit into file in .gitlet/commits/ by the name of the
     * commit.
     *
     * @param COMMIT
     **/
    public void serializeCommit(Commit commit) {
        File outFile = new File(".gitlet/commits/" + name);
        try {
            ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(commit);
            out.close();
        } catch (IOException excp) {
            new Error("Cannot serialize commit " + excp);
        }
    }

    /**
     * Deserializes a file from .gitlet/commits/ and sets that into a Commit
     * object.
     *
     * @param FIELD
     * @return CURCOMMIT
     **/
    public Commit deserializeCommits(String field) {
        Commit com;
        File inFile = new File(".gitlet/commits/" + field);
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
     * Returns commit's blob reference HashMap that maps wug.txt -> a123.
     *
     * @return MYFILES
     **/
    public HashMap<String, String> getMyFiles() {
        return myFiles;
    }

    /**
     * Instantiates myFiles by setting equal to parent's myFiles and adds the
     * staged files as well. If a file is both in my parent's files and the
     * stage, the staged version overrides the parent's version in this
     * iteration of the commit.
     *
     * @throws IOException
     * @throws FileNotFoundException
     **/
    public void fileInstantiation() throws FileNotFoundException, IOException {
        myFiles = new HashMap<String, String>();

        if (parent == null) {
            for (String key : stage.keySet()) {
                myFiles.put(key, stage.get(key));
                byte[] blobContents = deserializeFileStage(stage.get(key));
                serializeFiles(blobContents, stage.get(key));
                File s = new File(".gitlet/stage/" + stage.get(key));
                s.delete();
            }
        } else {
            Commit myparent = deserializeCommits(parent);
            for (String key : myparent.myFiles.keySet()) {
                myFiles.put(key, myparent.myFiles.get(key));
            }
            for (String key : stage.keySet()) {
                myFiles.put(key, stage.get(key));
                byte[] blobContents = deserializeFileStage(stage.get(key));
                serializeFiles(blobContents, stage.get(key));
                File s = new File(".gitlet/stage/" + stage.get(key));
                s.delete();
            }
        }

    }

    /**
     * Serializes a file into .gitlet/blobs/ by it's SHA name.
     *
     * @param FILECONTENTS
     * @param FILENAME
     * @throws IOException
     * @throws FileNotFoundException
     **/
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
     * Deserializes a file in .gitlet/stage/ into an accessible file.
     *
     * @return FILEHASH
     * @throws IOException
     * @throws FileNotFoundException
     **/
    public byte[] deserializeFileStage(String fileHash) {
        byte[] curByte;
        File curFile;
        File inFile = new File(".gitlet/stage/" + fileHash);
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(inFile));
            curFile = (File) inp.readObject();
            inp.close();
            curByte = Utils.readContents(curFile);
        } catch (IOException | ClassNotFoundException excp) {
            curByte = null;
        }
        return curByte;
    }

    /**
     * Gets this commit's parent.
     *
     * @return PARENT
     **/
    public String getParent() {
        return parent;
    }

    /**
     * Return Parent Commit.
     *
     * @return PAR
     **/
    public Commit getParentCommit() {
        File f = new File(".gitlet/commits/" + parent);
        if (f.exists()) {
            return deserializeCommits(parent);
        } else {
            return null;
        }
    }


    /**
     * Returns commit's timeStamp.
     *
     * @return TIMESTAMP
     **/
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * Returns commit's Stage.
     *
     * @return STAGE
     **/
    public HashMap getStage() {
        return stage;
    }

    /**
     * Returns name of commit.
     *
     * @return NAME
     **/
    public String getName() {
        return name;
    }

    /** Sets name of commit to SHA-1. **/
    public void namingCommit() {
        name = Utils.sha1(myFiles.toString()
                + logMsg + parent + timeStamp + "commit");
    }

    /** Prints the Commit. **/
    public void printCommit() {
        System.out.println("===");
        System.out.println("Commit " + name);
        System.out.println(timeStamp);
        System.out.println(logMsg);
        System.out.print("\n");

    }

    /** Gets the log message.
     *
     * @return STRING
     **/
    public String getMessage() {
        return logMsg;
    }
}
