package xyz.epicebic.communitychat.listeners;

import com.jeff_media.playerdataapi.DataProvider;
import me.epic.spigotlib.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.epicebic.communitychat.CommunityChat;

import java.util.UUID;

public class PlayerEventListener implements Listener {

    private final CommunityChat plugin;
    private final DataProvider dataProvider;

    public PlayerEventListener(CommunityChat plugin) {
        this.plugin = plugin;
        this.dataProvider = plugin.getPlayerDataAPI().getProvider();
    }

    @EventHandler
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
        final UUID playerUUID = event.getUniqueId();
        dataProvider.getChatColors(playerUUID).whenComplete((colors, ex) -> {
            String fixedColors = colors == null ? "white" : colors;
            plugin.getPlayerColorCache().put(playerUUID, fixedColors);
        });
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        OfflinePlayer offlinePlayer = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!offlinePlayer.isOnline()) {
                plugin.getPlayerColorCache().remove(offlinePlayer.getUniqueId());
            }
        }, 20 * 60 * 30);
    }
}
