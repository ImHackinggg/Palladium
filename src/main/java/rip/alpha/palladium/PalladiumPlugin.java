package rip.alpha.palladium;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import rip.alpha.palladium.crash.CrashHandler;
import rip.alpha.palladium.log4j.Log4jHandler;
import rip.alpha.palladium.profile.PalladiumProfileHandler;

@Getter
public class PalladiumPlugin extends JavaPlugin {

    @Getter
    private static PalladiumPlugin instance;

    private PalladiumProfileHandler profileHandler;
    private Log4jHandler log4jHandler;
    private CrashHandler crashHandler;

    @Override
    public void onEnable() {
        instance = this;
        this.profileHandler = new PalladiumProfileHandler(this);
        this.log4jHandler = new Log4jHandler(this);
        this.crashHandler = new CrashHandler(this);
    }
}
