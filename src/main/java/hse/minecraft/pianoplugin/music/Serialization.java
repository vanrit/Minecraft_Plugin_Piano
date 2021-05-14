package hse.minecraft.pianoplugin.music;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hse.minecraft.pianoplugin.helpers.Sender;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Serialization {
    public static void saveStandart(ArrayList<PlayerPlaylist> music, String path) {
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


    public static ArrayList<PlayerPlaylist> loadStandart(String path) {
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

    public static void savePlaylist(ArrayList<PlayerPlaylist> music, String path) {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(path)) {
            gson.toJson(music, writer);
            Sender.sendConsole("Serialization playlists completed");
        } catch (IOException ioe) {
            Sender.sendErrorConsole("Error in serialization " + ioe.getMessage());
        }
    }

    public static ArrayList<PlayerPlaylist> loadPlaylist(String path) {
        Gson gson = new Gson();
        ArrayList<PlayerPlaylist> music = new ArrayList<>();
        Type listOfMyClassObject = new TypeToken<ArrayList<PlayerPlaylist>>() {
        }.getType();
        try (FileReader reader = new FileReader(path)) {
            music = gson.fromJson(reader, listOfMyClassObject);
            Sender.sendConsole("Deserialization playlist completed");
        } catch (IOException ioe) {
            Sender.sendErrorConsole("Error in deserialization " + ioe.getMessage());
            return null;
        }

        return music;
    }

    public static void saveMusic(Music music, String path) {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(path)) {
            gson.toJson(music, writer);
            Sender.sendConsole("Serialization conductor music completed");
        } catch (IOException ioe) {
            Sender.sendErrorConsole("Error in serialization " + ioe.getMessage());
        }
    }

    public static Music loadMusic(String path) {
        Gson gson = new Gson();
        Music music = new Music();
        try (FileReader reader = new FileReader(path)) {
            music = gson.fromJson(reader, Music.class);
            Sender.sendConsole("Deserialization conductor music completed");
        } catch (IOException ioe) {
            Sender.sendErrorConsole("Error in deserialization " + ioe.getMessage());
            return null;
        }

        return music;
    }

}
