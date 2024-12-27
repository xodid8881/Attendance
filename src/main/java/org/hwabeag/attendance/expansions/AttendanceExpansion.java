package org.hwabeag.attendance.expansions;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.hwabeag.attendance.Attendance;
import org.hwabeag.attendance.config.ConfigManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AttendanceExpansion extends PlaceholderExpansion {


    FileConfiguration PlayerConfig = ConfigManager.getConfig("player");
    private final Attendance plugin; //

    public AttendanceExpansion(Attendance plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "cash_system";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("getIndex")) { // %cash_system_getIndex%
            String name = Objects.requireNonNull(player).getName();
            if (PlayerConfig.getString(name + ".캐시") != null) {
                return PlayerConfig.getString(name + ".캐시");
            }
            return "&l&a[&f서버원&l&a]";
        }
        return null; //
    }
}