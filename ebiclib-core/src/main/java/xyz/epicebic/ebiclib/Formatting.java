package xyz.epicebic.ebiclib;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;

import java.awt.Color;

public class Formatting {

    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder().character('&')
            .hexCharacter('#').hexColors().useUnusualXRepeatedCharacterHexFormat().build();


    /**
     * Translates the given string by converting color codes and serializing it using MiniMessage.
     *
     * @param toTranslate the string to be translated
     * @return the translated string with color codes and serialized MiniMessage
     */
    public static String translate(String toTranslate) {
        return (ChatColor.translateAlternateColorCodes('&', SERIALIZER.serialize(MiniMessage.miniMessage().deserialize(toTranslate))));
    }

    /**
     * Translates a given string into a {@link Component}.
     *
     * @param toTranslate the string to be translated
     * @return the translated {@link Component}.
     */
    public static Component translateComponent(String toTranslate) {
        return MiniMessage.miniMessage().deserialize(ChatColor.translateAlternateColorCodes('&', toTranslate));
    }

    /**
     * Returns a ChatColor object based on the provided hex color code.
     *
     * @param colour the hex color code as a string
     * @return the corresponding ChatColor object
     */
    public static ChatColor getColourFromHex(String colour) {
        return ChatColor.of(new Color(Integer.parseInt(colour, 16)));
    }

    /**
     * Converts a miniMessage string to a legacy format.
     *
     * @param input the miniMessage string to convert
     * @return the converted legacy string
     */
    public static String miniMessageToLegacy(String input) {
        return SERIALIZER.serialize(MiniMessage.miniMessage().deserialize(input));
    }
}
