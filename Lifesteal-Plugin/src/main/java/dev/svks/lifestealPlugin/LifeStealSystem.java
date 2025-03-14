package dev.svks.lifestealPlugin;

import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

import static dev.svks.lifestealPlugin.LifestealPlugin.getHeartItem;
import static dev.svks.lifestealPlugin.LifestealPlugin.getPlugin;

public class LifeStealSystem implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getPlayer();
        Player k = p.getKiller();
        if(p.equals(k)) return;

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

        if(k==null) return;

        AttributeInstance kMaxHealth = k.getAttribute(Attribute.MAX_HEALTH);
        if (kMaxHealth.getBaseValue()>=40) k.getInventory().addItem(getHeartItem());
        else kMaxHealth.setBaseValue(kMaxHealth.getBaseValue()+2);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(getPlugin().getConfig().getStringList("banned-players").contains(p.getUniqueId().toString()))
            p.kick(Component.text("§cYou ran out of hearts."));
    }
}
