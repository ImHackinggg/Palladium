package rip.alpha.palladium.crash;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.alpha.libraries.command.annotation.Command;
import rip.alpha.libraries.command.annotation.CommandUsage;
import rip.alpha.libraries.command.annotation.Default;
import rip.alpha.libraries.util.message.MessageColor;
import rip.alpha.palladium.PalladiumPlugin;
import rip.alpha.palladium.profile.PalladiumProfile;

public class CrashCommand {

    @CommandUsage("(target)")
    @Command(names = {"crash info"}, permission = "op")
    public static void crashPacketsCommand(CommandSender sender, @Default("self") Player target) {
        PalladiumProfile profile = PalladiumPlugin.getInstance().getProfileHandler().getProfile(target.getUniqueId());
        int packetPerSecond = profile.getPackets();
        int leftClicksCPS = profile.getLeftClicks();
        int rightClicksCPS = profile.getRightClicks();
        sender.sendMessage(MessageColor.GOLD + "Packets per second: " + packetPerSecond);
        sender.sendMessage(MessageColor.GOLD + "LCPS: " + leftClicksCPS);
        sender.sendMessage(MessageColor.GOLD + "RCPS: " + rightClicksCPS);
    }

    @CommandUsage("<amount>")
    @Command(names = "crash setpacketlimit", permission = "op")
    public static void crashSetLimitCommand(CommandSender sender, int amount) {
        PalladiumPlugin.getInstance().getCrashHandler().setPacketLimit(amount);
        sender.sendMessage(MessageColor.GREEN + "Updated packet limit to " + amount);
    }

    @CommandUsage("<amount>")
    @Command(names = "crash setcpsLimit", permission = "op")
    public static void crashSetCPSLimit(CommandSender sender, int amount) {
        PalladiumPlugin.getInstance().getCrashHandler().setCpsLimit(amount);
        sender.sendMessage(MessageColor.GREEN + "Updated cps limit to " + amount);
    }
}
