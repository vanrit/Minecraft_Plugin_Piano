package hse.minecraft.pianoplugin.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Обработка входа на сервер
 */
public class EntaranceListener implements Listener {
    @EventHandler
    public void join(PlayerJoinEvent e)
    {
        Player p=e.getPlayer();
        p.sendMessage(ChatColor.GREEN+"Plugin Piano is working now");
        p.sendMessage(ChatColor.GREEN+"Type /piano");
    }
}
