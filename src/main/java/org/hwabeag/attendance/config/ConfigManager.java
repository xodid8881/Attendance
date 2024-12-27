package org.hwabeag.attendance.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.hwabeag.attendance.Attendance;

import java.util.HashMap;

public class ConfigManager {
    private static final Attendance plugin = Attendance.getPlugin();

    private static final HashMap<String, ConfigMaker> configSet = new HashMap<>();

    public ConfigManager() {
        String path = plugin.getDataFolder().getAbsolutePath();
        configSet.put("attendance", new ConfigMaker(path, "Attendance.yml"));
        configSet.put("player", new ConfigMaker(path, "Player.yml"));
        loadSettings();
        saveConfigs();
    }

    public static void reloadConfigs() {
        for (String key : configSet.keySet()){
            plugin.getLogger().info(key);
            configSet.get(key).reloadConfig();
        }
    }

    public static void saveConfigs(){
        for (String key : configSet.keySet())
            configSet.get(key).saveConfig();
    }

    public static FileConfiguration getConfig(String fileName) {
        return configSet.get(fileName).getConfig();
    }

    public static void loadSettings(){
        FileConfiguration StyleSystemConfig = getConfig("attendance");
        StyleSystemConfig.options().copyDefaults(true);
        StyleSystemConfig.addDefault("attendance.prefix", "&a&l[Attendance]&7");
    }
}