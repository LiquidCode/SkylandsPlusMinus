package uk.co.jacekk.bukkit.skylandsplusminus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import org.bukkit.generator.ChunkGenerator;

import org.bukkit.plugin.java.JavaPlugin;
import uk.co.jacekk.bukkit.skylandsplusminus.listeners.MobSpawnListener;
import uk.co.jacekk.bukkit.skylandsplusminus.listeners.PhysicsListener;

public class SkylandsPlusMinus extends JavaPlugin {
    private PlayerMovementWorker playerMovementWorker;
    private TimeSyncWorker timeSyncWorker;
    public List<Entity> fallingEntities = new ArrayList<Entity>();
    public Map<World, World> timeSyncTable = new HashMap<World, World>();
	
	public void onEnable(){
		
		if (true /*|| Config.PREVENT_SAND_FALLING*/){
			this.getServer().getPluginManager().registerEvents(new PhysicsListener(this), this);
		}
		
		if (true /*|| Config.RESTRICT_MOB_SPAWNING*/){
			this.getServer().getPluginManager().registerEvents(new MobSpawnListener(), this);
		}
                
            this.playerMovementWorker = new PlayerMovementWorker(this);
            this.timeSyncWorker = new TimeSyncWorker(this);
            this.getServer().getScheduler().scheduleSyncRepeatingTask(this, this.playerMovementWorker, 0, 1);
            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new WorldLoadWorker(this));
            this.getServer().getScheduler().scheduleSyncRepeatingTask(this, this.timeSyncWorker, 0, 20);
	}
	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id){
		if(id == null || id.isEmpty()){
			id = "offset=0";
		}
		return new uk.co.jacekk.bukkit.skylandsplusminus.generation.ChunkGenerator(id);
	}

	public void cleanFallingEntities() {

	    for (Entity entity : new ArrayList<Entity>(this.fallingEntities)) {
            if (entity.isOnGround()) {
                this.fallingEntities.remove(entity);
            }

            if (!entity.isValid() || entity.isDead()) {
                this.fallingEntities.remove(entity);
            }
        }
    }
}
