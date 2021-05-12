package hse.minecraft.pianoplugin.music;

import hse.minecraft.pianoplugin.menuSystem.PianoMenu;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Material.GOLD_INGOT;

public class MusicPointer {
    PianoMenu menu;
    int position;

    public MusicPointer(PianoMenu menu) {
        this.menu = menu;
    }

    public void addPointerItem(int i) {
        position = i;
        ItemStack pointer = menu.getItem("Pointer", GOLD_INGOT);
        menu.getInventory().setItem(i, pointer);

    }

    public void deletePointerItem() {
        ItemStack pointer = menu.getItem("Pointer", GOLD_INGOT);
        menu.getInventory().remove(pointer);
    }
}
