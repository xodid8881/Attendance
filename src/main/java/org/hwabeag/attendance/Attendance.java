package org.hwabeag.attendance;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.hwabeag.attendance.commands.AttendanceCommand;
import org.hwabeag.attendance.config.ConfigManager;
import org.hwabeag.attendance.events.InvClickEvent;
import org.hwabeag.attendance.events.InvCloseEvent;
import org.hwabeag.attendance.events.JoinEvent;
import org.hwabeag.attendance.expansions.AttendanceExpansion;

import java.util.Objects;

public final class Attendance extends JavaPlugin {

    private static ConfigManager configManager;

    private FileConfiguration config;

    public static Attendance getPlugin() {
        return JavaPlugin.getPlugin(Attendance.class);
    }

    public static void getConfigManager() {
        if (configManager == null)
            configManager = new ConfigManager();
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new InvClickEvent(), this);
        getServer().getPluginManager().registerEvents(new InvCloseEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
    }

    private void registerCommands() {
        Objects.requireNonNull(getServer().getPluginCommand("출석")).setExecutor(new AttendanceCommand());
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("[Attendance] Enable");
        getConfigManager();
        registerCommands();
        registerEvents();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new AttendanceExpansion(this).register();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("[Attendance] Disable");
        ConfigManager.saveConfigs();
    }
}
