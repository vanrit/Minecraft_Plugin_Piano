package hse.minecraft.pianoplugin.listeners;

import hse.minecraft.pianoplugin.PianoPlugin;
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
    public void join(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.sendMessage(ChatColor.GREEN + "Plugin Piano is working now");
        p.sendMessage(ChatColor.GREEN + "Type /piano");

        /*
        p.sendMessage("Size " + PianoPlugin.menuUtilHashMap.entrySet().size());
        for (Map.Entry<Player, PlayerMenuUtil> entry : PianoPlugin.menuUtilHashMap.entrySet()) {
            p.sendMessage(entry.getKey().getName());
        }*/

        if (PianoPlugin.playerPlaylists.get(p.getUniqueId()) != null)
            p.sendMessage("Your playlist was saved before");

        /*
        for (Map.Entry<UUID, PlayerPlaylist> entry : PianoPlugin.playerPlaylists.entrySet()) {
            p.sendMessage(entry.getKey().toString());
        }*/
    }
}
