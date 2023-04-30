package xyz.epicebic.communitychat;

import com.jeff_media.playerdataapi.PlayerDataAPI;
import lombok.Getter;
import me.epic.spigotlib.EpicSpigotLib;
import me.epic.spigotlib.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.epicebic.communitychat.commands.ChatColorCommand;
import xyz.epicebic.communitychat.commands.ReloadCommand;
import xyz.epicebic.communitychat.listeners.PlayerChatListener;
import xyz.epicebic.communitychat.listeners.PlayerEventListener;
import xyz.epicebic.communitychat.listeners.RedisMessageListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public final class CommunityChat extends JavaPlugin {

    @Getter
    private PlayerDataAPI playerDataAPI;
    @Getter
    private CommunityChat instance;
    @Getter
    private String messageFormat;
    @Getter
    private final Map<UUID, String> playerColorCache = new HashMap<>();
    @Getter
    private String serverName;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        playerDataAPI = (PlayerDataAPI) Bukkit.getPluginManager().getPlugin("PlayerDataAPI");
        getServer().getPluginManager().registerEvents(new PlayerChatListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);
        saveDefaultConfig();
        reload();

        // Load commands
        getCommand("communitychat-reload").setExecutor(new ReloadCommand(this));
        getCommand("chatcolor").setExecutor(new ChatColorCommand(this));

        // Registering jedis pub sub channel
        playerDataAPI.getProvider().redisSubscribe(new RedisMessageListener(this), "global-chat");

        // Testing stuff
//        Bukkit.getOnlinePlayers().forEach(player -> {
//            playerDataAPI.getProvider().getChatColors(player.getUniqueId()).whenComplete((colors, ex) -> {
//                String fixedColors = colors == null ? "white" : colors;
//                playerColorCache.put(player.getUniqueId(), fixedColors);
//            });
//        });
    }

    public void reload() {
        this.messageFormat = getConfig().getString("chat.format", "%luckperms_prefix% <name> » <message>");
        this.serverName = getConfig().getString("server.name", "test");
        this.reloadConfig();
    }


}
