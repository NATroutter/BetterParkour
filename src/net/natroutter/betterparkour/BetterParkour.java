package net.natroutter.betterparkour;

import net.natroutter.betterparkour.commands.BetterParkourCMD;
import net.natroutter.betterparkour.listeners.ParkourListener;
import net.natroutter.betterparkour.listeners.WandUseListener;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterParkour extends JavaPlugin {

    private Handler handler;
    private static ParkourAPI api;

    public static ParkourAPI getAPI() {
        return api;
    }

    @Override
    public void onEnable() {

        handler = new Handler(this);

        PluginManager pm = getServer().getPluginManager();
        CommandMap map = getServer().getCommandMap();

        pm.registerEvents(new WandUseListener(handler), this);
        pm.registerEvents(new ParkourListener(handler), this);

        map.register("betterparkour", new BetterParkourCMD(handler));

        api = new ParkourAPI(handler);
    }

    @Override
    public void onDisable() {
        handler.getStatisticHandler().save(true);
    }
}
