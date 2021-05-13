package hse.minecraft.pianoplugin.menuSystem;

import hse.minecraft.pianoplugin.PianoPlugin;
import hse.minecraft.pianoplugin.helpers.Sender;
import hse.minecraft.pianoplugin.music.Music;
import hse.minecraft.pianoplugin.music.MusicForConductor;
import hse.minecraft.pianoplugin.music.MusicSample;
import hse.minecraft.pianoplugin.music.SoundProducer;
import hse.minecraft.pianoplugin.runnable.MusicConductor;
import hse.minecraft.pianoplugin.runnable.MusicPlayer;
import org.bukkit.ChatColor;
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
    int hitCounter = 0;
    SoundProducer soundProducer = new SoundProducer();

    //Мап содержащий название звука блока и сам Sound
    public static final SortedMap<String, Sound> blockSounds = new TreeMap<>();
    public static List<String> blockList;

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

        blockList = new ArrayList<String>(PianoMenu.blockSounds.keySet());

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
                    player.sendMessage(ChatColor.GREEN + "Recording is started");
                    music = new Music();
                    playerMenuUtil.setStart(Instant.now());
                    //Sender.sendConsole(playerMenuUtil.getStart().toString());
                    recording = true;
                } else {
                    playerMenuUtil.setFinish(Instant.now());
                    long timeElapsed = 0;
                    timeElapsed = Duration.between(playerMenuUtil.getStart(), playerMenuUtil.getFinish()).toMillis();
                    //Sender.sendConsole("MS: " + timeElapsed);

                    if (music.getMusicVector().size() > 0) {
                        player.sendMessage(ChatColor.GREEN + "Music recorded");
                        //Добавление музыки
                        music.setName(playerMenuUtil.getStart().toString());
                        music.setTimeLength(timeElapsed);
                        addMusic(music);
                    } else
                        player.sendMessage(ChatColor.RED + "No sounds recorded");

                    //Sender.sendConsole(playerMenuUtil.getFinish().toString());
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

                        //Обработка кликов, если включен поток с дирижером
                        MusicConductor conductor = (MusicConductor) PianoPlugin.tasksConductor.get(player.getUniqueId());
                        if (conductor != null && blockList.indexOf(entry.getKey()) == conductor.getPos()) {
                            //hitCounter++;
                            //Sender.sendConsole("Hit counter" + hitCounter);
                            conductor.checkClick();
                        }

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

                br = new MusicPlayer(player, lastMusic, false);

                startTask(br, player, PianoPlugin.tasksMusic);

                break;

            case JACK_O_LANTERN:
                list = PianoPlugin.playerPlaylists.get(playerMenuUtil.getOwner().getUniqueId()).getPlaylist();
                if (list.isEmpty()) break;
                lastMusic = list.get(list.size() - 1);

                removeTask(player, PianoPlugin.tasksRandomMusic);

                br = new MusicPlayer(player, lastMusic, true);
                startTask(br, player, PianoPlugin.tasksRandomMusic);

                break;

            case DIAMOND:

                removeTask(player, PianoPlugin.tasksConductor);

                br = new MusicConductor(player, MusicForConductor.getMusic(), this);
                startTask(br, player, PianoPlugin.tasksConductor);

                break;

            case GREEN_RECORD:
                event.getWhoClicked().closeInventory();
                PlaylistMenu menu = new PlaylistMenu(PianoPlugin.getPlayerMenu(player));
                menu.open();
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
        ItemStack stopPlayingItem = getItem(ChatColor.RED + "Open my playlist", GREEN_RECORD);

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
     * Запись времени воспроизведения звука игроком
     *
     * @param name имя звука
     */
    private void setTime(String name) {
        if (recording) {
            long timeElapsed = 0;
            timeElapsed = Duration.between(playerMenuUtil.getStart(), Instant.now()).toMillis();
            Sender.sendConsole(name + ":" + timeElapsed);
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
        Sender.sendConsole("ADDED to playlist " + music.getName() + "; Time:" + music.getTimeLength());
        for (int i = 0; i < music.getMusicVector().size(); i++) {
            Sender.sendConsole(music.getMusicVector().get(i).getTime() + "  " + music.getMusicVector().get(i).getSoundName());
        }
        PianoPlugin.playerPlaylists.get(playerMenuUtil.getOwner().getUniqueId()).addToPlaylist(music);
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

    private void allTasksRemove(Player player) {
        Sender.sendConsole("TASK MUSIC SIZE:" + PianoPlugin.tasksMusic.entrySet().size());
        Sender.sendConsole("TASK MUSIC RANDOM SIZE:" + PianoPlugin.tasksMusic.entrySet().size());
        Sender.sendConsole("TASK CONDUCTOR SIZE:" + PianoPlugin.tasksConductor.entrySet().size());


        removeTask(player, PianoPlugin.tasksMusic);
        removeTask(player, PianoPlugin.tasksRandomMusic);
        removeTask(player, PianoPlugin.tasksConductor);

        for (HashMap.Entry<UUID, BukkitRunnable> entry : PianoPlugin.tasksMusic.entrySet()) {
            Sender.sendConsole(entry.getKey() + " Завершилось ли: " + entry.getValue().isCancelled());
        }

        for (HashMap.Entry<UUID, BukkitRunnable> entry : PianoPlugin.tasksRandomMusic.entrySet()) {
            Sender.sendConsole(entry.getKey() + " Завершилось ли: " + entry.getValue().isCancelled());
        }

        for (HashMap.Entry<UUID, BukkitRunnable> entry : PianoPlugin.tasksConductor.entrySet()) {
            Sender.sendConsole(entry.getKey() + " Завершилось ли: " + entry.getValue().isCancelled());
        }
    }
}
