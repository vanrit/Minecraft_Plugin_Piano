package hse.minecraft.pianoplugin.music;

import java.io.*;
import java.util.ArrayList;

public class MusicSerialization {
    public static void saveMusic(ArrayList<MusicSample> music, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(music);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static ArrayList<MusicSample> loadMusic(String path) {
        ArrayList<MusicSample> music = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);

            music = (ArrayList<MusicSample>) ois.readObject();

            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException ioe) {
            ioe.printStackTrace();
        }

        return music;
    }

}
