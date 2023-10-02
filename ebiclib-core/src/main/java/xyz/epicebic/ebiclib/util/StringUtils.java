package xyz.epicebic.ebiclib.util;

public class StringUtils {

    private static final int A_CODEPOINT = 'A';
    private static final char[] SMALL_CAPS = "ᴀʙᴄᴅᴇғɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ".toCharArray();

    public static String toSmallCaps(String input) {
        StringBuilder builder = new StringBuilder();

        for (char character : input.toCharArray()) {
            int codepoint = Character.toUpperCase((int) character) - A_CODEPOINT;
            if (codepoint >= 0 && codepoint <= SMALL_CAPS.length) {
                builder.append(SMALL_CAPS[codepoint]);
                continue;
            }
            builder.append(character);
        }
        return builder.toString();
    }
}
