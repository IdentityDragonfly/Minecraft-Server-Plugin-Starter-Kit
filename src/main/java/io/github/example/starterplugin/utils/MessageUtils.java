package io.github.example.starterplugin.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.concurrent.TimeUnit;

public final class MessageUtils {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();

    private MessageUtils() {
    }

    public static Component color(String message) {
        if (message == null || message.isBlank()) {
            return Component.empty();
        }
        return LEGACY.deserialize(message);
    }

    public static String formatDuration(long totalSeconds) {
        long minutes = TimeUnit.SECONDS.toMinutes(totalSeconds);
        long seconds = totalSeconds - TimeUnit.MINUTES.toSeconds(minutes);

        if (minutes <= 0) {
            return seconds + "s";
        }
        return minutes + "m " + seconds + "s";
    }
}
