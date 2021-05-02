package hse.minecraft.pianoplugin.commands;

import hse.minecraft.pianoplugin.PianoPlugin;
import hse.minecraft.pianoplugin.menuSystem.PianoMenu;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PianoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 20.0F, 20.0F);
            player.getWorld().playEffect(player.getLocation().add(0.0D, 1.5D, 0.0D), Effect.RECORD_PLAY, 2);
            PianoMenu menu = new PianoMenu(PianoPlugin.getPlayerMenu(player));
            menu.open();
        }

        return true;
    }
}
