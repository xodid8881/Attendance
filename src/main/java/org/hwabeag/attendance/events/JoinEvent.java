package org.hwabeag.attendance.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.hwabeag.attendance.config.ConfigManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class JoinEvent implements Listener {
    FileConfiguration PlayerConfig = ConfigManager.getConfig("player");
    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        SimpleDateFormat Day = new SimpleDateFormat("yyyyMMdd");
        Day.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        Date now = new Date();
        String nowTime1 = Day.format(now);
        if (PlayerConfig.getString("출석." + name + ".Day") == null) {
            PlayerConfig.addDefault("출석." + name, "");
            PlayerConfig.set("출석." + name + ".Day", nowTime1);
            PlayerConfig.set("출석." + name + ".Check", 0);
            PlayerConfig.set("출석." + name + ".번호", 0);
            ConfigManager.saveConfigs();
        }
    }
}
