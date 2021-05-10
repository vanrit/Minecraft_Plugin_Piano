package hse.minecraft.pianoplugin.Listeners;

import hse.minecraft.pianoplugin.PianoPlugin;
import hse.minecraft.pianoplugin.Music.PlayerPlaylist;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;
import java.util.UUID;

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


        p.sendMessage("Size " + PianoPlugin.playerPlaylists.entrySet().size());
        for (Map.Entry<UUID, PlayerPlaylist> entry : PianoPlugin.playerPlaylists.entrySet()) {
            p.sendMessage(entry.getKey().toString());
        }
    }
}
