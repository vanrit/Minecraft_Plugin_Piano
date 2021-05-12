package hse.minecraft.pianoplugin.runnable;

import hse.minecraft.pianoplugin.PianoPlugin;
import hse.minecraft.pianoplugin.menuSystem.PianoMenu;
import hse.minecraft.pianoplugin.music.Music;
import hse.minecraft.pianoplugin.music.MusicPointer;
import hse.minecraft.pianoplugin.music.MusicSample;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class MusicConductor extends BukkitRunnable {
    private final Player player;
    private final Music tempMusic;
    private final PianoMenu menu;
    private int pos;
    int indexSound = 0;

    List<String> blockList = new ArrayList<String>(PianoMenu.blockSounds.keySet());
    ArrayList<Integer> musicScore;

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
        boolean isPlaced = false;
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

        musicScore = new ArrayList<Integer>(Collections.nCopies(vectorSamples.size(), 0));

        long minTime = 300;
        //TODO проверить почему не удаляется указатель, а только если нажать другую кнопку, сделать доп вывод
        //TODO проверить вылет, если очень частые клики
        while (!musicSampleQueue.isEmpty() && Duration.between(start, Instant.now()).toMillis() < music.getTimeLength() + 100) {
            finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).toMillis();

            if (musicSampleQueue.peek() != null) {
                MusicSample tempSample = musicSampleQueue.peek();
                long sampleTime = tempSample.getTime();

                long dif = sampleTime - timeElapsed;
                if (!isPlaced && dif <= 220 && dif >= 200) {
                    indexSound++;
                    int tempPos = blockList.indexOf(tempSample.getSoundName());
                    pos = tempPos;

                    musicConductor.addPointerItem(pos);
                    //System.out.println("Start " + Duration.between(start, Instant.now()).toMillis() + " MArk: " + sampleTime);
                    isPlaced = true;
                    player.updateInventory();

                    sampleLength = Instant.now();
                    //
                    musicSampleQueue.remove();

                    if (musicSampleQueue.peek() != null)
                        minTime = Math.min(musicSampleQueue.peek().getTime() - sampleTime, 1000);
                    else
                        minTime = 1000;

                    System.out.println(minTime);
                }

                if (isPlaced && sampleLength != null && Duration.between(sampleLength, Instant.now()).toMillis() >= minTime) {
                    //musicSampleQueue.remove();
                    musicConductor.deletePointerItem();
                    //System.out.println("End " + Duration.between(start, Instant.now()).toMillis() + " MArk:" + sampleTime + " Dur: " + Duration.between(sampleLength, Instant.now()).toMillis());
                    isPlaced = false;
                }
            }
        }

        //Последний висит не более  1000 мс
        start = Instant.now();
        while (Duration.between(start, Instant.now()).toMillis() < 1000) {
            if (Duration.between(start, Instant.now()).toMillis() >= 900) {
                musicConductor.deletePointerItem();
                player.updateInventory();
                System.out.println("End " + start);
                break;
            }
        }

        int resScore = 0;
        for (int item : musicScore) {
            if (item == 1)
                resScore++;
        }
        System.out.println("Result: " + resScore);
        checkResult(resScore);


        BukkitRunnable br = PianoPlugin.tasksConductor.remove(player.getUniqueId());
        this.cancel();
        //BukkitRunnable br = PianoPlugin.tasksConductor.remove(player.getUniqueId());
        //if (br != null) br.cancel();
    }

    public void checkClick() {
        if (musicScore != null && indexSound > 0 && indexSound <= musicScore.size()) {
            if (musicScore.get(indexSound - 1) == 0)
                musicScore.set(indexSound - 1, 1);
            else
                musicScore.set(indexSound - 1, -1);
        }
    }

    public int getPos() {
        return pos;
    }

    private void checkResult(int res) {
        String message = "";
        if (res == musicScore.size()) {
            message = ChatColor.GOLD + "Great result music score";
            sendResult(message, res);
        } else if (res == 0) {
            message = ChatColor.DARK_RED + "The worst result";
            sendResult(message, res);
        } else {
            message = ChatColor.DARK_GRAY + "Not bad, but not great.";
            sendResult(message, res);
        }
    }

    private void sendResult(String message, int res) {
        player.sendMessage(message);
        player.sendTitle(message, "Your score=" + res, 20, 60, 20);
    }
}
