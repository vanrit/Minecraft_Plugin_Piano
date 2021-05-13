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
        if (playlist.size() < 36)
            playlist.add(music);
        else resizePlaylist(music);
    }

    public UUID getUniqueId() {
        return uuid;
    }

    private void resizePlaylist(Music music) {
        playlist.remove(0);
        playlist.add(music);
    }
}
