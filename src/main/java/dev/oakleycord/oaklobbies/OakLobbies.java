package dev.oakleycord.oaklobbies;

import dev.oakleycord.oaklobbies.events.PlayerEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.plusmc.pluslib.bukkit.handlers.MultiWorldHandler;
import org.plusmc.pluslib.bukkit.managing.BaseManager;
import org.plusmc.pluslib.bukkit.managing.TickingManager;
import org.plusmc.pluslib.mongo.DatabaseHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public final class OakLobbies extends JavaPlugin {
    private Map<Player, TimeZone> timeZones;
    private MultiWorldHandler multiWorldHandler;

    private DatabaseHandler databaseHandler;

    public Map<Player, TimeZone> getTimeZones() {
        return timeZones;
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    @Override
    public void onDisable() {
        multiWorldHandler.unregisterAllEvents();
    }

    @Override
    public void onEnable() {
        DatabaseHandler.createInstance();
        this.databaseHandler = DatabaseHandler.getInstance();

        timeZones = new HashMap<>();
        BaseManager.createManager(TickingManager.class, this);

        multiWorldHandler = new MultiWorldHandler(this, Bukkit.getWorlds().get(0));
        multiWorldHandler.registerEvents(new PlayerEvents(this));
        multiWorldHandler.getWorldOverworld().setKeepSpawnInMemory(false); //memory optimization since players don't hang out much in the lobby
        BaseManager.registerAny(new RealtimeHandler(this), this);
    }
}
