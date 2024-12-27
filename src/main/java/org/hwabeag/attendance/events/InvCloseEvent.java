package org.hwabeag.attendance.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.hwabeag.attendance.config.ConfigManager;
import org.jetbrains.annotations.Nullable;

public class InvCloseEvent implements Listener {

    FileConfiguration AttendanceConfig = ConfigManager.getConfig("attendance");
    FileConfiguration PlayerConfig = ConfigManager.getConfig("player");
    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        String name = p.getName();
        if (e.getView().getTitle().equals("보상세부설정")) {
            int index = PlayerConfig.getInt("출석." + name + ".번호");
            int N = 0;
            while (N <= 53) {
                if (e.getInventory().getItem(N) != null) {
                    @Nullable ItemStack item = e.getInventory().getItem(N);
                    AttendanceConfig.set("attendance.물품." + index + "." + N, item);
                    ConfigManager.saveConfigs();
                } else {
                    if (AttendanceConfig.getItemStack("attendance.물품." + index + "." + N) != null) {
                        AttendanceConfig.set("attendance.물품." + index + "." + N, null);
                        ConfigManager.saveConfigs();
                    }
                }
                N += 1;
            }
            ConfigManager.saveConfigs();
        }
    }
}
