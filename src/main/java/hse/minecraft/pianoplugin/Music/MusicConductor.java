package hse.minecraft.pianoplugin.Music;

import org.bukkit.ChatColor;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class MusicConductor {
        //Todo: сделать обработку данных песни как для указателя, так и для проигрывания
        void conductPointer(Music music)
        {
                Instant start = Instant.now();
                Instant finish;

                for (MusicSample sample:music.getMusic())
                {
                        finish = Instant.now();
                        long timeElapsed = Duration.between(start, finish).toMillis();
                        if (sample.getTime()==timeElapsed);
                        sample.getSoundName();
                }
        }

}
