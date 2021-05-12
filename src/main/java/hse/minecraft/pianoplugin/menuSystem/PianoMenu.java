package hse.minecraft.pianoplugin.menuSystem;

import hse.minecraft.pianoplugin.Music.*;
import hse.minecraft.pianoplugin.PianoPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static org.bukkit.Material.*;
import static org.bukkit.Sound.*;


/**
 * Меню пианино
 */
public class PianoMenu extends Menu {
    boolean recording = false;
    Music music;
    float pitchLevel = 1F;
    SoundProducer soundProducer = new SoundProducer();

    //Мап содержащий название звука блока и сам Sound
    public static final SortedMap<String, Sound> blockSounds = new TreeMap<>();
    List<String> blockList = new ArrayList<String>(PianoMenu.blockSounds.keySet());

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
        ArrayList<Music> list;
        Music lastMusic;
        BukkitRunnable br;

        Player player = playerMenuUtil.getOwner();

        if (event == null) return;

        if (event.getCurrentItem() == null) return;

        if (event.getCurrentItem().getType() == null) return;

        if (event.getCurrentItem().getItemMeta() == null) return;

        if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        //TODO Печать инфы игроку:
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

                    if (music.getMusicVector().size() > 0) {
                        //Добавление музыки
                        music.setName(playerMenuUtil.getStart().toString());
                        music.setTimeLength(timeElapsed);
                        addMusic(music);
                    }

                    //System.out.println(playerMenuUtil.getFinish().toString());
                    recording = false;
                }
                break;
            //Случай выхода
            case BARRIER:
                playerMenuUtil.getOwner().sendMessage(ChatColor.RED + "Exit");
                event.getWhoClicked().closeInventory();
                break;

            //Если клик по стеклу, то значит надо воспроизводим звуки
            case STAINED_GLASS_PANE:
                int count = 0;
                for (HashMap.Entry<String, Sound> entry : blockSounds.entrySet()) {
                    if (count == 9) break;
                    //Выводим звук, по соответсвующему названию
                    if (event.getCurrentItem().getItemMeta().getDisplayName().equals(entry.getKey())) {
                        setTime(entry.getKey());
                        soundProducer.makeSound(player, entry.getValue(), pitchLevel);
                        //player.playSound(player.getLocation(), entry.getValue(), 20.0F, pitchLevel);
                        //player.getWorld().playEffect(player.getLocation().add(0.0D, 1.5D, 0.0D), Effect.RECORD_PLAY, 2);
                    }
                    count++;

                }
                //event.setCancelled(true);
                break;

            case PUMPKIN:
                list = PianoPlugin.playerPlaylists.get(playerMenuUtil.getOwner().getUniqueId()).getPlaylist();
                if (list.isEmpty()) break;
                lastMusic = list.get(list.size() - 1);

                removeTask(player, PianoPlugin.tasksMusic);

                //TODO сделать отдельно для песенки и учитывать, что человек мог уже запустить поток, тогда надо пррервать
                br = new MusicPlayer(player, lastMusic, false);

                startTask(br, player, lastMusic, PianoPlugin.tasksMusic);

                break;

            case JACK_O_LANTERN:
                list = PianoPlugin.playerPlaylists.get(playerMenuUtil.getOwner().getUniqueId()).getPlaylist();
                if (list.isEmpty()) break;
                lastMusic = list.get(list.size() - 1);

                removeTask(player, PianoPlugin.tasksMusic);

                br = new MusicPlayer(player, lastMusic, true);
                startTask(br, player, lastMusic, PianoPlugin.tasksMusic);

                break;

            case DIAMOND:
                list = PianoPlugin.playerPlaylists.get(playerMenuUtil.getOwner().getUniqueId()).getPlaylist();
                if (list.isEmpty()) break;
                lastMusic = list.get(list.size() - 1);

                //TODO не завершается таск по айди
                removeTask(player, PianoPlugin.tasksConductor);

                br = new MusicConductor(player, lastMusic, this);
                startTask(br, player, lastMusic, PianoPlugin.tasksConductor);

                break;

            case TNT:
                System.out.println(PianoPlugin.tasksMusic.entrySet().size());
                System.out.println(PianoPlugin.tasksConductor.entrySet().size());
                /*
                for (HashMap.Entry<UUID, BukkitRunnable> entry : PianoPlugin.tasks.entrySet()) {
                    System.out.println(entry.getKey() + " Завершилось ли: " + entry.getValue().isCancelled());
                }*/
                removeTask(player, PianoPlugin.tasksMusic);
                removeTask(player, PianoPlugin.tasksConductor);
                break;

            case LAVA_BUCKET:
                upPitch(player);
                break;

            case WATER_BUCKET:
                downPitch(player);
                break;
        }
    }

    /**
     * Добавляем item в инвентарь
     */
    @Override
    public void setMenuItems() {
        ItemStack startRecordingItem = new ItemStack(EMERALD, 1);
        ItemMeta yesMeta = startRecordingItem.getItemMeta();
        yesMeta.setDisplayName(ChatColor.GREEN + "Start Recording");
        startRecordingItem.setItemMeta(yesMeta);

        ItemStack exitItem = new ItemStack(BARRIER, 1);
        ItemMeta noMeta = exitItem.getItemMeta();
        noMeta.setDisplayName(ChatColor.DARK_RED + "Exit");
        exitItem.setItemMeta(noMeta);

        ItemStack conductorItem = getItem(ChatColor.DARK_GREEN + "Start Conductor", DIAMOND);
        ItemStack samePithItem = getItem(ChatColor.AQUA + "Last Music same pitch", PUMPKIN);
        ItemStack randomPitchItem = getItem(ChatColor.BLUE + "Last Music random pitch", JACK_O_LANTERN);
        ItemStack stopPlayingItem = getItem(ChatColor.RED + "Stop playing music", TNT);

        ItemStack upPitch = getItem(ChatColor.YELLOW + "UP PITCH", LAVA_BUCKET);
        ItemStack downPitch = getItem(ChatColor.BLUE + "DOWN PITCH", WATER_BUCKET);

        int count = 0;
        for (HashMap.Entry<String, Sound> entry : blockSounds.entrySet()) {
            if (count == 9) break;
            ItemStack tempItem = getGlassStack(entry.getKey(), count);
            //ItemStack tempItem = getGlassStack(entry.getKey(), count%2);
            setItemColumn(tempItem, count, 4);
            count++;
        }

        inventory.setItem(9 * 4, downPitch);
        inventory.setItem(9 * 4 + 1, upPitch);
        inventory.setItem(9 * 4 + 2, startRecordingItem);
        inventory.setItem(9 * 4 + 3, conductorItem);
        inventory.setItem(9 * 4 + 4, samePithItem);
        inventory.setItem(9 * 4 + 5, randomPitchItem);
        inventory.setItem(9 * 4 + 6, stopPlayingItem);
        inventory.setItem(9 * 4 + 8, exitItem);
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
            MusicSample musicSample = new MusicSample(name, timeElapsed);
            musicSample.setPitchLevel(pitchLevel);
            music.getMusicVector().add(musicSample);
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
        PianoPlugin.playerPlaylists.get(playerMenuUtil.getOwner().getUniqueId()).addToPlaylist(music);
    }

    public void startTask(BukkitRunnable br, Player player, Music lastMusic, HashMap<UUID, BukkitRunnable> tasks) {
        tasks.put(player.getUniqueId(), br);
        br.runTaskTimerAsynchronously(PianoPlugin.getPlugin(), 0, lastMusic.getTimeLength() / 60 * 20);
        //PianoPlugin.tasksConductor.put(player.getUniqueId(), br);
    }

    public void removeTask(Player player, HashMap<UUID, BukkitRunnable> tasks) {

        BukkitRunnable br = tasks.remove(player.getUniqueId());
        while (br != null) {
            System.out.println("Stop" + tasks.size() + " " + br.getTaskId());
            br.cancel();
            br = tasks.remove(player.getUniqueId());
        }
    }

    private void upPitch(Player player) {
        if (pitchLevel < 1)
            pitchLevel += 0.1F;
        player.sendMessage(ChatColor.GREEN + "Pitch level " + pitchLevel);
    }

    private void downPitch(Player player) {
        if (pitchLevel > 0.1F)
            pitchLevel -= 0.1F;
        player.sendMessage(ChatColor.GREEN + "Pitch level " + pitchLevel);
    }
}
