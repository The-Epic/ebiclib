package xyz.epicebic.ebiclib.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public abstract class SimpleCommandHandler implements CommandExecutor, TabCompleter {
    private final String permission;

    protected SimpleCommandHandler(String permission) {
        this.permission = permission;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }

    public String getPermission() {
        return permission;
    }

}