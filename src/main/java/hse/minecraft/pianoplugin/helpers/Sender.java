package hse.minecraft.pianoplugin.helpers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Sender {
    static public void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[PIANO] " + message);
    }

    static public void sendErrorConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[PIANO_ERROR] " + message);
    }
}
