package dev.svks.lifestealPlugin;

import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.time.Duration;
import java.util.List;

import static dev.svks.lifestealPlugin.LifestealPlugin.getPlugin;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getPlayer();
        if(p.getKiller()==null) return;
        if(p.equals(p.getKiller())) return;
        Player k = p.getKiller();

        AttributeInstance kMaxHealth = k.getAttribute(Attribute.MAX_HEALTH);
        kMaxHealth.setBaseValue(kMaxHealth.getBaseValue()+2);

        if(p.getAttribute(Attribute.MAX_HEALTH).getBaseValue()==2) {
            List<String> bannedPlayers = getPlugin().getConfig().getStringList("banned-players");
            bannedPlayers.add(p.getUniqueId().toString());
            getPlugin().getConfig().set("banned-players",bannedPlayers);
            getPlugin().saveConfig();

            p.kick(Component.text("§cYou ran out of hearts."));
            return;
        }

        AttributeInstance pMaxHealth = p.getAttribute(Attribute.MAX_HEALTH);
        pMaxHealth.setBaseValue(pMaxHealth.getBaseValue()-2);
    }
}
