package dev.oakleycord.oaklobbies.events;

import dev.oakleycord.oaklobbies.LobbyConfig;
import dev.oakleycord.oaklobbies.OakLobbies;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.plusmc.pluslib.bukkit.handlers.VariableHandler;

import java.util.Calendar;
import java.util.Date;


public class PlayerEvents implements Listener {
    private final OakLobbies plugin;
    private final LobbyConfig lobbyConfig;

    public PlayerEvents(OakLobbies plugin) {
        this.plugin = plugin;
        this.lobbyConfig = plugin.getLobbyConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation().add(0.5, 0, 0.5));


        plugin.getDatabaseHandler().asyncUserAction(player.getUniqueId(), user -> {
            if (lobbyConfig.getRealTimeConfig().enabled)
                plugin.getTimeZones().put(player, user.getTimeZone());

            if (!lobbyConfig.getWelcomeConfig().enabled)
                return;
            LobbyConfig.WelcomeMessage welcomeConfig = lobbyConfig.getWelcomeConfig();

            updateVariables();
            String[] messages = ChatColor.translateAlternateColorCodes('&', VariableHandler.format(welcomeConfig.message, player, true)).split("\\n");
            String title = ChatColor.translateAlternateColorCodes('&', VariableHandler.format(welcomeConfig.title, player, true));
            String subtitle = ChatColor.translateAlternateColorCodes('&', VariableHandler.format(welcomeConfig.subtitle, player, true));
            player.sendTitle(title, subtitle, welcomeConfig.fadeIn, welcomeConfig.stay, welcomeConfig.fadeOut);
            player.sendMessage(messages);
        });
    }

    public void updateVariables() {
        plugin.getTimeZones().forEach((player, timeZone) -> {
            VariableHandler.setVariable(player, "playerTimezone", timeZone.getDisplayName());
            //get current time in the timezone
            Calendar calendar = Calendar.getInstance(timeZone);
            Date date = calendar.getTime();
            //format date to #:## AM/PM
            String time = String.format("%tR", date);
            VariableHandler.setVariable(player, "playerTime", time);
        });
    }
}
