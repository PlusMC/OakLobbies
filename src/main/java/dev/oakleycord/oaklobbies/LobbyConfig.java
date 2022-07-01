package dev.oakleycord.oaklobbies;

import org.plusmc.pluslib.reflect.bungeespigot.config.ConfigEntry;
import org.plusmc.pluslib.reflect.bungeespigot.config.IConfig;

public class LobbyConfig {
    private WelcomeMessage welcomeConfig;
    private Realtime realtimeConfig;
    @ConfigEntry boolean keepSpawnInMemory;

    public LobbyConfig(IConfig config) {
        this.welcomeConfig = new WelcomeMessage();
        this.realtimeConfig = new Realtime();
        config.writeIntoObject(this);
        config.section("WelcomeMessage").writeIntoObject(welcomeConfig);
        config.section("Realtime").writeIntoObject(realtimeConfig);
    }

    public WelcomeMessage getWelcomeConfig() {
        return welcomeConfig;
    }

    public Realtime getRealTimeConfig() {
        return realtimeConfig;
    }


    public static class WelcomeMessage {
        @ConfigEntry public boolean enabled;

        @ConfigEntry public String title;
        @ConfigEntry public int fadeIn;
        @ConfigEntry public int stay;
        @ConfigEntry public int fadeOut;
        @ConfigEntry public String subtitle;
        @ConfigEntry public String message;
    }

    public static class Realtime {
        @ConfigEntry public boolean enabled;
    }
}
