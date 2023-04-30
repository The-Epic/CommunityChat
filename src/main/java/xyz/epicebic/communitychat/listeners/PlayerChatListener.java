package xyz.epicebic.communitychat.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.epicebic.communitychat.CommunityChat;
import xyz.epicebic.communitychat.Utils;

public class PlayerChatListener implements Listener {

    private final CommunityChat plugin;

    public PlayerChatListener(CommunityChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = Utils.formatMessage(plugin, player.getName(), event.getMessage(), player, plugin.getPlayerColorCache().getOrDefault(player.getUniqueId(), "white").replaceAll(",", ":")).replace("%", "%%");
        event.setFormat(message);


        plugin.getPlayerDataAPI().getProvider().redisPublish("global-chat", message + ":::" + player.getUniqueId() + ":::" + plugin.getServerName());
    }
}
