package hse.minecraft.pianoplugin.menuSystem;

import hse.minecraft.pianoplugin.PianoPlugin;
import hse.minecraft.pianoplugin.helpers.Sender;
import hse.minecraft.pianoplugin.music.Conductor;
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
    boolean startDelete = false;
    boolean setConductor = false;
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
                int idx = searchInPlaylist(event);
                if (idx == -1) break;

                //Устанавливаем дирижера
                if (setConductor) {
                    event.getWhoClicked().closeInventory();

                    tempMusic = playlist.get(idx);
                    Conductor.setMusic(tempMusic);

                    //Заново открываем меню, чтобы пластинки обновились
                    PlaylistMenu menu = new PlaylistMenu(PianoPlugin.getPlayerMenu(player));
                    menu.open();
                    break;
                }

                //Удаляем
                if (startDelete) {
                    event.getWhoClicked().closeInventory();

                    //Удаление
                    PianoPlugin.playerPlaylists.get(playerMenuUtil.getOwner().getUniqueId()).getPlaylist().remove(searchInPlaylist(event));

                    //Заново открываем меню, чтобы пластинки обновились
                    PlaylistMenu menu = new PlaylistMenu(PianoPlugin.getPlayerMenu(player));
                    menu.open();
                    break;
                }

                tempMusic = playlist.get(idx);
                Sender.sendConsole("Now playing" + event.getCurrentItem().getItemMeta().getDisplayName());

                if (tempMusic == null) break;

                removeTask(player, PianoPlugin.tasksMusic);

                br = new MusicPlayer(player, tempMusic, randomPitchFlag);

                startTask(br, player, PianoPlugin.tasksMusic);

                break;

            case JUKEBOX:
                randomPitchFlag = !randomPitchFlag;
                if (randomPitchFlag)
                    player.sendMessage(ChatColor.WHITE + "Now pitch is random");
                else
                    player.sendMessage(ChatColor.DARK_GRAY + "Now pitch is normal");
                break;

            case COMPASS:
                event.getWhoClicked().closeInventory();
                PianoMenu menu = new PianoMenu(PianoPlugin.getPlayerMenu(player));
                menu.open();
                break;
            case FLINT_AND_STEEL:
                startDelete = !startDelete;
                setConductor = false;
                break;
            case NETHER_STAR:
                setConductor = !setConductor;
                startDelete = false;
                break;
            case TNT:
                player.closeInventory();
                PianoPlugin.playerPlaylists.get(playerMenuUtil.getOwner().getUniqueId()).setPlaylist(new ArrayList<Music>());
                PlaylistMenu playlistMenu = new PlaylistMenu(PianoPlugin.getPlayerMenu(player));
                playlistMenu.open();
                break;
            case PAPER:
                Conductor.setToDefault();
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
        ItemStack pianoItem = getItem(ChatColor.YELLOW + "Open Piano menu", COMPASS);
        ItemStack startDeleteItem = getItem(ChatColor.RED + "Delete Item", FLINT_AND_STEEL);
        ItemStack setConductorItem = getItem(ChatColor.GOLD + "Set music as Conductor", NETHER_STAR);
        ItemStack deleteAllItem = getItem(ChatColor.DARK_RED + "Delete all", TNT);
        ItemStack setDefault = getItem(ChatColor.GRAY + "Set conductor to default", PAPER);

        int count = 0;
        for (Music item : playlist) {
            ItemStack tempItem = getItem(item.getName(), GOLD_RECORD);
            inventory.setItem(count, tempItem);
            count++;
        }

        inventory.setItem(9 * 4, setConductorItem);
        inventory.setItem(9 * 4 + 1, randomPitchItem);
        inventory.setItem(9 * 4 + 2, setDefault);
        inventory.setItem(9 * 4 + 5, startDeleteItem);
        inventory.setItem(9 * 4 + 6, deleteAllItem);
        inventory.setItem(9 * 4 + 7, pianoItem);
        inventory.setItem(9 * 4 + 8, exitItem);
    }

    private int searchInPlaylist(InventoryClickEvent event) {
        for (Music item : playlist) {
            if ((item.getName()).equals(event.getCurrentItem().getItemMeta().getDisplayName())) {
                return playlist.indexOf(item);
            }
        }
        return -1;
    }
}
