package hse.minecraft.pianoplugin.Music;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerPlaylist {
    Player player;

    public PlayerPlaylist(Player player) {
        this.player = player;
    }

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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
