package hse.minecraft.pianoplugin;

import hse.minecraft.pianoplugin.commands.PianoCommand;
import hse.minecraft.pianoplugin.commands.PlaySoundCommand;
import hse.minecraft.pianoplugin.helpers.Sender;
import hse.minecraft.pianoplugin.listeners.EntaranceListener;
import hse.minecraft.pianoplugin.listeners.MenuListener;
import hse.minecraft.pianoplugin.menuSystem.PlayerMenuUtil;
import hse.minecraft.pianoplugin.music.PlayerPlaylist;
import hse.minecraft.pianoplugin.music.Serialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class PianoPlugin extends JavaPlugin {
    private static Plugin plugin;
    //Данные каждого игрока
    //public static final HashMap<Player, PlayerMenuUtil> menuUtilHashMap = new HashMap<>();
    public static final HashMap<UUID, PlayerPlaylist> playerPlaylists = new HashMap<>();

    //Хэш мапы
    public static final HashMap<UUID, BukkitRunnable> tasksMusic = new HashMap<>();
    public static final HashMap<UUID, BukkitRunnable> tasksRandomMusic = new HashMap<>();
    public static final HashMap<UUID, BukkitRunnable> tasksConductor = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        Sender.sendConsole("Plugin Started");
        Sender.sendConsole("Created by issimonovich@edu.hse");
        loadPlaylists();
        PianoPlugin.plugin = this;
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        ArrayList<PlayerPlaylist> playlists = new ArrayList<>(playerPlaylists.values());
        Path temp = Paths.get("tempSave.txt");
        Serialization.save(playlists, "savePianoPlugin.txt");
        // Plugin shutdown logic
    }

    private void loadPlaylists() {
        ArrayList<PlayerPlaylist> playlists = Serialization.load("savePianoPlugin.txt");
        for (PlayerPlaylist item : playlists) {
            playerPlaylists.put(item.getUniqueId(), item);
        }
    }

    private void registerListeners() {
        getPlugin().getServer().getPluginManager().registerEvents(new EntaranceListener(), this);
        getPlugin().getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    private void registerCommands() {
        getCommand("piano").setExecutor(new PianoCommand());
        getCommand("plays").setExecutor(new PlaySoundCommand());
    }

    /**
     * Получаем меню игрока
     *
     * @param player игрок
     * @return класс для меню
     */
    public static PlayerMenuUtil getPlayerMenu(Player player) {
        PlayerMenuUtil resMenuUtil;
        //if (menuUtilHashMap.containsKey(player))
        // return menuUtilHashMap.get(player);
        //else {

        //Посмотрите, есть ли у игрока playerMenuUtil, сохраненный для него
        //Если нет, то добавляем

        resMenuUtil = new PlayerMenuUtil(player);

        if (!playerPlaylists.containsKey(player.getUniqueId())) {
            PlayerPlaylist playerPlaylist = new PlayerPlaylist(player.getUniqueId());
            playerPlaylists.put(player.getUniqueId(), playerPlaylist);
        }
        //menuUtilHashMap.put(player, resMenuUtil);
        return resMenuUtil;
        //}
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
