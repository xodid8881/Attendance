package org.hwabeag.attendance.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.hwabeag.attendance.config.ConfigManager;
import org.hwabeag.attendance.inventorys.AttendanceGUI;
import org.hwabeag.attendance.inventorys.AttendanceSettingGUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.*;

public class AttendanceCommand implements TabCompleter, @Nullable CommandExecutor {

    FileConfiguration AttendanceConfig = ConfigManager.getConfig("attendance");
    FileConfiguration PlayerConfig = ConfigManager.getConfig("player");
    String Prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(AttendanceConfig.getString("attendance.prefix")));

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<String>();
            list.add("보상설정");
            list.add("초기화");
            list.add("전체초기화");
            return list;
        }
        if (args.length == 2) {
            List<String> list = new ArrayList<String>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                list.add(p.getName());
            }
            return list;
        }
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(Prefix + " 인게임에서 사용할 수 있습니다.");
            return true;
        }
        String name = player.getName();
        if (!player.isOp()) {
            AttendanceGUI inv = new AttendanceGUI(player);
            inv.open(player);
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(Prefix + " /출석 보상설정 - 출석보상을 설정합니다.");
            player.sendMessage(Prefix + " /출석 초기화 [닉네임] - 유저의 출석을 초기화합니다.");
            player.sendMessage(Prefix + " /출석 전체초기화 - 전체 유저들의 출석을 초기화합니다.");
            AttendanceGUI inv = new AttendanceGUI(player);
            inv.open(player);
            return true;
        }
        if (args[0].equalsIgnoreCase("초기화")) {
            if (args.length == 1) {
                player.sendMessage(Prefix + " 닉네임을 적어주세요.");
                return true;
            }
            if (PlayerConfig.getString("출석." + args[1]) != null) {
                SimpleDateFormat Day = new SimpleDateFormat("yyyyMMdd");
                Day.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                Date now = new Date();
                String nowTime1 = Day.format(now);
                PlayerConfig.set("출석." + args[1] + ".Day", nowTime1);
                PlayerConfig.set("출석." + args[1] + ".Check", 0);
                PlayerConfig.set("출석." + args[1] + ".번호", 0);
                ConfigManager.saveConfigs();
                player.sendMessage(Prefix + " " + args[1] + " 님의 출석 정보를 초기화했습니다.");
            } else {
                player.sendMessage(Prefix + " " + args[1] + " 닉네임의 유저는 존재하지 않습니다.");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("전체초기화")) {
            for (String key : Objects.requireNonNull(PlayerConfig.getConfigurationSection("출석")).getKeys(false)) {
                if (PlayerConfig.getString("출석." + key + ".Day") != null) {
                    SimpleDateFormat Day = new SimpleDateFormat("yyyyMMdd");
                    Day.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                    Date now = new Date();
                    String nowTime1 = Day.format(now);
                    PlayerConfig.set("출석." + key + ".Day", nowTime1);
                    PlayerConfig.set("출석." + key + ".Check", 0);
                    PlayerConfig.set("출석." + key + ".번호", 0);
                    ConfigManager.saveConfigs();
                }
            }
            player.sendMessage(Prefix + " 유저 전체의 출석정보를 초기화했습니다.");
            return true;
        }
        if (args[0].equalsIgnoreCase("보상설정")) {
            AttendanceSettingGUI inv = new AttendanceSettingGUI(player);
            inv.open(player);
            return true;
        }
        player.sendMessage(Prefix + " /출석 보상설정 - 출석보상을 설정합니다.");
        player.sendMessage(Prefix + " /출석 초기화 [닉네임] - 유저의 출석을 초기화합니다.");
        player.sendMessage(Prefix + " /출석 전체초기화 - 전체 유저들의 출석을 초기화합니다.");
        AttendanceGUI inv = new AttendanceGUI(player);
        inv.open(player);
        return true;
    }
}