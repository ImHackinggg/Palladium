package rip.alpha.palladium.crash;

import lombok.Getter;
import lombok.Setter;
import rip.alpha.libraries.LibrariesPlugin;
import rip.alpha.palladium.PalladiumPlugin;
import rip.foxtrot.spigot.fSpigot;

@Getter
@Setter
public class CrashHandler {

    private int packetLimit = 400;
    private int cpsLimit = 12;

    public CrashHandler(PalladiumPlugin plugin) {
        fSpigot.INSTANCE.addPacketHandler(new CrashPacketHandler(this));
        LibrariesPlugin.getCommandFramework().registerClass(CrashCommand.class);
        plugin.getServer().getPluginManager().registerEvents(new CrashListener(plugin), plugin);
    }
}
