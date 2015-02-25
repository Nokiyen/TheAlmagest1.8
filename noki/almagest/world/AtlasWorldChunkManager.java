package noki.almagest.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class AtlasWorldChunkManager extends WorldChunkManager {

	private GenLayer genBiomes;
	private GenLayer biomeIndexLayer;
	private BiomeCache biomeCache;
	@SuppressWarnings("rawtypes")
	private List biomesToSpawnIn;
	
	private static BiomeGenBase atlasBiome = new AtlasBiome(0);
	private static ArrayList<BiomeGenBase> allowedBiomes = new ArrayList<BiomeGenBase>(Arrays.asList(atlasBiome));
	
	public static final BiomeGenBase[] biomeList = new BiomeGenBase[256];
	
	static {
		for (int i = 0; i < 256; ++i) {
			biomeList[i] = atlasBiome;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected AtlasWorldChunkManager() {
		
		this.biomeCache = new BiomeCache(this);
		this.biomesToSpawnIn = new ArrayList();
		this.biomesToSpawnIn.addAll(allowedBiomes);
		
	}
	
	public AtlasWorldChunkManager(long par1, WorldType par3WorldType, String option) {
		
		this();
		GenLayer[] var4 = GenLayer.initializeAllBiomeGenerators(par1, par3WorldType, option);
		this.genBiomes = var4[0];
		this.biomeIndexLayer = var4[1];
		
	}
	
	public AtlasWorldChunkManager(World par1World) {
		
		this(par1World.getSeed(), par1World.getWorldInfo().getTerrainType(), par1World.getWorldInfo().getGeneratorOptions());
		
	}
	
	///////////////////////////////////////////////////////////////////////
	// WorldChunkManagerのプライベート変数を使用するメソッドはすべて再実装
	
	@SuppressWarnings("rawtypes")
	@Override
	public List getBiomesToSpawnIn() {
		
		return this.biomesToSpawnIn;
		
	}
	
	@Override
	public BiomeGenBase getBiomeGenerator(BlockPos p_180631_1_) {
		
		return this.func_180300_a(p_180631_1_, (BiomeGenBase)null);
		
	}
	
	@Override
	public BiomeGenBase func_180300_a(BlockPos p_180300_1_, BiomeGenBase p_180300_2_) {
		
		return this.biomeCache.func_180284_a(p_180300_1_.getX(), p_180300_1_.getZ(), p_180300_2_);
		
	}
	
	@Override
	public float[] getRainfall(float[] par1ArrayOfFloat, int par2, int par3, int par4, int par5) {
		
		IntCache.resetIntCache();
		
		if(par1ArrayOfFloat == null || par1ArrayOfFloat.length < par4 * par5) {
			par1ArrayOfFloat = new float[par4 * par5];
		}

		int[] aint = this.biomeIndexLayer.getInts(par2, par3, par4, par5);
		
		for(int i1 = 0; i1 < par4 * par5; ++i1) {
			try {
				float f = (float)biomeList[aint[i1]].getIntRainfall() / 65536.0F;
				if(f > 1.0F) {
					f = 1.0F;
				}
				par1ArrayOfFloat[i1] = f;
			}
			catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("DownfallBlock");
				crashreportcategory.addCrashSection("biome id", Integer.valueOf(i1));
				crashreportcategory.addCrashSection("downfalls[] size", Integer.valueOf(par1ArrayOfFloat.length));
				crashreportcategory.addCrashSection("x", Integer.valueOf(par2));
				crashreportcategory.addCrashSection("z", Integer.valueOf(par3));
				crashreportcategory.addCrashSection("w", Integer.valueOf(par4));
				crashreportcategory.addCrashSection("h", Integer.valueOf(par5));
				throw new ReportedException(crashreport);
			}
		}
		
		return par1ArrayOfFloat;
		
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public float getTemperatureAtHeight(float par1, int par2) {
		
		return par1;
		
	}
	
	@Override
	public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5) {
		
        IntCache.resetIntCache();

        if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5)
        {
        	par1ArrayOfBiomeGenBase = new BiomeGenBase[par4 * par5];
        }

        int[] aint = this.genBiomes.getInts(par2, par3, par4, par5);

        try
        {
            for (int i1 = 0; i1 < par4 * par5; ++i1)
            {
            	par1ArrayOfBiomeGenBase[i1] = biomeList[aint[i1]];
            }

            return par1ArrayOfBiomeGenBase;
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
            crashreportcategory.addCrashSection("biomes[] size", Integer.valueOf(par1ArrayOfBiomeGenBase.length));
            crashreportcategory.addCrashSection("x", Integer.valueOf(par2));
            crashreportcategory.addCrashSection("z", Integer.valueOf(par3));
            crashreportcategory.addCrashSection("w", Integer.valueOf(par4));
            crashreportcategory.addCrashSection("h", Integer.valueOf(par5));
            throw new ReportedException(crashreport);
        }
		
	}
	
	@Override
	public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5) {
		
		return this.getBiomeGenAt(par1ArrayOfBiomeGenBase, par2, par3, par4, par5, true);
		
	}
	
	@Override
	public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5, boolean par6) {
		
        IntCache.resetIntCache();

        if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5)
        {
        	par1ArrayOfBiomeGenBase = new BiomeGenBase[par4 * par5];
        }

        if (par6 && par4 == 16 && par5 == 16 && (par2 & 15) == 0 && (par3 & 15) == 0)
        {
            BiomeGenBase[] abiomegenbase1 = this.biomeCache.getCachedBiomes(par2, par3);
            System.arraycopy(abiomegenbase1, 0, par1ArrayOfBiomeGenBase, 0, par4 * par5);
            return par1ArrayOfBiomeGenBase;
        }
        else
        {
            int[] aint = this.biomeIndexLayer.getInts(par2, par3, par4, par5);

            for (int i1 = 0; i1 < par4 * par5; ++i1)
            {
            	par1ArrayOfBiomeGenBase[i1] = biomeList[aint[i1]];
            }

            return par1ArrayOfBiomeGenBase;
        }
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean areBiomesViable(int par1, int par2, int par3, List par4List) {
		
        IntCache.resetIntCache();
        int l = par1 - par2 >> 2;
        int i1 = par2 - par3 >> 2;
        int j1 = par1 + par3 >> 2;
        int k1 = par2 + par3 >> 2;
        int l1 = j1 - l + 1;
        int i2 = k1 - i1 + 1;
        int[] aint = this.genBiomes.getInts(l, i1, l1, i2);

        try
        {
            for (int j2 = 0; j2 < l1 * i2; ++j2)
            {
                BiomeGenBase biomegenbase = biomeList[aint[j2]];

                if (!par4List.contains(biomegenbase))
                {
                    return false;
                }
            }

            return true;
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
            crashreportcategory.addCrashSection("Layer", this.genBiomes.toString());
            crashreportcategory.addCrashSection("x", Integer.valueOf(par1));
            crashreportcategory.addCrashSection("z", Integer.valueOf(par2));
            crashreportcategory.addCrashSection("radius", Integer.valueOf(par3));
            crashreportcategory.addCrashSection("allowed", par4List);
            throw new ReportedException(crashreport);
        }
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public BlockPos findBiomePosition(int x, int z, int range, List biomes, Random random) {
		
        IntCache.resetIntCache();
        int l = x - range >> 2;
        int i1 = z - range >> 2;
        int j1 = x + range >> 2;
        int k1 = z + range >> 2;
        int l1 = j1 - l + 1;
        int i2 = k1 - i1 + 1;
        int[] aint = this.genBiomes.getInts(l, i1, l1, i2);
        BlockPos blockpos = null;
        int j2 = 0;

        for (int k2 = 0; k2 < l1 * i2; ++k2)
        {
            int l2 = l + k2 % l1 << 2;
            int i3 = i1 + k2 / l1 << 2;
            BiomeGenBase biomegenbase = biomeList[aint[k2]];

            if (biomes.contains(biomegenbase) && (blockpos == null || random.nextInt(j2 + 1) == 0))
            {
                blockpos = new BlockPos(l2, 0, i3);
                ++j2;
            }
        }

        return blockpos;
        
	}
	
	@Override
	public void cleanupCache() {
		
		this.biomeCache.cleanupCache();
		
	}
	
}
