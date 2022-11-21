package rip.alpha.palladium.profile;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import rip.alpha.palladium.PalladiumPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PalladiumProfileHandler {

    private final Map<UUID, PalladiumProfile> profileMap = new Object2ObjectOpenHashMap<>();

    public PalladiumProfileHandler(PalladiumPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new PalladiumProfileListener(this), plugin);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new PalladiumProfileTask(this), 1, 1, TimeUnit.SECONDS);
    }

    public PalladiumProfile getProfile(UUID uuid) {
        return this.profileMap.get(uuid);
    }

    public void addProfile(UUID uuid) {
        this.profileMap.put(uuid, new PalladiumProfile(uuid));
    }

    public void removeProfile(UUID uuid) {
        this.profileMap.remove(uuid);
    }

    public Set<PalladiumProfile> getProfiles() {
        return new HashSet<>(this.profileMap.values());
    }
}
