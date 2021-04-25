package hse.minecraft.pianoplugin.Music;

import java.io.Serializable;
import java.util.ArrayList;

public class Music implements Serializable {
    protected String name;
    protected int timeLength;
    protected ArrayList<MusicSample> music;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }

    public ArrayList<MusicSample> getMusic() {
        return music;
    }

    public void setMusic(ArrayList<MusicSample> music) {
        this.music = music;
    }
}
