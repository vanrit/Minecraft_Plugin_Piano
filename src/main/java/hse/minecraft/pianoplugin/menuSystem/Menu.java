package hse.minecraft.pianoplugin.menuSystem;

import hse.minecraft.pianoplugin.PianoPlugin;
import hse.minecraft.pianoplugin.helpers.Sender;
import hse.minecraft.pianoplugin.music.Music;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public abstract class Menu implements InventoryHolder {

    protected Inventory inventory;
    protected PlayerMenuUtil playerMenuUtil;

    public Menu(PlayerMenuUtil playerMenuUtil) {
        this.playerMenuUtil = playerMenuUtil;
    }

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract void handleMenu(InventoryClickEvent event);

    public abstract void setMenuItems();

    /**
     * Метод открытия меню заданного размера и названия
     */
    public void open() {
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());
        this.setMenuItems();
        playerMenuUtil.getOwner().openInventory(inventory);
    }

    /**
     * Получение инвентаря
     *
     * @return
     */
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void startTask(BukkitRunnable br, Player player, Music lastMusic, HashMap<UUID, BukkitRunnable> tasks) {
        tasks.put(player.getUniqueId(), br);
        //br.runTaskTimerAsynchronously(PianoPlugin.getPlugin(), 0, lastMusic.getTimeLength() / 60 * 20);
        br.runTaskAsynchronously(PianoPlugin.getPlugin());
        //PianoPlugin.tasksConductor.put(player.getUniqueId(), br);
    }

    public void removeTask(Player player, HashMap<UUID, BukkitRunnable> tasks) {
        BukkitRunnable br = tasks.remove(player.getUniqueId());
        while (br != null) {
            Sender.sendConsole("Stop" + tasks.size() + " Task id:" + br.getTaskId());
            br.cancel();
            br = tasks.remove(player.getUniqueId());
        }
    }

    /**
     * Устанавливает переданный item в колонку
     *
     * @param item вещь
     * @param idx  индекс колонки
     * @param n    длина колонки вниз
     */
    public void setItemColumn(ItemStack item, int idx, int n) {
        for (int i = 1; i < n; i++) {
            inventory.setItem(idx + i * 9, item);
        }
    }

    public ItemStack getItem(String name, Material mat) {
        ItemStack itemStack = new ItemStack(mat, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
