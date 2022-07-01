package dev.oakleycord.oaklobbies;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.plusmc.pluslib.bukkit.managed.PlusCommand;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class TechnobladeTribute implements PlusCommand, Listener {
    List<Player> waitingMsg = new ArrayList<>();
    File tributeFile = new File(OakLobbies.getInstance().getDataFolder(), "tribute.txt");

    TechnobladeTribute() {
        if(!tributeFile.exists()) {
            try {
                tributeFile.createNewFile();
                OakLobbies.getInstance().getLogger().info("Created file");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public String getName() {
        return "technobladetribute";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public String getUsage() {
        return "/technobladetribute";
    }

    @Override
    public String getDescription() {
        return "Send a tribute to technoblade";
    }

    @Override
    public List<String> getCompletions(int index) {
        return List.of("");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("You must be a player to use this command!");
            return false;
        }


        player.sendMessage(ChatColor.YELLOW + "Write your message bellow:");
        waitingMsg.add(player);

        return true;
    }


    @EventHandler
    public void onMessageReceived(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(!waitingMsg.contains(player)) {
            return;
        }
        waitingMsg.remove(player);
        String message = "\n" + player.getName() + ": " + event.getMessage();
        try {
            Files.write(tributeFile.toPath(), message.getBytes(), StandardOpenOption.APPEND);
            event.getPlayer().sendMessage(ChatColor.GREEN + "Thank you for your message it will be emailed to technoblade's family.");
            Bukkit.getScheduler().runTask(OakLobbies.getInstance(), ()-> player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f));
        } catch (IOException e) {
            e.printStackTrace();
            event.getPlayer().sendMessage(ChatColor.RED + "Please try again later as an error has occured");
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        TextComponent click = new TextComponent("[Click Here]");
        click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/technobladetribute"));
        click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Send Message")));
        click.setColor(ChatColor.DARK_PURPLE);
        click.setBold(true);
        TextComponent component = new TextComponent("Please click below to send a message as a tribute to technoblade\n");
        component.setColor(ChatColor.GOLD);
        component.setBold(true);
        component.addExtra(click);
        event.getPlayer().spigot().sendMessage(component);
        event.setJoinMessage("");
    }
}
