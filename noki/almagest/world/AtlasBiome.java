package noki.almagest.world;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.almagest.AlmagestData;


public class AtlasBiome extends BiomeGenBase {
	
	

	public AtlasBiome(int biomeID) {
		
		super(biomeID);
		this.spawnableMonsterList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCaveCreatureList.clear();
		this.topBlock = AlmagestData.blockStar.getDefaultState();
		this.fillerBlock = AlmagestData.blockStar.getDefaultState();
		this.theBiomeDecorator = new AtlasBiomeDecorator();
		
		this.setDisableRain();
		this.setBiomeName("Atlas");
		
	}
	
	@SideOnly(Side.CLIENT)
	public int getSkyColorByTemp(float par1) {
		
		return 0;
	
	}
	
}
