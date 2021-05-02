package hse.minecraft.pianoplugin.menuSystem;

import hse.minecraft.pianoplugin.Music.Music;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Класс игрока
 */
public class PlayerMenuUtil {
    private Player owner;
    private final UUID uuid;
    private Instant start;
    private Instant finish;

    private ArrayList<Music> playlist = new ArrayList<>();

    public PlayerMenuUtil(Player owner) {
        this.owner = owner;
        uuid = owner.getUniqueId();
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getFinish() {
        return finish;
    }

    public void setFinish(Instant finish) {
        this.finish = finish;
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

    public UUID getUuid() {
        return uuid;
    }
}
