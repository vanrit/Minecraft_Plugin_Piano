package hse.minecraft.pianoplugin.commands;

import hse.minecraft.pianoplugin.Music.SoundProducer;
import org.bukkit.ChatColor;
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
                    if (args[0].equals("sound"))
                        SoundProducer.makeRandomSound(player);
                    else if (args[0].equals("music"))
                        SoundProducer.makeRandomMinecraftMusic(player);
                    else if (!SoundProducer.makeSoundFromString(player, args[0]))
                        sender.sendMessage(ChatColor.RED + "Error in <sound> name\n ");
                } else {
                    sender.sendMessage(ChatColor.RED + "Use /plays <sound> \nor /plays music\nor /plays sound");
                }


            }
        }
        return true;
    }
}
