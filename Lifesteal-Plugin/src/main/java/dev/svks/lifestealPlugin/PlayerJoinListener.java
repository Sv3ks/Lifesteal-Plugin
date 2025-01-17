package dev.svks.lifestealPlugin;

import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

import static dev.svks.lifestealPlugin.LifestealPlugin.getPlugin;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(getPlugin().getConfig().getStringList("banned-players").contains(p.getUniqueId().toString())) {
            p.kick(Component.text("§cYou ran out of hearts."));
        } else if(getPlugin().getConfig().getStringList("awaiting-players").contains(p.getUniqueId().toString())) {
            List<String> awaitingPlayers = getPlugin().getConfig().getStringList("awaiting-players");
            awaitingPlayers.remove(p.getUniqueId().toString());
            getPlugin().getConfig().set("awaiting-players",awaitingPlayers);
            getPlugin().saveConfig();

            p.getAttribute(Attribute.MAX_HEALTH).setBaseValue(6);
        }
    }
}
