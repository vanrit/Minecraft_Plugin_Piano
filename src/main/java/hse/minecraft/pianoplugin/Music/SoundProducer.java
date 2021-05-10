package hse.minecraft.pianoplugin.Music;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public class SoundProducer {

    static ArrayList<Sound> minecraftMusic = new ArrayList<Sound>();

    static {
        minecraftMusic.add(Sound.MUSIC_CREATIVE);
        minecraftMusic.add(Sound.MUSIC_CREDITS);
        minecraftMusic.add(Sound.MUSIC_DRAGON);
        minecraftMusic.add(Sound.MUSIC_END);
        minecraftMusic.add(Sound.MUSIC_GAME);
        minecraftMusic.add(Sound.MUSIC_MENU);
        minecraftMusic.add(Sound.MUSIC_NETHER);
    }

    /**
     * Проигрывает звук по его имени
     *
     * @param player    игрок
     * @param soundName имя звука
     * @return была ли ошибка при попытке воспроизведения
     */
    static public boolean makeSoundFromString(Player player, String soundName) {
        if (soundName == null || player == null)
            return false;
        try {
            Sound sound = Sound.valueOf(soundName);
            makeSound(player, sound, (float) Math.random());
            player.sendMessage(ChatColor.ITALIC + "Playing Minecraft sound:" + soundName);
        } catch (IllegalArgumentException exception) {
            return false;
        }
        return true;
    }

    //Todo сделать отдельно методы для рандомного питча и нет, и кнопки соответсвенно
    static public void makeSound(Player player, Sound sound, float pitchLevel) {
        if (pitchLevel > 1) pitchLevel = 1;
        pitchLevel = Math.abs(pitchLevel);
        //player.playSound(player.getLocation(), sound, 20.0F, 20.0F);
        player.playSound(player.getLocation(), sound, 20.0F, 2F * pitchLevel);
        player.getWorld().playEffect(player.getLocation().add(0.0D, 1.5D, 0.0D), Effect.RECORD_PLAY, 2);
    }

    static public void makeRandomMinecraftMusic(Player player) {
        player.sendMessage(ChatColor.ITALIC + "Playing random Minecraft music:" + minecraftMusic.get((int) (Math.random() * 6)).toString());
        makeSound(player, minecraftMusic.get((int) (Math.random() * minecraftMusic.size())), (float) Math.random());
    }

    static public void makeRandomSound(Player player) {
        makeSound(player, Sound.values()[new Random().nextInt(Sound.values().length)], (float) Math.random());
    }
}
