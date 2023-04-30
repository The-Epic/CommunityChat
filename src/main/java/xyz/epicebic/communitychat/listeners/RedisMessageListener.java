package xyz.epicebic.communitychat.listeners;

import lombok.RequiredArgsConstructor;
import me.epic.spigotlib.utils.ProfileUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.JedisPubSub;
import xyz.epicebic.communitychat.CommunityChat;

import javax.swing.plaf.SplitPaneUI;
import java.util.UUID;

@RequiredArgsConstructor
public class RedisMessageListener extends JedisPubSub {
    private final CommunityChat plugin;

    @Override
    public void onMessage(String channel, String message) {
        String delimiter = ":::";
//        int lastIndex = message.lastIndexOf(delimiter);
//
//        String result;
//        if (lastIndex != -1) {
//            result = message.substring(lastIndex + delimiter.length());
//        } else {
//            return;
//        }

        String[] parts = message.split(delimiter);
        if (parts.length != 3) return;
        UUID uuid;
        if (ProfileUtils.isValidUUID(parts[1])) {
            uuid = ProfileUtils.getUUIDFromString(parts[1]);
        } else {
            return;
        }
        String playerMessage = parts[0];
        String server = parts[2];

        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            Bukkit.broadcastMessage(plugin.getConfig().getString("chat.global", "[<server>] <message>").replace("<server>", server).replace("<message>", playerMessage));
        }
    }
}
