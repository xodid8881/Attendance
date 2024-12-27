package org.hwabeag.attendance.inventorys;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hwabeag.attendance.config.ConfigManager;

import java.util.Arrays;

public class AttendanceGUI implements Listener {
    private final Inventory inv;

    FileConfiguration PlayerConfig = ConfigManager.getConfig("player");
    private void initItemSetting(Player p) {
        String name = p.getName();
        int i = 0;
        int[ ] Index = { 10,11,12,13,14,15,16, 19,20,21,22,23,24,25, 28,29,30,31,32,33,34, 37,38,39,40,41,42,43};
        while (i <= 27) {
            int check = PlayerConfig.getInt("출석." + name + ".Check");
            if (check == i){
                ItemStack item = new ItemStack(Material.CHEST_MINECART, 1);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a[" + (i+1) + "일차] &f보상"));
                itemMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&a- &f클릭 시 보상을 받습니다.")));
                item.setItemMeta(itemMeta);
                inv.setItem(Index[i],item);
            } else if (check < i){
                ItemStack item = new ItemStack(Material.CHEST_MINECART, 1);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a[" + (i+1) + "일차] &f보상"));
                itemMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&a- &f아직 보상을 받을 수 없습니다.")));
                item.setItemMeta(itemMeta);
                inv.setItem(Index[i],item);
            } else if (check > i){
                ItemStack item = new ItemStack(Material.HOPPER_MINECART, 1);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a[" + (i+1) + "일차] &f보상"));
                itemMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&a- &f이미 받은 보상입니다.")));
                item.setItemMeta(itemMeta);
                inv.setItem(Index[i],item);
            }
            i++;
        }

    }
    public AttendanceGUI(Player p) {
        inv = Bukkit.createInventory(null,54,"출석");
        this.initItemSetting(p);
    }
    public void open(Player player){
        player.openInventory(inv);
    }
}
