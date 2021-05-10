package hse.minecraft.pianoplugin.Music;

import java.io.Serializable;

public class MusicSample implements Serializable {
    protected String SoundName;
    protected long time;
    protected float pitchLevel = 1;

    public MusicSample(String soundName, long time) {
        SoundName = soundName;
        this.time = time;
    }

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

    public float getPitchLevel() {
        return pitchLevel;
    }

    public void setPitchLevel(float pitchLevel) {
        this.pitchLevel = pitchLevel;
    }
}
