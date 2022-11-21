package rip.alpha.palladium.crash;

import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.alpha.libraries.util.task.TaskUtil;
import rip.alpha.palladium.PalladiumPlugin;
import rip.alpha.palladium.profile.PalladiumProfile;
import rip.foxtrot.spigot.handler.PacketHandler;

import java.util.Arrays;

@RequiredArgsConstructor
public class CrashPacketHandler implements PacketHandler {

    private final CrashHandler handler;

    @Override
    public boolean handleReceivedPacketCancellable(PlayerConnection connection, Packet packet) {
        Player player = connection.getPlayer();

        PalladiumProfile profile = PalladiumPlugin.getInstance().getProfileHandler().getProfile(player.getUniqueId());

        if (profile != null) {
            if (!(packet instanceof PacketPlayInSettings)) {  //ignore settings as its sent way to quickly
                int packetsPerSecond = profile.incrementPackets();
                if (packetsPerSecond > this.handler.getPacketLimit()) {
                    int i = profile.incrementVl();

                    if (i >= 5) {
                        this.kickPlayer(player, "Sending too many packets");
                        return false;
                    }

                    this.flag(ChatColor.RED + player.getName() + " has just sent " + packetsPerSecond + " in a second! (VL:" + i + ")");
                }
            }

            if (packet instanceof PacketPlayInUseEntity packetPlayInUseEntity) {
                if (player.getGameMode() != GameMode.CREATIVE) {
                    if (packetPlayInUseEntity.a == player.getEntityId()) {
                        this.kickPlayer(player, "Crash attempt [PacketPlayInUseEntity]");
                        return false;
                    }
                }

                if (packetPlayInUseEntity.c() == EnumEntityUseAction.ATTACK) {
                    Entity entity = packetPlayInUseEntity.a(connection.player.world);
                    if (entity != null && entity.getBukkitEntity() instanceof Player) {
                        long cps = profile.getLeftClicks();
                        if (cps > this.handler.getCpsLimit()) {
                            return false;
                        }
                        profile.incrementLeftClick();
                    }
                }
            }

            if (packet instanceof PacketPlayInClientCommand packetPlayInClientCommand) {
                if (packetPlayInClientCommand.c() == EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
                    if (profile.isBackpacking()) {
                        this.updateCraftingSlots(connection, player, profile);
                    }
                    profile.setBackpacking(true);
                }
            }

            if (packet instanceof PacketPlayInArmAnimation) {
                if (profile.isBackpacking()) {
                    this.updateCraftingSlots(connection, player, profile);
                }
            }

            if (packet instanceof PacketPlayInBlockPlace) {
                long rightClickCPS = profile.getRightClicks();
                if (rightClickCPS > 12) {
                    return false;
                }
                profile.incrementRightClick();
            }

            if (packet instanceof PacketPlayInPositionLook position) {
                float pitch = position.h();
                if (Math.abs(pitch) > 90.0f) {
                    this.kickPlayer(player, "Crash attempt [PacketPlayInPositionLook]");
                    return false;
                }
            }

            if (packet instanceof PacketPlayInAbilities packetPlayInAbilities) {
                if (packetPlayInAbilities.isFlying() && !player.getAllowFlight()) {
                    this.kickPlayer(player, "Crash attempt [PacketPlayInAbilities]");
                    return false;
                }
            }
            if(packet instanceof PacketPlayInCustomPayload customPayload) {
                String channel = customPayload.c();
                if("MC|BSign".equals(channel) || "MC|BEdit".equals(channel) || "MC|BOpen".equalsIgnoreCase(channel)) {
                    if(player.getItemInHand() == null || !player.getItemInHand().getType().equals(Material.BOOK_AND_QUILL)) {
                        kickPlayer(player, "Book Crash");
                        return false;
                    }
                }
                if(customPayload.length > 15000) {
                    kickPlayer(player, "Too Big of a Book");
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean handlesCancellable() {
        return true;
    }

    private void kickPlayer(Player player, String message) {
        TaskUtil.runSync(() -> player.kickPlayer(ChatColor.RED + message));
        this.flag(ChatColor.RED + player.getName() + " has just been kicked for " + message);
    }

    private void flag(String message) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.isOp()) {
                target.sendMessage(message);
            }
        }
    }

    private void updateCraftingSlots(PlayerConnection connection, Player player, PalladiumProfile profile) {
        ContainerPlayer containerPlayer = (ContainerPlayer) connection.getPlayer().getHandle().defaultContainer;
        InventoryCrafting inventoryCrafting = containerPlayer.craftInventory;
        ItemStack[] content = inventoryCrafting.getContents();
        Arrays.fill(content, null);
        inventoryCrafting.resultInventory.setItem(0, null);
        profile.setBackpacking(false);
    }
}
