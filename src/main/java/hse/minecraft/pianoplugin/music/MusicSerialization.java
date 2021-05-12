package hse.minecraft.pianoplugin.music;

import java.io.*;
import java.util.ArrayList;

public class MusicSerialization {
    public static void saveMusic(ArrayList<PlayerPlaylist> music, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(music);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            System.out.println("Error in serialization");
        }
    }

    public static ArrayList<PlayerPlaylist> loadMusic(String path) {
        ArrayList<PlayerPlaylist> music = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);

            music = (ArrayList<PlayerPlaylist>) ois.readObject();

            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException ioe) {
            System.out.println("Error in deserialization");
        }

        return music;
    }

}
