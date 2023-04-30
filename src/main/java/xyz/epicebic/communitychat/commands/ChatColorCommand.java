package xyz.epicebic.communitychat.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.epic.spigotlib.commands.SimpleCommandHandler;
import me.epic.spigotlib.formatting.Formatting;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.epicebic.communitychat.CommunityChat;
import xyz.epicebic.communitychat.Utils;

public class ChatColorCommand extends SimpleCommandHandler {
    private final CommunityChat plugin;


    public ChatColorCommand(CommunityChat plugin) {
        super("communitychat.command.chatcolor", null);
        this.plugin = plugin;
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This is a player only command.");
            return;
        }

        if (args.length < 1) {
            player.sendMessage("Please specify a color!");
            return;
        }

        for (int i = 0; i < args.length; i++) {
            if (!Utils.isColorValid(args[i])) {
                player.sendMessage(Formatting.translate("<red>The " + (i + 1) + Utils.getNumberSuffix(i + 1) + " color you provided was not valid, please fix this."));
                return;
            }
            args[i] = Utils.getValidColor(args[i]);
        }

        plugin.getPlayerDataAPI().getProvider().setChatColors(player.getUniqueId(), args);
        plugin.getPlayerColorCache().put(player.getUniqueId(), String.join(":", args));
        player.sendMessage(PlaceholderAPI.setPlaceholders(player, Utils.formatMessage(plugin, player.getName(), "This is a test message", player, args)));
    }}
