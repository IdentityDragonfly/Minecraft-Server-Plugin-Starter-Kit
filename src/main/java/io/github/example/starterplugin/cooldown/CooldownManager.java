package io.github.example.starterplugin.cooldown;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CooldownManager {

    private final Map<UUID, Instant> cooldowns = new ConcurrentHashMap<>();

    public boolean isOnCooldown(UUID playerId, long cooldownSeconds) {
        Instant lastUsed = cooldowns.get(playerId);
        if (lastUsed == null) {
            return false;
        }
        return Duration.between(lastUsed, Instant.now()).getSeconds() < cooldownSeconds;
    }

    public long remainingSeconds(UUID playerId, long cooldownSeconds) {
        Instant lastUsed = cooldowns.get(playerId);
        if (lastUsed == null) {
            return 0;
        }

        long elapsed = Duration.between(lastUsed, Instant.now()).getSeconds();
        return Math.max(0, cooldownSeconds - elapsed);
    }

    public void setCooldown(UUID playerId) {
        cooldowns.put(playerId, Instant.now());
    }
}
