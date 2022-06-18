package dev.oakleycord.oaklobbies;

import org.plusmc.pluslib.bukkit.managed.Tickable;

import java.util.Calendar;

public class RealtimeHandler implements Tickable {
    private final OakLobbies plugin;

    public RealtimeHandler(OakLobbies plugin) {
        this.plugin = plugin;
    }


    @Override
    public void tick(long tick) {
        if (tick % 10 != 0)
            return;
        plugin.getTimeZones().forEach((player, timeZone) -> {
            Calendar calendar = Calendar.getInstance(timeZone);
            player.setPlayerTime(getTimeInMinecraft(calendar), false);
        });
    }


    private long getTimeInMinecraft(Calendar calendar) {
        long hours = calendar.get(Calendar.HOUR_OF_DAY);
        long minutes = calendar.get(Calendar.MINUTE);
        long seconds = calendar.get(Calendar.SECOND);


        //An hour in minecraft is 1000 ticks
        long mcHours = hours * 1000;
        long mcMinutes = (long) (minutes * 1000 / 60D);
        long mcSeconds = (long) (seconds * (1000 / 60D) / 60D);

        long timeMC = (mcHours + mcMinutes + mcSeconds - 6000) % 24000;
        if (timeMC < 0)
            timeMC += 24000;

        return timeMC;
    }
}
