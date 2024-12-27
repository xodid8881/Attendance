package org.hwabeag.attendance.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.hwabeag.attendance.Attendance;
import org.hwabeag.attendance.config.ConfigManager;
import org.hwabeag.attendance.inventorys.AttendanceGUI;
import org.hwabeag.attendance.inventorys.AttendanceItemSettingGUI;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.Material.CHEST_MINECART;

public class InvClickEvent implements Listener {

    FileConfiguration AttendanceConfig = ConfigManager.getConfig("attendance");
    FileConfiguration PlayerConfig = ConfigManager.getConfig("player");
    String Prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AttendanceConfig.getString("attendance.prefix")));

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null)
            return;
        if (e.getCurrentItem() != null) {
            Player player = (Player) e.getWhoClicked();
            String name = player.getName();
            if (e.getView().getTitle().equals("출석보상설정")) {
                Material clickitem = e.getCurrentItem().getType();
                if (clickitem == CHEST_MINECART) {
                    String clickitemname = e.getCurrentItem().getItemMeta().getDisplayName();
                    String clickitemname1 = clickitemname.replace("§a[", "");
                    String clickitemname2 = clickitemname1.replace("일차] §f보상", "");
                    PlayerConfig.set("출석." + name + ".번호", Integer.parseInt(clickitemname2));
                    ConfigManager.saveConfigs();
                    e.getInventory().clear();
                    player.closeInventory();
                    ConfigManager.saveConfigs();
                    getServer().getScheduler().scheduleSyncDelayedTask(Attendance.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            AttendanceItemSettingGUI inv = new AttendanceItemSettingGUI(player);
                            inv.open(player);
                        }
                    }, 20);
                }
                return;
            }
            if (e.getView().getTitle().equals("출석")) {
                e.setCancelled(true);
                String clickitemname = e.getCurrentItem().getItemMeta().getDisplayName();
                String clickitemname1 = clickitemname.replace("§a[", "");
                String clickitemname2 = clickitemname1.replace("일차] §f보상", "");
                int check = PlayerConfig.getInt("출석." + name + ".Check");
                int playerday = PlayerConfig.getInt("출석." + name + ".Day");
                if (Integer.parseInt(clickitemname2) == check + 1) {
                    SimpleDateFormat Day = new SimpleDateFormat("yyyyMMdd");
                    Day.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                    Date now = new Date();
                    String nowTime1 = Day.format(now);
                    if (playerday <= Integer.parseInt(nowTime1)) {
                        if (AttendanceConfig.getConfigurationSection("attendance.물품." + Integer.parseInt(clickitemname2)) != null) {
                            for (String key : Objects.requireNonNull(AttendanceConfig.getConfigurationSection("attendance.물품." + Integer.parseInt(clickitemname2))).getKeys(false)) {
                                @Nullable ItemStack item = AttendanceConfig.getItemStack("attendance.물품." + Integer.parseInt(clickitemname2) + "." + key);
                                player.getInventory().addItem(item);
                            }
                        }
                        PlayerConfig.set("출석." + name + ".Check", (check + 1));
                        PlayerConfig.set("출석." + name + ".Day", (Integer.parseInt(nowTime1) + 1));
                        player.sendMessage(Prefix + " " + clickitemname2 + "일의 출석 보상을 수령했습니다.");
                        if (PlayerConfig.getInt("출석." + name + ".Check") == 28) {
                            PlayerConfig.set("출석." + name + ".Check", 0);
                            player.sendMessage(Prefix + " 28일간의 출석을 완료하여 출석판이 초기화 됩니다.");
                        }
                        ConfigManager.saveConfigs();

                        e.getInventory().clear();
                        player.closeInventory();
                        ConfigManager.saveConfigs();
                        getServer().getScheduler().scheduleSyncDelayedTask(Attendance.getPlugin(), new Runnable() {
                            @Override
                            public void run() {
                                AttendanceGUI inv = new AttendanceGUI(player);
                                inv.open(player);
                            }
                        }, 20);
                    } else if (playerday > Integer.parseInt(nowTime1)) {
                        player.sendMessage(Prefix + " 당신은 이미 오늘의 보상을 받았습니다.");
                        e.getInventory().clear();
                        player.closeInventory();
                        ConfigManager.saveConfigs();
                        getServer().getScheduler().scheduleSyncDelayedTask(Attendance.getPlugin(), new Runnable() {
                            @Override
                            public void run() {
                                AttendanceGUI inv = new AttendanceGUI(player);
                                inv.open(player);
                            }
                        }, 20);
                    }
                } else if (Integer.parseInt(clickitemname2) < check + 1) {
                    player.sendMessage(Prefix + " 이미 받은 보상입니다!");
                    e.getInventory().clear();
                    player.closeInventory();
                    ConfigManager.saveConfigs();
                    getServer().getScheduler().scheduleSyncDelayedTask(Attendance.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            AttendanceGUI inv = new AttendanceGUI(player);
                            inv.open(player);
                        }
                    }, 20);
                } else if (Integer.parseInt(clickitemname2) > check + 1) {
                    player.sendMessage(Prefix + " 받지 않은 보상이 존재해서 받을 수 없습니다.");
                    e.getInventory().clear();
                    player.closeInventory();
                    ConfigManager.saveConfigs();
                    getServer().getScheduler().scheduleSyncDelayedTask(Attendance.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            AttendanceGUI inv = new AttendanceGUI(player);
                            inv.open(player);
                        }
                    }, 20);
                }
            }
        }
    }
}
