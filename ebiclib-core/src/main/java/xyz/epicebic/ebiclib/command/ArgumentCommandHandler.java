package xyz.epicebic.ebiclib.command;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

public class ArgumentCommandHandler extends SimpleCommandHandler {
    private final Map<String, SimpleCommandHandler> subcommands = new HashMap<>();
    private final Supplier<String> permMessage;
    private final Supplier<String> usageMessage;

    private CommandExecutor defaultExecutor;

    public ArgumentCommandHandler(String permission, Supplier<String> permMessage, Supplier<String> usageMessage) {
        super(permission);
        this.permMessage = permMessage;
        this.usageMessage = usageMessage;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage(permMessage.get());
            return true;
        }

        if (args.length == 0) {
            if (this.defaultExecutor != null) {
                return this.defaultExecutor.onCommand(sender, command, alias, args);
            } else {
                sendUsage(sender, "none", this.subcommands.keySet());
                return true;
            }
        } else if (args.length > 0) {
            SimpleCommandHandler executor = this.subcommands.get(args[0]);

            if (executor == null) {
                sendUsage(sender, args[0], this.subcommands.keySet());
                return true;
            }

            return executor.onCommand(sender, command, alias, Arrays.copyOfRange(args, 1, args.length));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> validSubcommands = new ArrayList<>();

            for (Entry<String, SimpleCommandHandler> entry : this.subcommands.entrySet()) {
                if (sender.hasPermission(entry.getValue().getPermission()))
                    validSubcommands.add(entry.getKey());
            }

            return StringUtil.copyPartialMatches(args[0], validSubcommands, new ArrayList<>());
        } else if (args.length > 1) {
            SimpleCommandHandler executor = this.subcommands.get(args[0]);
            if (executor != null)
                return executor.onTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length));
        }
        return Collections.emptyList();
    }

    public void sendUsage(CommandSender sender, String arg, Collection<String> allowedargs) {
        sender.sendMessage(MessageFormat.format(usageMessage.get(), arg, String.join(", ", allowedargs)));
    }

    protected void addArgumentExecutor(String arg, SimpleCommandHandler exeuctor) {
        this.subcommands.put(arg, exeuctor);
    }

    protected void setDefault(SimpleCommandHandler exeuctor) {
        this.defaultExecutor = exeuctor;
    }

    public Supplier<String> getPermMessage() {
        return permMessage;
    }

    public Supplier<String> getUsageMessage() {
        return usageMessage;
    }
}