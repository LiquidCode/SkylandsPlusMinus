/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.jacekk.bukkit.skylandsplusminus;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 *
 * @author sl
 */
public class PlayerMovementWorker implements Runnable {
    private SkylandsPlusMinus plugin;

    public PlayerMovementWorker(SkylandsPlusMinus plugin) {
        this.plugin = plugin;
    }
    
    public void run() {
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            testForSkylandsWarp(player);
            testForOverworldWarp(player);
        }

        // Teleport items, mobs etc. down from Skylands
        for (World world : this.plugin.getServer().getWorlds()) {
            testForSkylandsEntities(world);
        }
    }
        
    public void testForSkylandsWarp(Player player) {
        Location loc = player.getLocation();
        if (loc.getWorld().getEnvironment() != World.Environment.NORMAL
                || loc.getWorld().getGenerator() instanceof uk.co.jacekk.bukkit.skylandsplusminus.generation.ChunkGenerator
                || loc.getWorld().getName().endsWith("_skylands")) {
            return;
        }
        
        if (loc.getBlockY() > loc.getWorld().getMaxHeight() + 16) {
            World target = this.plugin.getServer().getWorld(loc.getWorld().getName() + "_skylands");
            if (target == null) {
                return;
            }
            Location targetLoc = new Location(target, loc.getBlockX(), 224 + 32, loc.getBlockZ());
            targetLoc.setPitch(loc.getPitch());
            targetLoc.setYaw(loc.getYaw());
            player.teleport(targetLoc);
            if (!player.isInvulnerable() && !this.plugin.fallingEntities.contains(player)) {
                this.plugin.fallingEntities.add(player);
            }
        }
    }

    public void testForOverworldWarp(Entity player) {
        Location loc = player.getLocation();
        if (loc.getWorld().getEnvironment() != World.Environment.NORMAL || (
                !(loc.getWorld().getGenerator() instanceof uk.co.jacekk.bukkit.skylandsplusminus.generation.ChunkGenerator)
                && !loc.getWorld().getName().endsWith("_skylands")
                )) {
            return;
        }
        
        if (loc.getBlockY() < 96) {
            String worldname = loc.getWorld().getName();
            worldname = worldname.substring(0, worldname.length() - "_skylands".length());
            World target = this.plugin.getServer().getWorld(worldname);
        
            if (target == null) {
                return;
            }

            Location targetLoc = new Location(target, loc.getBlockX(), target.getMaxHeight() + 8, loc.getBlockZ());
            targetLoc.setPitch(loc.getPitch());
            targetLoc.setYaw(loc.getYaw());
            player.teleport(targetLoc);

            if (!player.isInvulnerable() && !this.plugin.fallingEntities.contains(player)) {
                this.plugin.fallingEntities.add(player);
            }
        }
    }

    public void testForSkylandsEntities(World world) {
        // Only process skylands worlds
        if (!(world.getGenerator() instanceof uk.co.jacekk.bukkit.skylandsplusminus.generation.ChunkGenerator
                || world.getName().endsWith("_skylands"))) {
            return;
        }

        world.getEntities().forEach((entity) -> {
            if (!(entity instanceof Player)) {
                this.testForOverworldWarp(entity);
            }
        });
    }
}
