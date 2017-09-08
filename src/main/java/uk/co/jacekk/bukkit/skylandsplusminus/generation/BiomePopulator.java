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
		Biome biome = world.getBiome(chunk.getX() * 16, chunk.getZ() * 16);
		
		//TODO: Some biomes are not being decorated.
		
		try{
                        
		
			if (BiomeBase.getBiome(biome.ordinal()) != null) {
                            BiomeBase.getBiome(biome.ordinal()).a(((CraftWorld) world).getHandle(), random, new BlockPosition(chunk.getX() * 16, 0, chunk.getZ() * 16));
                        } else {
                            BiomeBase.getBiome(Biome.FOREST.ordinal()).a(((CraftWorld) world).getHandle(), random, new BlockPosition(chunk.getX() * 16, 0, chunk.getZ() * 16));                            
                        }

		}catch (IllegalArgumentException e){
			System.err.println(e.getMessage());
		}catch (RuntimeException e){
			System.err.println(e.getMessage());
		}
	}
	
}
