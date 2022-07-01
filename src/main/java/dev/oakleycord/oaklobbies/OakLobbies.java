package dev.oakleycord.oaklobbies;

import dev.oakleycord.oaklobbies.events.PlayerEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.plusmc.pluslib.bukkit.handlers.MultiWorldHandler;
import org.plusmc.pluslib.bukkit.managing.BaseManager;
import org.plusmc.pluslib.bukkit.managing.PlusCommandManager;
import org.plusmc.pluslib.bukkit.managing.TickingManager;
import org.plusmc.pluslib.mongo.DatabaseHandler;
import org.plusmc.pluslib.reflect.bungeespigot.config.ConfigSpigot;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public final class OakLobbies extends JavaPlugin {
    private Map<Player, TimeZone> timeZones;
    private MultiWorldHandler multiWorldHandler;
    private DatabaseHandler databaseHandler;
    private LobbyConfig lobbyConfig;
    public Map<Player, TimeZone> getTimeZones() {
        return timeZones;
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public LobbyConfig getLobbyConfig() {
        return lobbyConfig;
    }



    @Override
    public void onDisable() {
        multiWorldHandler.unregisterAllEvents();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        ConfigSpigot config = new ConfigSpigot(new File(getDataFolder(), "config.yml"));
        lobbyConfig = new LobbyConfig(config);

        this.databaseHandler = DatabaseHandler.getInstance();

        timeZones = new HashMap<>();

        TechnobladeTribute tribute = new TechnobladeTribute();
        BaseManager.createManager(PlusCommandManager.class, this);
        BaseManager.registerAny(tribute, this);

        this.multiWorldHandler = new MultiWorldHandler(this, Bukkit.getWorlds().get(0));
        multiWorldHandler.registerEvents(new PlayerEvents(this));
        multiWorldHandler.registerEvents(tribute);


        if(lobbyConfig.keepSpawnInMemory) {
            multiWorldHandler.getWorldOverworld().setKeepSpawnInMemory(false); //memory optimization since players don't hang out much in the lobby
        }

        if(lobbyConfig.getRealTimeConfig().enabled) {
            BaseManager.createManager(TickingManager.class, this);
            BaseManager.registerAny(new RealtimeHandler(this), this);
        }
    }

    public static OakLobbies getInstance() {
        return JavaPlugin.getPlugin(OakLobbies.class);
    }
}
