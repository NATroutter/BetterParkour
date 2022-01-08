package net.natroutter.betterparkour.handlers;

import net.natroutter.natlibs.handlers.hooking.Hook;
import net.natroutter.natlibs.handlers.hooking.HookSettings;
import org.bukkit.plugin.java.JavaPlugin;

public class Hooks {

    private Hook holographicDisplays;

    public Hook getHolographicDisplays() {return holographicDisplays;}

    public Hooks(JavaPlugin pl) {
        HookSettings set = new HookSettings();
        set.setHookedMessage("  §9+ §b{plugin} Hooked succesfully!");
        set.setHookingFailedMessage("  §9- b7{plugin} Failed to hook!");
        set.setDisableMessage("§7Disabling plugin because plugin hooking failed");
        set.disableWhenFailed();

        holographicDisplays = new Hook(pl, set, "HolographicDisplays", true);

    }

}
