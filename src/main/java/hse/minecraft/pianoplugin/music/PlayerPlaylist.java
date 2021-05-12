package hse.minecraft.pianoplugin.music;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class PlayerPlaylist implements Serializable {
    private final UUID uuid;

    private ArrayList<Music> playlist = new ArrayList<>();

    public PlayerPlaylist(UUID uuid) {
        this.uuid = uuid;
    }

    public ArrayList<Music> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(ArrayList<Music> playlist) {
        this.playlist = playlist;
    }

    public void addToPlaylist(Music music) {
        playlist.add(music);
    }

    public UUID getUniqueId() {
        return uuid;
    }
}
