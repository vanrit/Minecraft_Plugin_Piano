package hse.minecraft.pianoplugin.music;

import java.io.Serializable;
import java.util.ArrayList;

public class Music implements Serializable {
    protected String name;
    protected long timeLength;
    protected ArrayList<MusicSample> musicVector = new ArrayList<>();
    ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(long timeLength) {
        this.timeLength = timeLength;
    }

    public ArrayList<MusicSample> getMusicVector() {
        return musicVector;
    }

    public void setMusicVector(ArrayList<MusicSample> musicVector) {
        this.musicVector = musicVector;
    }
}
