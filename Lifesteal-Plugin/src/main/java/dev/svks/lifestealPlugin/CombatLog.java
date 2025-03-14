package dev.svks.lifestealPlugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;

import static dev.svks.lifestealPlugin.LifestealPlugin.getPlugin;

public class CombatLog implements Listener, Runnable {
    public static final int combatTimer = 5*60;

    public void run() { // Update combat log
        for (Player p : Bukkit.getOnlinePlayers()) {
            int timer = getPlugin().getConfig().getInt("combat." + p.getUniqueId());
            if(timer != 0) {
                timer--;
                getPlugin().getConfig().set("combat." + p.getUniqueId(), timer);
                if (timer == 0) p.sendMessage("§aYou are no longer in combat log.");
                else p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c§lCOMBATLOG: §f§l"+timer));
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        Bukkit.getServer().broadcastMessage("SLÅ");
        if (!(e.getEntity() instanceof Player victim) || !(e.getDamager() instanceof Player attacker)) return;
        Bukkit.getServer().broadcastMessage("SPILLER SLÅ");

        for(Player p : Arrays.asList(victim,attacker)) {
            p.sendMessage("§cYou are now in combat log.");
            getPlugin().getConfig().set("combat." + p.getUniqueId(), combatTimer);
        }
        getPlugin().saveConfig();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (getPlugin().getConfig().getInt("combat." + e.getPlayer().getUniqueId())!=0) {
            e.getPlayer().setHealth(0);
            getPlugin().getConfig().set("combat." + e.getPlayer().getUniqueId(),0);
            getPlugin().saveConfig();
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        getPlugin().getConfig().set("combat." + e.getPlayer().getUniqueId(), 0);
        getPlugin().saveConfig();
    }
}
