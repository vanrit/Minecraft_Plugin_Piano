package hse.minecraft.pianoplugin.Listeners;

import hse.minecraft.pianoplugin.menuSystem.Menu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * Слушатель для меню
 */
public class MenuListener implements Listener {
    @EventHandler
    public void onMenuClicked(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            if (e.getClickedInventory() == null)
                return;

            Player p = (Player) e.getWhoClicked();

            if (e.getClickedInventory().getHolder() == null)
                return;

            InventoryHolder holder = e.getClickedInventory().getHolder();

            if (holder instanceof Menu) {
                e.setCancelled(true);

                if (e.getCurrentItem() == null) {
                    return;
                }
                Menu menu = (Menu) holder;
                menu.handleMenu(e);
            }
        }
    }
}
