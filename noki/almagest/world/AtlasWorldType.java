package noki.almagest.world;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;

public class AtlasWorldType extends WorldType {

	public AtlasWorldType(String name) {
		
		super(name);
		
	}

	@Override
	public WorldChunkManager getChunkManager(World world) {
		
		return new AtlasWorldChunkManager(world);
		
	}
	
	@Override
	public IChunkProvider getChunkGenerator(World world, String generatorOptions) {
		
		return new AtlasChunkProvider(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions);
		
	}
	
}
