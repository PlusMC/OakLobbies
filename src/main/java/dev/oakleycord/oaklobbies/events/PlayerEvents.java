package dev.oakleycord.oaklobbies.events;

import dev.oakleycord.oaklobbies.OakLobbies;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class PlayerEvents implements Listener {

    private final OakLobbies plugin;

    public PlayerEvents(OakLobbies plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getDatabaseHandler().asyncUserAction(player.getUniqueId(), user -> plugin.getTimeZones().put(player, user.getTimeZone()));
    }


}
