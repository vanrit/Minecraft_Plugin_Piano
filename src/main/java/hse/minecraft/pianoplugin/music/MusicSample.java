package hse.minecraft.pianoplugin.music;

import java.io.Serializable;

public class MusicSample implements Serializable {
    protected String soundName;
    protected long time;
    protected float pitchLevel = 1.0F;

    public MusicSample(String soundName, long time) {
        this.soundName = soundName;
        this.time = time;
    }

    public MusicSample(String soundName, long time, float pitchLevel) {
        this.soundName = soundName;
        this.time = time;
        this.pitchLevel = pitchLevel;
    }

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
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
