package hse.minecraft.pianoplugin.Music;

import hse.minecraft.pianoplugin.menuSystem.PlayerMenuUtil;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundProducer {
    /**
     * Проигрывает звук по его имени
     *
     * @param player    игрок
     * @param soundName имя звука
     * @return была ли ошибка при попытке воспроизведения
     */
    static public boolean makeSound(Player player, String soundName) {
        if (soundName == null || player == null)
            return false;
        try {
            Sound sound = Sound.valueOf(soundName);
            player.playSound(player.getLocation(), sound, 20.0F, 20.0F);
            player.getWorld().playEffect(player.getLocation().add(0.0D, 1.5D, 0.0D), Effect.RECORD_PLAY, 2);
        } catch (IllegalArgumentException exception) {
            return false;
        }
        catch (Exception exception) {
            return false;
        }
        return true;
    }
}
