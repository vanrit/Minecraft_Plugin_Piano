package hse.minecraft.pianoplugin.menuSystem;

import hse.minecraft.pianoplugin.Music.Music;
import hse.minecraft.pianoplugin.Music.MusicConductor;
import hse.minecraft.pianoplugin.Music.MusicSample;
import hse.minecraft.pianoplugin.PianoPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static org.bukkit.Material.*;
import static org.bukkit.Sound.*;


/**
 * Меню пианино
 */
public class PianoMenu extends Menu {
    boolean recording = false;
    Music music;

    //Мап содержащий название звука блока и сам Sound
    public static final HashMap<String, Sound> blockSounds = new HashMap<>();

    static {
        blockSounds.put("BASEDRUM", BLOCK_NOTE_BASEDRUM);
        blockSounds.put("BASS", BLOCK_NOTE_BASS);
        blockSounds.put("BELL", BLOCK_NOTE_BELL);
        blockSounds.put("CHIME", BLOCK_NOTE_CHIME);
        blockSounds.put("FLUTE", BLOCK_NOTE_FLUTE);
        blockSounds.put("GUITAR", BLOCK_NOTE_GUITAR);
        blockSounds.put("HARP", BLOCK_NOTE_HARP);
        //blockSounds.put("HAT", BLOCK_NOTE_HAT);
        blockSounds.put("PLING", BLOCK_NOTE_PLING);
        blockSounds.put("SNARE", BLOCK_NOTE_SNARE);
        //blockSounds.put("XYLOPHONE", BLOCK_NOTE_XYLOPHONE);
    }

    public PianoMenu(PlayerMenuUtil playerMenuUtil) {
        super(playerMenuUtil);
    }

    @Override
    public String getMenuName() {
        return "Piano";
    }

    @Override
    public int getSlots() {
        return 9 * 5;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = playerMenuUtil.getOwner();

        if (event == null) return;

        if (event.getCurrentItem() == null) return;

        if (event.getCurrentItem().getType() == null) return;

        if (event.getCurrentItem().getItemMeta() == null) return;

        if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        switch (event.getCurrentItem().getType()) {
            case EMERALD:
                if (!recording) {
                    music = new Music();
                    playerMenuUtil.setStart(Instant.now());
                    //System.out.println(playerMenuUtil.getStart().toString());
                    recording = true;
                } else {
                    playerMenuUtil.setFinish(Instant.now());
                    long timeElapsed = 0;
                    timeElapsed = Duration.between(playerMenuUtil.getStart(), playerMenuUtil.getFinish()).toMillis();
                    //System.out.println("MS: " + timeElapsed);

                    //Добавление музыки
                    music.setName(playerMenuUtil.getStart().toString());
                    music.setTimeLength(timeElapsed);
                    addMusic(music);

                    //System.out.println(playerMenuUtil.getFinish().toString());
                    recording = false;
                }
                break;
            //Случай выхода
            case BARRIER:
                playerMenuUtil.getOwner().sendMessage(ChatColor.RED + "Exit");
                event.getWhoClicked().closeInventory();
                break;

            //Если клик по стеклу, то значит надо воспроизводить звуки
            case STAINED_GLASS_PANE:
                int count = 0;
                for (HashMap.Entry<String, Sound> entry : blockSounds.entrySet()) {
                    if (count == 9) break;
                    //Выводим звук, по соответсвующему названию
                    if (event.getCurrentItem().getItemMeta().getDisplayName().equals(entry.getKey())) {
                        setTime(entry.getKey());
                        player.playSound(player.getLocation(), entry.getValue(), 20.0F, 20.0F);
                        player.getWorld().playEffect(player.getLocation().add(0.0D, 1.5D, 0.0D), Effect.RECORD_PLAY, 2);
                    }
                    count++;

                }
                //event.setCancelled(true);
                break;

            case TNT:
                ArrayList<Music> list = playerMenuUtil.getPlaylist();
                if (list.isEmpty()) break;
                Music lastMusic = list.get(list.size() - 1);

                BukkitRunnable br = PianoPlugin.tasks.remove(player.getUniqueId());
                if (br != null) br.cancel();

                //TODO сделать отдельно для песенки и учитывать, что человек мог уже запустить поток, тогда надо пррервать
                br = new MusicConductor(player, lastMusic);
                br.runTaskTimer(PianoPlugin.getPlugin(), 0, lastMusic.getTimeLength() / 60 * 20);
                PianoPlugin.tasks.put(playerMenuUtil.getUuid(), br);

                //deletePointerItem(4);
                break;

            case DIAMOND:
                System.out.println(PianoPlugin.tasks.entrySet().size());
                for (HashMap.Entry<UUID, BukkitRunnable> entry : PianoPlugin.tasks.entrySet()) {
                    System.out.println(entry.getKey() + " Завершилось ли: " + entry.getValue().isCancelled());
                }
                //addPointerItem(4);
                break;
        }
    }

    /**
     * Добавляем item в инвентарь
     */
    @Override
    public void setMenuItems() {
        ItemStack yes = new ItemStack(EMERALD, 1);
        ItemMeta yesMeta = yes.getItemMeta();
        yesMeta.setDisplayName(ChatColor.GREEN + "Start Recording");
        yes.setItemMeta(yesMeta);

        ItemStack exitItem = new ItemStack(BARRIER, 1);
        ItemMeta noMeta = exitItem.getItemMeta();
        noMeta.setDisplayName(ChatColor.RED + "Exit");
        exitItem.setItemMeta(noMeta);

        ItemStack addItem = getItem("Add", DIAMOND);
        ItemStack noItem = getItem("Play Last Music", TNT);

        int count = 0;
        for (HashMap.Entry<String, Sound> entry : blockSounds.entrySet()) {
            if (count == 9) break;
            ItemStack tempItem = getGlassStack(entry.getKey(), count);
            //ItemStack tempItem = getGlassStack(entry.getKey(), count%2);
            setItemColumn(tempItem, count, 4);
            count++;
        }

        inventory.setItem(9 * 4 + 2, yes);
        inventory.setItem(9 * 4 + 3, addItem);
        inventory.setItem(9 * 4 + 4, noItem);
        inventory.setItem(9 * 4 + 5, exitItem);
    }

    /**
     * Получаем item стекла нужного цвета, нужного имени
     *
     * @param name  имя
     * @param count номер цвета
     * @return стекло нужного цвета
     */
    public ItemStack getGlassStack(String name, int count) {
        ItemStack itemStack;
        if (count != 8)
            itemStack = new ItemStack(STAINED_GLASS_PANE, 1, (short) count);
        else
            itemStack = new ItemStack(STAINED_GLASS_PANE, 1, (short) 13);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }


    /**
     * Устанавливает переданный item в колонку
     *
     * @param item вещь
     * @param idx  индекс колонки
     * @param n    длина колонки вниз
     */
    private void setItemColumn(ItemStack item, int idx, int n) {
        for (int i = 1; i < n; i++) {
            inventory.setItem(idx + i * 9, item);
        }
    }

    public ItemStack getItem(String name, Material mat) {
        ItemStack itemStack = new ItemStack(mat, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private void addPointerItem(int i) {
        ItemStack pointer = getItem("Pointer", GOLD_INGOT);
        inventory.setItem(i, pointer);

    }

    private void deletePointerItem(int i) {
        ItemStack pointer = getItem("Pointer", GOLD_INGOT);
        inventory.remove(pointer);
    }

    /**
     * Запись времени воспроизведения звука игроком
     *
     * @param name имя звука
     */
    private void setTime(String name) {
        if (recording) {
            long timeElapsed = 0;
            timeElapsed = Duration.between(playerMenuUtil.getStart(), Instant.now()).toMillis();
            System.out.println(name + ":" + timeElapsed);
            music.getMusicVector().add(new MusicSample(name, timeElapsed));
        }
    }

    /**
     * Добавление музыки в плейлист и вывод звуков
     *
     * @param music музыка
     */
    private void addMusic(Music music) {
        System.out.println("ADDED to playlist " + music.getName() + "; Time:" + music.getTimeLength());
        for (int i = 0; i < music.getMusicVector().size(); i++) {
            System.out.println(music.getMusicVector().get(i).getTime() + "  " + music.getMusicVector().get(i).getSoundName());
        }
        playerMenuUtil.addToPlaylist(music);
    }

}
