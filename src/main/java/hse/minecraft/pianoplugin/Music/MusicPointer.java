package hse.minecraft.pianoplugin.Music;

import hse.minecraft.pianoplugin.menuSystem.Menu;
import hse.minecraft.pianoplugin.menuSystem.PianoMenu;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Material.GOLD_INGOT;

public class MusicPointer {
    PianoMenu menu;
    int position;

    MusicPointer(PianoMenu menu) {
        this.menu = menu;
    }

    private void addPointerItem(int i) {
        position = i;
        ItemStack pointer = menu.getItem("Pointer", GOLD_INGOT);
        menu.getInventory().setItem(i, pointer);

    }

    private void deletePointerItem() {
        ItemStack pointer = menu.getItem("Pointer", GOLD_INGOT);
        menu.getInventory().remove(pointer);
    }
}
