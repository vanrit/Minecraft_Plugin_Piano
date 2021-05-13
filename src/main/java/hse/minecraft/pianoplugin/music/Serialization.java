package hse.minecraft.pianoplugin.music;

import hse.minecraft.pianoplugin.helpers.Sender;

import java.io.*;
import java.util.ArrayList;

public class Serialization {
    public static void save(ArrayList<PlayerPlaylist> music, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(music);
            oos.close();
            fos.close();
            Sender.sendConsole("Serialization completed");
        } catch (IOException ioe) {
            Sender.sendErrorConsole("Error in serialization " + ioe.getMessage());
        }
    }

    public static ArrayList<PlayerPlaylist> load(String path) {
        ArrayList<PlayerPlaylist> music = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);

            music = (ArrayList<PlayerPlaylist>) ois.readObject();

            ois.close();
            fis.close();
            Sender.sendConsole("Deserialization completed");
        } catch (IOException ioe) {
            Sender.sendErrorConsole("Error in deserialization " + ioe.getMessage());
        } catch (ClassNotFoundException ioe) {
            Sender.sendErrorConsole("Error in class founding " + ioe.getMessage());
        }

        return music;
    }

}
