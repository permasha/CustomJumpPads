package ru.permasha.customjumppads.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import ru.permasha.customjumppads.CustomJumpPads;
import ru.permasha.customjumppads.util.JsonSerializer;
import ru.permasha.customjumppads.util.VelocityUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JumpPadManager {

    CustomJumpPads plugin;

    public JumpPadManager(CustomJumpPads plugin) {
        this.plugin = plugin;
    }

    List<Player> fallPlayers = new ArrayList<>();

    public List<String> getJumpPads(String world) {
        return new ArrayList<>(plugin.getConfig().getConfigurationSection("jumppads." + world).getKeys(false));
    }

    public List<Player> getFallPlayers() {
        return fallPlayers;
    }

    public void createJumpPad(String name, Location location) {
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);

        String toLocStr = JsonSerializer.toJsonLocation(location);

        FixedMetadataValue value = new FixedMetadataValue(plugin, name);
        armorStand.setMetadata("jumppad", value);

        plugin.getConfig().set("jumppads." + location.getWorld().getName() + "." + name + "." + "location",
                toLocStr);

        plugin.saveConfig();
    }

    public Location getJumpPadToLocation(World world, String name) {
        String str = plugin.getConfig().getString("jumppads." + world.getName() + "." + name + "." + "to");
        return JsonSerializer.fromJsonLocation(str);
    }

    public Location getJumpPadLocation(World world, String name) {
        String str = plugin.getConfig().getString("jumppads." + world.getName() + "." + name + "." + "location");
        return JsonSerializer.fromJsonLocation(str);
    }

    public void setToJumpPad(String name, Location location) {
        plugin.getConfig().set("jumppads." + location.getWorld().getName() + "." + name + "." + "to",
                JsonSerializer.toJsonLocation(location));
        plugin.saveConfig();
    }

    public double getJumpPadSpeed(World world, String name) {
        return plugin.getConfig().getDouble("jumppads." + world.getName() + "." + name + "." + "speed");
    }

    public void setSpeedJumpPad(World world, String name, double speed) {
        plugin.getConfig().set("jumppads." + world.getName() + "." + name + "." + "speed",
                speed);
        plugin.saveConfig();
    }

    public int getJumpPadHeight(World world, String name) {
        return plugin.getConfig().getInt("jumppads." + world.getName() + "." + name + "." + "height");
    }

    public void setSpeedJumpHeight(World world, String name, int height) {
        plugin.getConfig().set("jumppads." + world.getName() + "." + name + "." + "height",
                height);
        plugin.saveConfig();
    }

    public void pushPlayer(Player player, Location location) {
        World world = location.getWorld();
        Collection<Entity> entities = world.getNearbyEntities(location, 2, 2, 2);
        if (!entities.isEmpty()) {
            for (Entity entity : entities) {
                if (entity.getType().equals(EntityType.ARMOR_STAND)) {
                    if (entity.hasMetadata("jumppad")) {
                        String jumppad = entity.getMetadata("jumppad").get(0).asString();

                        Location playerLoc = player.getLocation();
                        Location loc = getJumpPadToLocation(world, jumppad);

                        Vector vec = playerLoc.toVector();
                        Vector vecSec = loc.toVector();

                        int height = getJumpPadHeight(world, jumppad);
                        double speed = getJumpPadSpeed(world, jumppad);

                        Vector vector = VelocityUtil.calculateVelocity(vec, vecSec, height).normalize().multiply(speed);

//                        Vector vector = vecSec.subtract(vec).normalize().multiply(4.3D);
                        player.setVelocity(vector);
                        player.playSound(playerLoc, Sound.BLOCK_PISTON_EXTEND, 1, 1.5F);
                        if (!fallPlayers.contains(player)) {
                            fallPlayers.add(player);
                        }
                    }
                }
            }
        }
    }

}
