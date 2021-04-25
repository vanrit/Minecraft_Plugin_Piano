package hse.minecraft.pianoplugin.Music;

import java.io.Serializable;

public class MusicSample implements Serializable {
    protected String SoundName;
    protected long time;

    public String getSoundName() {
        return SoundName;
    }

    public void setSoundName(String soundName) {
        SoundName = soundName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
