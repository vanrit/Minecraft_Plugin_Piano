package hse.minecraft.pianoplugin.commands;

import hse.minecraft.pianoplugin.PianoPlugin;
import hse.minecraft.pianoplugin.menuSystem.PianoMenu;
import hse.minecraft.pianoplugin.menuSystem.PlaylistMenu;
import hse.minecraft.pianoplugin.music.SoundProducer;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PianoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

//        if (sender instanceof Player) {
//
//            Player player = (Player) sender;
//
//            player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 20.0F, 20.0F);
//            player.getWorld().playEffect(player.getLocation().add(0.0D, 1.5D, 0.0D), Effect.RECORD_PLAY, 2);
//            PianoMenu menu = new PianoMenu(PianoPlugin.getPlayerMenu(player));
//            menu.open();
//        }

        if (label.equalsIgnoreCase("menu")) {
            SoundProducer soundProducer = new SoundProducer();
            if (sender instanceof Player) {

                Player player = (Player) sender;

                if (args.length == 1) {
                    if (args[0].equals("piano")) {
                        player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 20.0F, 20.0F);
                        player.getWorld().playEffect(player.getLocation().add(0.0D, 1.5D, 0.0D), Effect.RECORD_PLAY, 2);
                        PianoMenu menu = new PianoMenu(PianoPlugin.getPlayerMenu(player));
                        menu.open();
                    } else if (args[0].equals("playlist")) {
                        //player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 20.0F, 20.0F);
                        //player.getWorld().playEffect(player.getLocation().add(0.0D, 1.5D, 0.0D), Effect.RECORD_PLAY, 2);
                        PlaylistMenu menu = new PlaylistMenu(PianoPlugin.getPlayerMenu(player));
                        menu.open();
                    } else sender.sendMessage(ChatColor.RED + "Use /menu piano \nor /menu playlist");
                } else {
                    sender.sendMessage(ChatColor.RED + "Use /menu piano \nor /menu playlist");
                }


            }
        } else {
            sender.sendMessage(ChatColor.RED + "Use /menu piano \nor /menu playlist");
        }

        return true;
    }
}
