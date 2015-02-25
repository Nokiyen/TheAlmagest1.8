package noki.almagest.world;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.almagest.AlmagestData;


public class AtlasWorldProvider extends WorldProvider {
	
	public static WorldType ATLAS = new AtlasWorldType("Atlas");
	
/*	public AtlasWorldProvider() {
		
		this.setDimension(AlmagestData.dimensionID);
		this.setSpawnPoint(new BlockPos(0, 88, 0));
		this.hasNoSky = true;
		
	}*/

	@Override
	public String getDimensionName() {
		
		return "Atlas";
		
	}

	@Override
	public String getInternalNameSuffix() {
		
		return "_atlas";
		
	}
	
	@Override
	protected void registerWorldChunkManager() {
		
		this.worldObj.getWorldInfo().setTerrainType(ATLAS);
		this.worldChunkMgr = new AtlasWorldChunkManager(worldObj);
//		this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.0F);
//		this.setDimension(AlmagestData.dimensionID);
//		this.hasNoSky = true;
//		this.hasNoSky = false;
		this.setDimension(AlmagestData.dimensionID);
		this.setSpawnPoint(new BlockPos(0, 90, 0));
		this.hasNoSky = false;
		
	}

	@Override
	public IChunkProvider createChunkGenerator() {
		
//		return new AtlasChunkProvider(worldObj, worldObj.getSeed(), true, this.worldObj.getWorldInfo().getGeneratorOptions());
		return new AtlasChunkProvider(worldObj, worldObj.getSeed(), true, "");
//		return new ChunkProviderEnd(this.worldObj, this.worldObj.getSeed());
		
	}
	
	@Override
	public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
		
		return 0.0F;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float[] calcSunriseSunsetColors(float p_76560_1_, float p_76560_2_) {
		
		return null;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
		
		int i = 10518688;
		float f2 = MathHelper.cos(p_76562_1_ * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		f2 = MathHelper.clamp_float(f2, 0.0F, 1.0F);
		float f3 = (float)(i >> 16 & 255) / 255.0F;
		float f4 = (float)(i >> 8 & 255) / 255.0F;
		float f5 = (float)(i & 255) / 255.0F;
		f3 *= f2 * 0.0F + 0.15F;
		f4 *= f2 * 0.0F + 0.15F;
		f5 *= f2 * 0.0F + 0.15F;
		
		return new Vec3((double)f3, (double)f4, (double)f5);
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isSkyColored() {
		
		return false;
		
	}
	
	// if return false, minecraft will get crash!!
	@Override
	public boolean canRespawnHere() {
		
		return false;
		
	}
	
	@Override
	public boolean isSurfaceWorld(){
		
		return false;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getCloudHeight() {
		
		return 8.0F;
		
	}
	
	@Override
	public boolean canCoordinateBeSpawn(int x, int z) {
		
		return this.worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)).getMaterial().blocksMovement();
		
	}
	
	@Override
	public BlockPos getSpawnPoint() {
		
		return new BlockPos(0, 90, 0);
		
	}
	
	@Override
	public int getAverageGroundLevel() {
		
		return 88;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean doesXZShowFog(int p_76568_1_, int p_76568_2_) {
		
		return false;
		
	}

}
