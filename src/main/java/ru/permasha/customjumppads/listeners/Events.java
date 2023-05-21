package ru.permasha.customjumppads.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import ru.permasha.customjumppads.CustomJumpPads;

import java.util.ArrayList;
import java.util.List;

public class Events implements Listener {

    CustomJumpPads plugin;

    public Events(CustomJumpPads plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        if(from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
            if (event.getTo() != null && event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.LIME_STAINED_GLASS)) {
                Location location = event.getTo().getBlock().getRelative(BlockFace.DOWN).getLocation();
                plugin.getManager().pushPlayer(player, location);

            }
        }
    }

    @EventHandler
    public void onLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        if (plugin.getManager().getJumpPads(world.getName()) != null) {
            for (String jdName : plugin.getManager().getJumpPads(world.getName())) {
                Location location = plugin.getManager().getJumpPadLocation(world, jdName);
                ArmorStand armorStand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
                armorStand.setInvisible(true);
                armorStand.setInvulnerable(true);
                armorStand.setGravity(false);

                FixedMetadataValue value = new FixedMetadataValue(plugin, jdName);
                armorStand.setMetadata("jumppad", value);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void entityDamageEvent(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (plugin.getManager().getFallPlayers().contains(player)) {
                    plugin.getManager().getFallPlayers().remove(player);
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        plugin.getManager().getFallPlayers().remove(player);
    }

}
