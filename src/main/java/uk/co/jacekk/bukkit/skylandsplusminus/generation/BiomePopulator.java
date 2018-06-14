package uk.co.jacekk.bukkit.skylandsplusminus.generation;

import java.util.Random;

import net.minecraft.server.v1_12_R1.BiomeBase;
import net.minecraft.server.v1_12_R1.BlockPosition;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.generator.BlockPopulator;

public class BiomePopulator extends BlockPopulator {
	
	@Override
	public void populate(World world, Random random, Chunk chunk){
	    int chunkX = chunk.getX() * 16,
                chunkZ = chunk.getZ() * 16;

		Biome biome = world.getBiome(chunkX, chunkZ);
		
		//TODO: Some biomes are not being decorated.
		
		try {
		    BlockPosition pos = new BlockPosition(chunkX, 0, chunkZ);

		    // Default to the forest biome base
		    BiomeBase base = BiomeBase.getBiome(Biome.FOREST.ordinal());
		    // Find the biome base for the biome the world object gives us, if possible
			if (BiomeBase.getBiome(biome.ordinal()) != null) {
                base = BiomeBase.getBiome(biome.ordinal());
            }

            // Populate biome?
            base.a(((CraftWorld) world).getHandle(), random, pos);

		} catch (IllegalArgumentException e){
			System.err.println(e.getMessage());
		} catch (RuntimeException e){
			System.err.println(e.getMessage());
		}
	}
	
}
