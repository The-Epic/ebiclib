package xyz.epicebic.ebiclib.util;

import org.bukkit.Chunk;

public class MathsUtils {
    public static boolean isWithinBounds(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public static long getChunkKey(Chunk chunk) {
        return ((long) chunk.getZ() << 32) | (chunk.getX() & 0xFFFFFFFFL);
    }

    public static long clamp(long value, long min, long max) {
        return Math.min(Math.max(value, min), max);
    }

    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public static short clamp(short value, short min, short max) {
        return (short) Math.min(Math.max(value, min), max);
    }

    public static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    public static float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }
}
