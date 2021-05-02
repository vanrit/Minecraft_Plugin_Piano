package hse.minecraft.pianoplugin.commands;

import hse.minecraft.pianoplugin.Music.SoundProducer;
import hse.minecraft.pianoplugin.PianoPlugin;
import hse.minecraft.pianoplugin.menuSystem.PianoMenu;
import javafx.scene.paint.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlaySoundCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("plays")) {
            if (sender instanceof Player) {

                Player player = (Player) sender;

                if (args.length == 1) {
                    if (!SoundProducer.makeSound(player, args[0]))
                        sender.sendMessage(ChatColor.RED + "Error in <sound> name\n");
                } else {
                    sender.sendMessage(ChatColor.RED + "Use /plays <sound>");
                }


            }
        }
        return true;
    }
}
