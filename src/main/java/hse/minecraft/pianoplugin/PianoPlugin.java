package hse.minecraft.pianoplugin;

import hse.minecraft.pianoplugin.Listeners.EntaranceListener;
import hse.minecraft.pianoplugin.Listeners.MenuListener;
import hse.minecraft.pianoplugin.commands.PianoCommand;
import hse.minecraft.pianoplugin.commands.PlaySoundCommand;
import hse.minecraft.pianoplugin.menuSystem.PlayerMenuUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class PianoPlugin extends JavaPlugin {
    private static Plugin plugin;
    //Меню каждого игрока
    private static final HashMap<Player, PlayerMenuUtil> menuUtilHashMap = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[HSE]~~~~~~~~ Piano plugin Started ~~~~~~~~");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[HSE] Created by John Sim");
        PianoPlugin.plugin = this;
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
        if (menuUtilHashMap.containsKey(player))
            return menuUtilHashMap.get(player);
        else {
            //Посмотрите, есть ли у игрока playerMenuUtil, сохраненный для него
            //Если нет, то добавляем
            resMenuUtil = new PlayerMenuUtil(player);
            menuUtilHashMap.put(player, resMenuUtil);
            return resMenuUtil;
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}