package hse.minecraft.pianoplugin.Music;

import hse.minecraft.pianoplugin.PianoPlugin;
import hse.minecraft.pianoplugin.menuSystem.PianoMenu;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MusicConductor extends BukkitRunnable {
    private final Player player;
    private final Music tempMusic;
    private final PianoMenu menu;
    private int pos;
    private boolean isPlaced;

    List<String> blockList = new ArrayList<String>(PianoMenu.blockSounds.keySet());

    /*
    static
    {
        for (HashMap.Entry<String, Sound> entry : PianoMenu.blockSounds.entrySet()) {

        }
    }*/

    public MusicConductor(Player player, Music tempMusic, PianoMenu menu) {
        this.player = player;
        this.tempMusic = tempMusic;
        this.menu = menu;
    }

    @Override
    public void run() {
        conductPointer(tempMusic);
    }

    void conductPointer(Music music) {
        if (music == null) return;
        isPlaced = false;
        MusicPointer musicConductor = new MusicPointer(menu);

        ArrayList<MusicSample> vectorSamples = music.getMusicVector();
        Queue<MusicSample> musicSampleQueue = new LinkedList<MusicSample>(vectorSamples);

        Instant start = Instant.now();
        Instant finish;
        Instant sampleLength = null;

        /**
         System.out.println("~~~~~~~~");
         for (String item : blockList) {
         System.out.println(item);
         }
         System.out.println("~~~~~~~~");
         **/

        while (!musicSampleQueue.isEmpty()) {
            finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).toMillis();
            long minTime = 300;

            if (musicSampleQueue.peek() != null) {
                MusicSample tempSample = musicSampleQueue.peek();
                long sampleTime = tempSample.getTime();

                long dif = sampleTime - timeElapsed;
                if (!isPlaced && dif <= 220 && dif >= 200) {
                    int tempPos = blockList.indexOf(tempSample.SoundName);
                    pos = tempPos;

                    musicConductor.addPointerItem(pos);
                    //System.out.println("Start" + pos + "  " + sampleTime);
                    isPlaced = true;
                    player.updateInventory();

                    sampleLength = Instant.now();
                    musicSampleQueue.remove();

                    if (musicSampleQueue.peek() != null)
                        minTime = Math.min(musicSampleQueue.peek().getTime(), 200);
                    else
                        minTime = 200;
                }

                if (isPlaced && sampleLength != null && Duration.between(sampleLength, Instant.now()).toMillis() >= minTime) {
                    //musicSampleQueue.remove();
                    musicConductor.deletePointerItem();
                    //System.out.println("End" + pos + "  " + sampleTime + " Dur: " + Duration.between(sampleLength, Instant.now()).toMillis());
                    isPlaced = false;
                }
            }
        }
        musicConductor.deletePointerItem();

        BukkitRunnable br = PianoPlugin.tasks.remove(player.getUniqueId());
        if (br != null) br.cancel();
    }

    public int getPos() {
        return pos;
    }

    public boolean isPlaced() {
        return isPlaced;
    }
}
