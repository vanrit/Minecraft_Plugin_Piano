package hse.minecraft.pianoplugin.menuSystem;

import org.bukkit.entity.Player;

/**
 * Класс игрока
 */
public class PlayerMenuUtil {
    private Player owner;

    public PlayerMenuUtil(Player owner) {
        this.owner = owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }
}
