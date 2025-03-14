package dev.svks.lifestealPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static dev.svks.lifestealPlugin.LifestealPlugin.getPlugin;
import static org.bukkit.Bukkit.getServer;

public class ItemSystem implements Listener {
    @EventHandler
    public void rightClickEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();

        if (item.getType()== Material.POISONOUS_POTATO && item.getItemMeta().getCustomModelData()==1) {
            AttributeInstance maxHealth = p.getAttribute(Attribute.MAX_HEALTH);

            if (maxHealth.getBaseValue()==40) {
                p.sendMessage("§cYou already have 20 hearts!");
                return;
            }

            item.setAmount(item.getAmount() - 1);
            maxHealth.setBaseValue(maxHealth.getBaseValue() + 2);
        }



        else if (item.getType()==Material.BOOK && item.getItemMeta().getCustomModelData()==2) {
            OfflinePlayer revive = Bukkit.getOfflinePlayer(item.getItemMeta().getDisplayName());

            // non-existing players will get caught here (preventing typos)
            if(!getPlugin().getConfig().getStringList("banned-players").contains(revive.getUniqueId().toString())){
                p.sendMessage("§cThat player isn't banned.");
                return;
            }

            List<String> bannedPlayers = getPlugin().getConfig().getStringList("banned-players");
            bannedPlayers.remove(revive.getUniqueId().toString());
            getPlugin().getConfig().set("banned-players",bannedPlayers);

            List<String> awaitingPlayers = getPlugin().getConfig().getStringList("awaiting-players");
            awaitingPlayers.add(revive.getUniqueId().toString());
            getPlugin().getConfig().set("awaiting-players",awaitingPlayers);
            getPlugin().saveConfig();

            item.setAmount(item.getAmount()-1);

            getServer().broadcastMessage(revive.getName()+"§a was revived by §f"+p.getName()+"§a.");
        }
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(getPlugin().getConfig().getStringList("awaiting-players").contains(p.getUniqueId().toString())) {
            List<String> awaitingPlayers = getPlugin().getConfig().getStringList("awaiting-players");
            awaitingPlayers.remove(p.getUniqueId().toString());
            getPlugin().getConfig().set("awaiting-players",awaitingPlayers);
            getPlugin().saveConfig();

            p.getAttribute(Attribute.MAX_HEALTH).setBaseValue(6);
        }
    }
}
