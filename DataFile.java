
import java.io.*;
import java.util.ArrayList;

public class DataFile {
    
    public static void writeUserFile(User inputUser, String filename) 
    throws Exception {
        FileOutputStream out = new FileOutputStream(filename);
        ObjectOutputStream serializer = new ObjectOutputStream(out);
        serializer.writeObject(inputUser);
        out.close();
        serializer.close();
    }

    public static boolean userFileExists(String filename) {
        File userFile = new File(filename);
        if (userFile.exists() && !userFile.isDirectory())
            return true;
        return false;
    }

    public static User loadUserFile(String filename)
    throws Exception {
        FileInputStream in = new FileInputStream(filename);
        ObjectInputStream deserializer = new ObjectInputStream(in);
        User loadedUser = (User)deserializer.readObject();
        in.close();
        deserializer.close();

        return loadedUser;
    }
}