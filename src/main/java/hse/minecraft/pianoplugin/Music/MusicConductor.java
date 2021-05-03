package hse.minecraft.pianoplugin.Music;

import hse.minecraft.pianoplugin.PianoPlugin;
import hse.minecraft.pianoplugin.menuSystem.PianoMenu;
import hse.minecraft.pianoplugin.menuSystem.PlayerMenuUtil;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MusicConductor extends BukkitRunnable {
    private final Player player;
    private final Music tempMusic;

    public MusicConductor(Player player, Music tempMusic) {
        this.player = player;
        this.tempMusic = tempMusic;
    }

    @Override
    public void run() {
        conductPointer(tempMusic);
    }

    //Todo: сделать обработку данных песни как для указателя, так и для проигрывателя
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
                if (tempTime <= timeElapsed + 10 && tempTime >= timeElapsed - 10) {
                    SoundProducer.makeSound(player, PianoMenu.blockSounds.get(musicSampleQueue.remove().SoundName));
                }
            }
        }

        BukkitRunnable br = PianoPlugin.tasks.remove(player.getUniqueId());
        if (br != null) br.cancel();
    }
}
