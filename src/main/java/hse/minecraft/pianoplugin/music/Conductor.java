package hse.minecraft.pianoplugin.music;

import hse.minecraft.pianoplugin.helpers.Sender;
import hse.minecraft.pianoplugin.menuSystem.PianoMenu;

public class Conductor {
    static Music music;

    static {
        music = new Music();
        setToDefault();
    }

    public static Music getMusic() {
        return music;
    }

    public static void setMusic(Music tempMusic) {
        music = tempMusic;
        typeMusicInfo();
    }

    static public void typeMusicInfo() {
        Sender.sendConsole(music.getName());
        for (MusicSample item : music.getMusicVector()) {
            Sender.sendConsole(item.getSoundName() + "  " + item.getTime());
        }
    }

    static public void setToDefault() {
        music = new Music();
        music.setName("Conductor Music");
        music.getMusicVector().add(new MusicSample(PianoMenu.blockList.get(1), 500));
        music.getMusicVector().add(new MusicSample(PianoMenu.blockList.get(2), 1000));
        music.getMusicVector().add(new MusicSample(PianoMenu.blockList.get(1), 1600));
        music.getMusicVector().add(new MusicSample(PianoMenu.blockList.get(4), 2000));
        music.getMusicVector().add(new MusicSample(PianoMenu.blockList.get(0), 2600));
        music.getMusicVector().add(new MusicSample(PianoMenu.blockList.get(3), 3000));
        music.getMusicVector().add(new MusicSample(PianoMenu.blockList.get(5), 3600));
        music.setTimeLength(3600);
    }
}
