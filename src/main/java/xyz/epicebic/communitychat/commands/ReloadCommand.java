package xyz.epicebic.communitychat.commands;

import me.epic.spigotlib.Timings;
import me.epic.spigotlib.commands.SimpleCommandHandler;
import me.epic.spigotlib.formatting.Formatting;
import org.bukkit.command.CommandSender;
import xyz.epicebic.communitychat.CommunityChat;

public class ReloadCommand extends SimpleCommandHandler {

    private final CommunityChat plugin;

    public ReloadCommand(CommunityChat plugin) {
        super("communitychat.command.reload", null);
        this.plugin = plugin;
    }

    @Override
    public void handleCommand(CommandSender commandSender, String[] strings) {
        Timings.startTimings("reload");
        plugin.reload();
        commandSender.sendMessage(Formatting.translate("<green>Reloaded in " + Timings.endTimings("reload") + "ms"));
    }
}
