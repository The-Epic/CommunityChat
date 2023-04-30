package xyz.epicebic.communitychat;

import me.clip.placeholderapi.PlaceholderAPI;
import me.epic.spigotlib.formatting.Formatting;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.regex.Pattern;

public class Utils {

    private static final Pattern HEX_HASHTAG_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");
    private static final Pattern HEX_NUMBERS_PATTERN = Pattern.compile("0x[a-fA-F0-9]{6}");
    private static final Pattern CHATCOLOR_LETTER_PATTERN = Pattern.compile("(?i)[0-9A-FK-ORX]");

    public static String formatMessage(CommunityChat plugin, String playerName, String message, Player player, String... colors) {
        String colorString;
        if (colors.length == 1 && colors[0].contains(":")) {
            colorString = colors[0];
        } else {
            colorString = String.join(":", colors);
            if (colors.length == 1 && !colors[0].contains(":")) {
                colorString += ":" + getValidColor(colors[0]);
            }
        }

        String messageFormat = plugin.getMessageFormat();
        messageFormat = messageFormat.replace("<name>", "<gradient:" + colorString + ">" + playerName + "</gradient>");
        messageFormat = messageFormat.replace("<message>", message);
        messageFormat = messageFormat.replace(ChatColor.COLOR_CHAR, '&');

        return Formatting.translate(PlaceholderAPI.setPlaceholders(player, messageFormat));
    }

    public static boolean isColorValid(String color) {
        return HEX_HASHTAG_PATTERN.matcher(color).matches() || HEX_NUMBERS_PATTERN.matcher(color).matches() || CHATCOLOR_LETTER_PATTERN.matcher(color).matches();
    }

    /**
     * {@link Utils#isColorValid(String)} Should be called before this
     *
     * @param color Color to convert
     * @return hex color
     */
    public static String getValidColor(String color) {
        if (HEX_NUMBERS_PATTERN.matcher(color).matches()) {
            return "#" + color.substring(2).toUpperCase();
        } else if (CHATCOLOR_LETTER_PATTERN.matcher(color).matches()) {
            Color chatColor = ChatColor.getByChar(color.charAt(0)).getColor();
            return getValidColor(String.valueOf(String.format("#%02x%02x%02x", chatColor.getRed(), chatColor.getGreen(), chatColor.getBlue())));
        } else {
            return color;
        }
    }

    public static String getNumberSuffix(int number) {
        if (number >= 11 && number <= 13) {
            return "th";
        } else {
            int lastDigit = number % 10;
            return switch (lastDigit) {
                case 1 -> "st";
                case 2 -> "nd";
                case 3 -> "rd";
                default -> "th";
            };
        }
    }
}
