package dev.svks.lifestealPlugin;

import io.papermc.paper.ban.BanListType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static dev.svks.lifestealPlugin.LifestealPlugin.getPlugin;
import static org.bukkit.Bukkit.*;

public class RightClickListener implements Listener {
    @EventHandler
    public void rightClickEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();

        if (item.getType()==Material.POISONOUS_POTATO && item.getItemMeta().getCustomModelData()==1) {
            item.setAmount(item.getAmount() - 1);
            AttributeInstance maxHealth = p.getAttribute(Attribute.MAX_HEALTH);
            maxHealth.setBaseValue(maxHealth.getBaseValue() + 2);
        } else if (item.getType()==Material.BOOK && item.getItemMeta().getCustomModelData()==2) {
            OfflinePlayer revive = Bukkit.getOfflinePlayer(item.getItemMeta().getDisplayName());

            if(revive==null){
                p.sendMessage("§cThat player doesn't exist.");
                return;
            }
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

            getServer().broadcastMessage(p.getName()+"§a revived §f"+revive.getName()+"§a.");
        }
    }
}
