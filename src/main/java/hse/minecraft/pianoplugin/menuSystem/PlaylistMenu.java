package hse.minecraft.pianoplugin.menuSystem;

import hse.minecraft.pianoplugin.PianoPlugin;
import hse.minecraft.pianoplugin.helpers.Sender;
import hse.minecraft.pianoplugin.music.Music;
import hse.minecraft.pianoplugin.runnable.MusicPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

import static org.bukkit.Material.*;


/**
 * Меню пианино
 */
public class PlaylistMenu extends Menu {
    boolean randomPitchFlag = false;
    ArrayList<Music> playlist;

    public PlaylistMenu(PlayerMenuUtil playerMenuUtil) {
        super(playerMenuUtil);
    }

    @Override
    public String getMenuName() {
        return "Playlist";
    }

    @Override
    public int getSlots() {
        return 9 * 5;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Music tempMusic;
        BukkitRunnable br;

        Player player = playerMenuUtil.getOwner();

        if (event == null) return;

        if (event.getCurrentItem() == null) return;

        if (event.getCurrentItem().getType() == null) return;

        if (event.getCurrentItem().getItemMeta() == null) return;

        if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        switch (event.getCurrentItem().getType()) {
            //Случай выхода
            case BARRIER:
                playerMenuUtil.getOwner().sendMessage(ChatColor.RED + "Exit");
                event.getWhoClicked().closeInventory();
                break;

            case GOLD_RECORD:
                tempMusic = null;
                Sender.sendConsole("Now playing" + event.getCurrentItem().getItemMeta().getDisplayName());
                for (Music item : playlist) {
                    if ((ChatColor.LIGHT_PURPLE + item.getName()).equals(event.getCurrentItem().getItemMeta().getDisplayName())) {
                        tempMusic = item;
                        break;
                    }
                }

                if (tempMusic == null) break;

                removeTask(player, PianoPlugin.tasksMusic);

                br = new MusicPlayer(player, tempMusic, randomPitchFlag);

                startTask(br, player, tempMusic, PianoPlugin.tasksMusic);

                break;

            case JUKEBOX:
                randomPitchFlag = !randomPitchFlag;
                if (randomPitchFlag)
                    player.sendMessage("Now pitch is random");
                else
                    player.sendMessage("Now pitch is normal");
                break;

        }
    }

    /**
     * Добавляем item в инвентарь
     */
    @Override
    public void setMenuItems() {
        //Получаем playlist
        playlist = PianoPlugin.playerPlaylists.get(playerMenuUtil.getOwner().getUniqueId()).getPlaylist();

        ItemStack exitItem = new ItemStack(BARRIER, 1);
        ItemMeta noMeta = exitItem.getItemMeta();
        noMeta.setDisplayName(ChatColor.DARK_RED + "Exit");
        exitItem.setItemMeta(noMeta);


        ItemStack randomPitchItem = getItem(ChatColor.BLUE + "Change Music Pitch type", JUKEBOX);

        int count = 0;
        for (Music item : playlist) {
            ItemStack tempItem = getItem(ChatColor.LIGHT_PURPLE + item.getName(), GOLD_RECORD);
            inventory.setItem(count, tempItem);
            count++;
        }

        inventory.setItem(9 * 4 + 4, randomPitchItem);
        inventory.setItem(9 * 4 + 8, exitItem);
    }
}
