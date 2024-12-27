package org.hwabeag.attendance.inventorys;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hwabeag.attendance.config.ConfigManager;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class AttendanceItemSettingGUI implements Listener {
    private final Inventory inv;

    FileConfiguration AttendanceConfig = ConfigManager.getConfig("attendance");
    FileConfiguration PlayerConfig = ConfigManager.getConfig("player");

    private void initItemSetting(Player p) {
        String name = p.getName();
        int index = PlayerConfig.getInt("출석." + name + ".번호");
        if (AttendanceConfig.getConfigurationSection("attendance.물품." + index) != null) {
            for (String key : Objects.requireNonNull(AttendanceConfig.getConfigurationSection("attendance.물품." + index)).getKeys(false)) {
                @Nullable ItemStack item = AttendanceConfig.getItemStack("attendance.물품." + index + "." + key);
                inv.setItem(Integer.parseInt(key), item);
            }
        }
    }

    public AttendanceItemSettingGUI(Player p) {
        inv = Bukkit.createInventory(null,54,"보상세부설정");
        this.initItemSetting(p);
    }

    public void open(Player player){
        player.openInventory(inv);
    }

}
