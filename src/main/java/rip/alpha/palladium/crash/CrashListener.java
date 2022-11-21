package rip.alpha.palladium.crash;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.CraftChunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import rip.alpha.palladium.PalladiumPlugin;

import java.util.concurrent.atomic.AtomicInteger;

public class CrashListener implements Listener {

    private final Long2ObjectMap<AtomicInteger> chunks = new Long2ObjectOpenHashMap<>();

    public CrashListener(PalladiumPlugin plugin) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::clearMap, 20L, 20L);
    }

    private void clearMap() {
        chunks.values().forEach(atomic -> atomic.set(0));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockRedstone(BlockRedstoneEvent event) {
        Block block = event.getBlock();

        if (block == null){
            return;
        }

        if (block.getType() != Material.REDSTONE){
            return;
        }

        Chunk chunk = event.getBlock().getChunk();
        long chunkKey = ((CraftChunk) chunk).getHandle().chunkKey;
        AtomicInteger counter = chunks.computeIfAbsent(chunkKey, key -> new AtomicInteger());
        int ticks = counter.incrementAndGet();

        if (ticks >= 400) {
            String message = "Someone at " + event.getBlock().getLocation() + " is causing lag";
            PalladiumPlugin.getInstance().getLogger().info(message);
            for (Player player : Bukkit.getOnlinePlayers()){
                if (player.isOp()){
                    player.sendMessage(message);
                }
            }
            event.setNewCurrent(0);
        }
    }
}
