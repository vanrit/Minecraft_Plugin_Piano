package hse.minecraft.pianoplugin.music;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerPlaylist implements Serializable {
    private ArrayList<Music> playlist = new ArrayList<>();

    public ArrayList<Music> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(ArrayList<Music> playlist) {
        this.playlist = playlist;
    }

    public void addToPlaylist(Music music) {
        playlist.add(music);
    }


}
