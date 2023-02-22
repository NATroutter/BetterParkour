package fi.natroutter.betterparkour.handlers;

import fi.natroutter.natlibs.handlers.hooking.Hook;
import fi.natroutter.natlibs.handlers.hooking.HookSettings;
import org.bukkit.plugin.java.JavaPlugin;

public class Hooks {

    private Hook holographicDisplays;

    public Hook getHolographicDisplays() {return holographicDisplays;}

    public Hooks(JavaPlugin pl) {
        HookSettings set = new HookSettings();
        set.setHookedMessage("  §9+ §b{plugin} §7Hooked succesfully!");
        set.setHookingFailedMessage("  §9- §b{plugin} §7Failed to hook!");
        set.setDisableMessage("§7Disabling plugin because plugin hooking failed");
        set.disableWhenFailed();

        holographicDisplays = new Hook(pl, set, "HolographicDisplays", true);

    }

}
