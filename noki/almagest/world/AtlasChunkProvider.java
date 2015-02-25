package noki.almagest.world;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import noki.almagest.AlmagestData;


public class AtlasChunkProvider implements IChunkProvider {
	
	private World world;
	private Random random;

	public AtlasChunkProvider(World world, long seed, boolean optionFlag, String option) {
		
		this.world = world;
		this.random = new Random(seed);
				
	}

	@Override
	public boolean chunkExists(int chunkX, int chunkZ) {
		
		return true;
		
	}

	@Override
	public Chunk provideChunk(int chunkX, int chunkZ) {
		
//		AlmagestCore.log("chunkX/chunkZ is %s/%s.", chunkX, chunkZ);
		
		ChunkPrimer chunkPrimer = new ChunkPrimer();
		
		// central circle.
		if(-3 <= chunkX && chunkX <= 3 && -3 <= chunkZ && chunkZ <= 3) {
			for(int height=1; height<=88; height++) {
				for(int width=0; width<16; width++) {
					for(int depth=0; depth<16; depth++) {
	//					chunkPrimer.setBlockState(width, 88, depth, AlmagestData.blockStar.getStateFromMeta(0));
						// distant from 0:0.
						for(int layer=3; layer>=0; layer--) {
							if(Math.pow(chunkX*16+width, 2)+Math.pow(chunkZ*16+depth, 2)
									<= Math.pow((layer+1)*10, 2)) {
								chunkPrimer.setBlockState(width, height, depth, AlmagestData.blockStar.getStateFromMeta(3-layer));
							}
						}
					}
				}
			}
		}
		// other areas.
		else {
			int times = this.getRoundedNormalDist(0, 1);
			for(int i=0; i<times; i++) {
//				int type = this.random.nextInt(7);
				int type = 3;
				double radius = this.getRoundedNormalDist(1, 1);
				int radiusCeil = (int)Math.ceil(radius);
				int x = this.random.nextInt(15-radiusCeil*2) + radiusCeil;
				int z = this.random.nextInt(15-radiusCeil*2) + radiusCeil;
				int y = this.getRoundedNormalDist(0, 4) + 88;
				for(int width=-radiusCeil; width<=radiusCeil; width++) {
					for(int depth=-radiusCeil; depth<=radiusCeil; depth++) {
						for(int height=-radiusCeil; height<=radiusCeil; height++) {
							if(width*width+depth*depth+height*height <= radius*radius*0.7) {
								chunkPrimer.setBlockState(x+width, y+height, z+depth, AlmagestData.blockStar.getStateFromMeta(type));
	//							AlmagestCore.log("type/radius/x/y/z is %s/%s/%s/%s/%s.", type, radius, x+width, y+height, z+depth);
							}
						}					
					}	
				}
			}
		}
		
		Chunk chunk = new Chunk(this.world, chunkPrimer, chunkX, chunkZ);
		BiomeGenBase[] abiomegenbase =
				this.world.getWorldChunkManager().loadBlockGeneratorData((BiomeGenBase[])null, chunkX * 16, chunkZ * 16, 16, 16);
		byte[] abyte = chunk.getBiomeArray();
		
		for(int i1 = 0; i1 < abyte.length; ++i1) {
			abyte[i1] = (byte)abiomegenbase[i1].biomeID;
		}
		
		chunk.generateSkylightMap();
		return chunk;
		
	}

	@Override
	public Chunk provideChunk(BlockPos pos) {
		
		return this.provideChunk(pos.getX() >> 4, pos.getZ() >> 4);
		
	}

	@Override
	public void populate(IChunkProvider provider, int chunkX, int chunkZ) {
		
		// nothing to do.
		
	}

	@Override
	public boolean func_177460_a(IChunkProvider provider, Chunk chunk, int chunkX, int chunkZ) {
		
		return false;
		
	}

	@Override
	public boolean saveChunks(boolean par1, IProgressUpdate par2) {
		
		return true;
		
	}

	@Override
	public boolean unloadQueuedChunks() {
		
		return false;
		
	}

	@Override
	public boolean canSave() {
		
		return true;
		
	}

	@Override
	public String makeString() {
		
		return "AtlasLevelSource";
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List func_177458_a(EnumCreatureType type, BlockPos pos) {
		
        return this.world.getBiomeGenForCoords(pos).getSpawnableList(type);
        
	}

	@Override
	public BlockPos getStrongholdGen(World worldIn, String p_180513_2_,
			BlockPos p_180513_3_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLoadedChunkCount() {
		
		return 0;
		
	}

	@Override
	public void recreateStructures(Chunk p_180514_1_, int p_180514_2_,
			int p_180514_3_) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveExtraData() {
		
		// nothing to do.
		
	}
	
	private double getNormalDist(double mean, double deviation) {
		
		double res = this.random.nextGaussian();
		res = res*deviation + mean;
		return res;
		
	}
	
	private int getRoundedNormalDist(double mean, double deviation) {
		
		return (int)Math.round(this.getNormalDist(mean, deviation));
		
	}

}
