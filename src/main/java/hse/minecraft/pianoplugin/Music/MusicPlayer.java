package hse.minecraft.pianoplugin.Music;

import hse.minecraft.pianoplugin.PianoPlugin;
import hse.minecraft.pianoplugin.menuSystem.PianoMenu;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MusicPlayer extends BukkitRunnable {
    private final Player player;
    private final Music tempMusic;
    private final boolean isRandomPlaying;

    public MusicPlayer(Player player, Music tempMusic, boolean isRandomPlaying) {
        this.player = player;
        this.tempMusic = tempMusic;
        this.isRandomPlaying = isRandomPlaying;
    }

    @Override
    public void run() {
        conductPointer(tempMusic);
    }

    void conductPointer(Music music) {
        if (music == null) return;

        Instant start = Instant.now();
        Instant finish;
        ArrayList<MusicSample> vectorSamples = music.getMusicVector();
        Queue<MusicSample> musicSampleQueue = new LinkedList<MusicSample>(vectorSamples);


        /*for (MusicSample sample : music.getMusicVector()) {
            util.getOwner().sendMessage(ChatColor.AQUA + sample.getSoundName());
         */

        while (!musicSampleQueue.isEmpty()) {
            finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).toMillis();

            if (musicSampleQueue.peek() != null) {
                long tempTime = musicSampleQueue.peek().getTime();
                if (tempTime <= timeElapsed + 3 && tempTime >= timeElapsed - 3) {
                    MusicSample musicSample = musicSampleQueue.remove();
                    if (!isRandomPlaying)
                        SoundProducer.makeSound(player, PianoMenu.blockSounds.get(musicSample.SoundName), musicSample.getPitchLevel());
                    else
                        SoundProducer.makeSound(player, PianoMenu.blockSounds.get(musicSample.SoundName), (float) Math.random());
                }
            }
        }

        BukkitRunnable br = PianoPlugin.tasks.remove(player.getUniqueId());
        if (br != null) br.cancel();
    }
}
