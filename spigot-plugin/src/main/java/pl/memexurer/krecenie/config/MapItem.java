package pl.memexurer.krecenie.config;

import eu.okaeri.configs.OkaeriConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

public final class MapItem extends OkaeriConfig {
    private final double percentage;
    private final String command;

    public MapItem(double percentage, String command) {
        this.percentage = percentage;
        this.command = command;
    }

    public void execute(Player player) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("{PLAYER}", player.getName()));
    }

    public double percentage() {
        return percentage;
    }

    public String command() {
        return command;
    }

    @Override
    public String toString() {
        return "MapItem[" +
                "percentage=" + percentage + ", " +
                "command=" + command + ']';
    }

}
