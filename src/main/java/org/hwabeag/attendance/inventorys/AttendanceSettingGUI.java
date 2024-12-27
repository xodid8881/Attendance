package org.hwabeag.attendance.inventorys;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class AttendanceSettingGUI implements Listener {
    private final Inventory inv;

    private void initItemSetting() {
        int i = 0;
        int[ ] Index = { 10,11,12,13,14,15,16, 19,20,21,22,23,24,25, 28,29,30,31,32,33,34, 37,38,39,40,41,42,43};
        while (i <= 27) {
            ItemStack item = new ItemStack(Material.CHEST_MINECART, 1);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a[" + (i+1) + "일차] &f보상"));
            itemMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&a- &f클릭 시 보상을 설정합니다.")));
            item.setItemMeta(itemMeta);
            inv.setItem(Index[i],item);
            i++;
        }

    }
    public AttendanceSettingGUI(Player p) {
        inv = Bukkit.createInventory(null,54,"출석보상설정");
        this.initItemSetting();
    }
    public void open(Player player){
        player.openInventory(inv);
    }
}
