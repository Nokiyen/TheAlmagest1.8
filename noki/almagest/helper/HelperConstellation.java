package noki.almagest.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.JarFile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import noki.almagest.AlmagestCore;
import noki.almagest.item.ItemAlmagest;
import noki.almagest.item.ItemBlockConstellation;
import noki.almagest.item.ItemMissingStar;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


/**********
 * @class HelperConstellation
 *
 * @description 星と星座のデータを保持し、取得するためのクラスです。
 * また、ItemStackへのNBT操作を行うメソッドも持ちます。
 */
public class HelperConstellation {
	
	//******************************//
	// define member variables.
	//******************************//
	public static HashMap<Integer, StarData> starList = new HashMap<Integer, StarData>();
	public static ListMultimap<Constellation, Integer> constStar = ArrayListMultimap.create();
	public static ListMultimap<Constellation, LineData> lineList = ArrayListMultimap.create();
	
	public static final int constSize = 88;
	
	
	//******************************//
	// define member methods.
	//******************************//
	//--------------------
	// Static Methods for Getting Star Data.
	//--------------------
	public static StarData getStar(int starNumber) {
		
		return starList.get(starNumber);
		
	}
	
	public static ArrayList<StarData> getStars(int constNumber) {
		
		return getStars(Constellation.getConstFromNumber(constNumber));
		
	}
	
	public static ArrayList<StarData> getStars(Constellation constName) {
		
		ArrayList<StarData> list = new ArrayList<StarData>();
		
		List<Integer> gotList = constStar.get(constName);
		for(Integer each: gotList) {
			list.add(starList.get(each));
		}
		
		return list;
		
	}
	
	public static List<LineData> getLines(int constNumber) {
		
		return getLines(Constellation.getConstFromNumber(constNumber));
		
	}
	
	public static List<LineData> getLines(Constellation constName) {
		
		return lineList.get(constName);
		
	}
	

	//--------------------
	// Static Methods about Stack and NBT.
	//--------------------
	public static ItemStack getConstStack(int constNumber, int stackSize) {
		
		return ItemBlockConstellation.getConstStack(constNumber, stackSize);
		
	}
	
	public static ItemStack getConstStack(Constellation constellation, int stackSize) {
		
		return ItemBlockConstellation.getConstStack(constellation.getId(), stackSize);

	}
	
	public static ItemStack getConstStack(int constNumber, int[] missingStars, int stackSize) {
		
		return ItemBlockConstellation.getConstStack(constNumber, missingStars, stackSize);
		
	}
	
	public static ItemStack getIncompleteConstStack(Constellation constellation, int stackSize) {
		
		return ItemBlockConstellation.getIncompleteConstStack(constellation, stackSize);
		
	}

	public static int getConstNumber(ItemStack stack) {
		
		return ItemBlockConstellation.getConstNumber(stack);
		
	}
	
	public static int[] getMissingStars(ItemStack stack) {
		
		return ItemBlockConstellation.getMissingStars(stack);
		
	}
	
	public static int getMissingStarNumber(ItemStack stack) {
		
		return ItemMissingStar.getMissingStarNumber(stack);
		
	}
	
	public static ItemStack setConstFlagToAlmagest(ItemStack stack, int constNumber, int flag) {
		
		return ItemAlmagest.setConstFlag(stack, constNumber, flag);
		
	}
	
	public static int getConstFlagFromAlmagest(ItemStack stack, int constNumber) {
		
		return ItemAlmagest.getConstFlag(stack, constNumber);
		
	}
	
	public static int getConstMetadata(int constNumber) {
		
		return Constellation.getMetadataFromNumber(constNumber);
		
	}
	
	
	//--------------------
	// Inner Class.
	//--------------------
	public static class StarData {
		
		//*****define member variables.*//
		public int hip;
		public String name;
		public int longitudeH;
		public int longitudeM;
		public double longitudeS;
		public int latitudeD;
		public int latitudeM;
		public double latitudeS;
		public double magnitude;
		public Spectrum spectrum;
		
		//*****define member methods.***//
		public StarData(int hip, String name,
				int longitudeH, int longitudeM, double longitudeS, int latitudeD, int latitudeM, double latitudeS,
				double magnitude, Spectrum spectrum) {
			this.hip = hip;
			this.name = name;
			this.longitudeH = longitudeH;
			this.longitudeM = longitudeM;
			this.longitudeS = longitudeS;
			this.latitudeD = latitudeD;
			this.latitudeM = latitudeM;
			this.latitudeS = latitudeS;
			this.magnitude = magnitude;
			this.spectrum = spectrum;
		}
		
		public double getCalculatedLong() {
			double value = (double)this.longitudeH*15D + (double)this.longitudeM*(15D/60D) + this.longitudeS*(15D/(60D*60D));
			return value;
		}
		
		public double getCalculatedLat() {
			double value = (double)this.latitudeD + (double)this.latitudeM*(1D/60D) + this.latitudeS*(1D/(60D*60D));
			return value;
		}
		
	}
	
	public static class LineData {
		
		//*****define member variables.*//
		public Constellation constName;
		public StarData star1;
		public StarData star2;
		
		//*****define member methods.***//
		public LineData(Constellation constName, int hip1, int hip2) {
			this.constName = constName;
			this.star1 = starList.get(hip1);
			this.star2 = starList.get(hip2);
		}
		
	}
	
	
	//--------------------
	// Enum for Constellations.
	//--------------------
	public enum Spectrum {
		
		//*****define enums.************//
		W("W", 0),	// most blue.
		O("O", 1),
		B("B", 2),
		A("A", 3),	// almost white.
		F("F", 4),
		G("G", 5),
		K("K", 6),
		M("M", 7);	// most red.

		
		//*****define member variables.*//
		private String name;
		private int metadata;
		
		//*****define member methods.***//
		private Spectrum(String name, int metadata) {
			this.name = name;
			this.metadata = metadata;
		}
		
		public String getName() {
			return this.name;
		}
		
		public int getMetadata() {
			return this.metadata;
		}
		
		//-----static methods.----------//
		public static Spectrum getSpectrumFromName(String name) {
			Spectrum[] spectrums = Spectrum.values();
			for(Spectrum each: spectrums) {
				if(each.getName().equals(name)) {
					return each;
				}
			}
			return null;
		}
		
	}
	
	public enum Constellation {
		
		//*****define enums.************//
		And("And",	1,	false),			//Andromeda-アンドロメダ
		Ant("Ant",	2,	false),			//Antlia-ポンプ
		Aps("Aps",	3,	false),			//Apus-ふうちょう
		Aql("Aql",	4,	false,
				MissingStar.Altair),	//Aquila-わし
		Aqr("Aqr",	5,	true),			//Aquarius-みずがめ
		Ara("Ara",	6,	false),			//Ara-さいだん
		Ari("Ari",	7,	true),			//Aries-おひつじ
		Aur("Aur",	8,	false,
				MissingStar.Capella),	//Auriga-ぎょしゃ
		Boo("Boo",	9,	false,
				MissingStar.Arcturus),	//Bootes-うしかい
		Cae("Cae",	10,	false),			//Caelum-ちょうこくぐ
		Cam("Cam",	11,	false),			//Camelopardalis-きりん
		Cap("Cap",	12,	true),			//Capricornus-やぎ
		Car("Car",	13,	false,
				MissingStar.Canopus),	//Carina-りゅうこつ
		Cas("Cas",	14,	false),			//Cassiopeia-カシオペヤ
		Cen("Cen",	15,	false,
				MissingStar.RigilKent,
				MissingStar.Hadar),		//Centaurus-ケンタウルス
		Cep("Cep",	16,	false),			//Cepheus-ケフェウス
		Cet("Cet",	17,	false,
				MissingStar.Mira),		//Cetus-くじら
		Cha("Cha",	18,	false),			//Chamaeleon-カメレオン
		Cir("Cir",	19,	false),			//Circinus-コンパス
		CMa("CMa",	20,	false,
				MissingStar.Sirius),	//Canis Major-おおいぬ
		CMi("CMi",	21,	false,
				MissingStar.Procyon),	//Canis Minor-こいぬ
		Cnc("Cnc",	22,	true),			//Cancer-かに
		Col("Col",	23,	false),			//Columba-はと
		Com("Com",	24,	false),			//Coma Berenices-かみのけ
		CrA("CrA",	25,	false),			//Corona Australis-みなみのかんむり
		CrB("CrB",	26,	false),			//Corona Borealis-かんむり
		Crt("Crt",	27,	false),			//Crater-コップ
		Cru("Cru",	28,	false,
				MissingStar.Acrux,
				MissingStar.Becrux),	//Crux-みなみじゅうじ
		Crv("Crv",	29,	false),			//Corvus-からす
		CVn("CVn",	30,	false),			//Canes Venatici-りょうけん
		Cyg("Cyg",	31,	false,
				MissingStar.Deneb),		//Cygnus-はくちょう
		Del("Del",	32,	false),			//Delphinus-いるか
		Dor("Dor",	33,	false),			//Dorado-かじき
		Dra("Dra",	34,	false),			//Draco-りゅう
		Equ("Equ",	35,	false),			//Equuleus-こうま
		Eri("Eri",	36,	false,
				MissingStar.Achernar),	//Eridanus-エリダヌス
		For("For",	37,	false),			//Fornax-ろ
		Gem("Gem",	38,	true,
				MissingStar.Pollux),	//Gemini-ふたご
		Gru("Gru",	39,	false),			//Grus-つる
		Her("Her",	40,	false),			//Hercules-ヘルクレス
		Hor("Hor",	41,	false),			//Horologium-とけい
		Hya("Hya",	42,	false),			//Hydra-うみへび
		Hyi("Hyi",	43,	false),			//Hydrus-みずへび
		Ind("Ind",	44,	false),			//Indus-インディアン
		Lac("Lac",	45,	false),			//Lacerta-とかげ
		Leo("Leo",	46,	true,
				MissingStar.Regulus),	//Leo-しし
		Lep("Lep",	47,	false),			//Lepus-うさぎ
		Lib("Lib",	48,	true),			//Libra-てんびん
		LMi("LMi",	49,	false),			//Leo Minor-こじし
		Lup("Lup",	50,	false),			//Lupus-おおかみ
		Lyn("Lyn",	51,	false),			//Lynx-やまねこ
		Lyr("Lyr",	52,	false,
				MissingStar.Vega),		//Lyra-こと
		Men("Men",	53,	false),			//Mensa-テーブルさん
		Mic("Mic",	54,	false),			//Microscopium-けんびきょう
		Mon("Mon",	55,	false),			//Monoceros-いっかくじゅう
		Mus("Mus",	56,	false),			//Musca-はえ
		Nor("Nor",	57,	false),			//Norma-じょうぎ
		Oct("Oct",	58,	false),			//Octans-はちぶんぎ
		Oph("Oph",	59,	false),			//Ophiuchus-へびつかい
		Ori("Ori",	60,	false,
				MissingStar.Betelgeuse,
				MissingStar.Rigel),		//Orion-オリオン
		Pav("Pav",	61,	false),			//Pavo-くじゃく
		Peg("Peg",	62,	false),			//Pegasus-ペガスス
		Per("Per",	63,	false),			//Perseus-ペルセウス
		Phe("Phe",	64,	false),			//Phoenix-ほうおう
		Pic("Pic",	65,	false),			//Pictor-がか
		PsA("PsA",	66,	false,
				MissingStar.Fomalhaut),	//Piscis Austrinus-みなみのうお
		Psc("Psc",	67,	true),			//Pisces-うお
		Pup("Pup",	68,	false),			//Puppis-とも
		Pyx("Pyx",	69,	false),			//Pyxis-らしんばん
		Ret("Ret",	70,	false),			//Reticulum-レチクル
		Scl("Scl",	71,	false),			//Sculptor-ちょうこくしつ
		Sco("Sco",	72,	true,
				MissingStar.Antares),	//Scorpius-さそり
		Sct("Sct",	73,	false),			//Scutum-たて
		Ser("Ser",	74,	false),			//Serpens(Cauda)-へび(尾)
		Sex("Sex",	75,	false),			//Sextans-ろくぶんぎ
		Sge("Sge",	76,	false),			//Sagitta-や
		Sgr("Sgr",	77,	true),			//Sagittarius-いて
		Tau("Tau",	78,	true,
				MissingStar.Aldebaran),	//Taurus-おうし
		Tel("Tel",	79,	false),			//Telescopium-ぼうえんきょう
		TrA("TrA",	80,	false),			//Triangulum Australe-みなみのさんかく
		Tri("Tri",	81,	false),			//Triangulum-さんかく
		Tuc("Tuc",	82,	false),			//Tucana-きょしちょう
		UMa("UMa",	83,	false),			//Ursa Major-おおぐま
		UMi("UMi",	84,	false,
				MissingStar.Polaris),	//Ursa Minor-こぐま
		Vel("Vel",	85,	false),			//Vela-ほ
		Vir("Vir",	86,	true,
				MissingStar.Spica),		//Virgo-おとめ
		Vol("Vol",	87,	false),			//Volans-とびうお
		Vul("Vul",	88,	false);			//Vulpecula-こぎつね
		
		
		//*****define member variables.*//
		private String name;
		private int id;
		private boolean ecliptical;	//whether it is an ecliptical constellation(黄道十二星座) or not.
		private boolean incomplete;	//whether it can be incomplete or not.
		private MissingStar[] missingStars;
		
		//*****define member methods.***//
		private Constellation(String name, int id,  boolean ecliptical, MissingStar... missingStars) {
			this.name = name;
			this.id = id;
			this.ecliptical = ecliptical;
			this.incomplete = missingStars.length == 0 ? false : true;
			this.missingStars = missingStars;
		}
		
		public String getName() {
			return this.name;
		}
		
		public int getId() {
			return this.id;
		}
		
		public boolean isEcliptical() {
			return this.ecliptical;
		}
		
		public boolean isIncomplete() {
			return this.incomplete;
		}
		
		public MissingStar[] getMissingStars() {
			return this.missingStars;
		}
		
		//-----static methods.----------//
		public static Constellation getConstFromNumber(int constNumber) {
			constNumber = MathHelper.clamp_int(constNumber, 1, Constellation.values().length) - 1;
			return Constellation.values()[constNumber];
		}
		
		public static Constellation getConstFromString(String constName) {
			Constellation[] constellations = Constellation.values();
			for(Constellation each: constellations) {
				if(each.getName().equals(constName)) {
					return each;
				}
			}
			return null;
		}
		
		public static int getMetadataFromNumber(int constNumber) {
			return getConstFromNumber(constNumber).isEcliptical() == true ? 1 : 0;
		}
		
	}
	
	public enum MissingStar {
		
		//*****define enums.************//
		Altair		("Altair",		97649,	Constellation.Aql),
		Capella		("Capella",		24608,	Constellation.Aur),
		Arcturus	("Arcturus",	69673,	Constellation.Boo),
		Sirius		("Sirius",		32349,	Constellation.CMa),
		Procyon		("Procyon",		37279,	Constellation.CMi),
		Canopus		("Canopus",		30438,	Constellation.Car),
		RigilKent	("RigilKent",	71683,	Constellation.Cen),
		Hadar		("Hadar",		68702,	Constellation.Cen),
		Acrux		("Acrux",		60718,	Constellation.Cru),
		Becrux		("Becrux",		62434,	Constellation.Cru),
		Deneb		("Deneb",		102098,	Constellation.Cyg),
		Achernar	("Achernar",	7588,	Constellation.Eri),
		Pollux		("Pollux",		37826,	Constellation.Gem),
		Regulus		("Regulus",		49669,	Constellation.Leo),
		Vega		("Vega",		91262,	Constellation.Lyr),
		Betelgeuse	("Betelgeuse",	27989,	Constellation.Ori),
		Rigel		("Rigel",		24436,	Constellation.Ori),
		Fomalhaut	("Fomalhaut",	113368,	Constellation.PsA),
		Antares		("Antares",		80763,	Constellation.Sco),
		Aldebaran	("Aldebaran",	21421,	Constellation.Tau),
		Spica		("Spica",		65474,	Constellation.Vir),
		Mira		("Mira",		10826,	Constellation.Cet),
		Polaris		("Polaris",		11767,	Constellation.UMi);
		
		
		//*****define member variables.*//
		private String name;
		private int hip;
		private Constellation constellation;
		
		//*****define member methods.***//
		private MissingStar(String name, int hip, Constellation constellation) {
			this.name = name;
			this.hip = hip;
			this.constellation = constellation;
		}
		
		public String getName() {
			return this.name;
		}

		public int getStarNumber() {
			return this.hip;
		}
		
		public Constellation getConst() {
			return this.constellation;
		}
		
	}
	
	
	//--------------------
	// Method for Registering Star Data.
	//--------------------
	public static void registerStarData(FMLPreInitializationEvent event) {
		
		File file = event.getSourceFile();	// get the mod's jar file.
		try {
			JarFile jar = new JarFile(file);
			InputStream inputStream;
			BufferedReader reader;
			String container = "";	// local variable for containing csv's line.
			
			//	resolve constellation_star.csv.
			inputStream = jar.getInputStream(jar.getJarEntry("assets/almagest/csv/constellation_star.csv"));
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String[] stars = new String[10];
			
			while((container = reader.readLine()) != null) {
				StringTokenizer tokens = new StringTokenizer(container, ",");
				for(int i = 0; tokens.hasMoreTokens(); i++) {
					stars[i] = tokens.nextToken();
				}
				StarData star = new StarData(
						Integer.valueOf(stars[0]),
						stars[1] == "null" ? null : stars[1],
						Integer.valueOf(stars[2]), Integer.valueOf(stars[3]), Double.valueOf(stars[4]),
						Integer.valueOf(stars[5]), Integer.valueOf(stars[6]), Double.valueOf(stars[7]),
						Double.valueOf(stars[8]), Spectrum.getSpectrumFromName(stars[9]));
				starList.put(Integer.valueOf(stars[0]), star);
			}
			reader.close();
			
			//	resolve constellation_star.csv.
			inputStream = jar.getInputStream(jar.getJarEntry("assets/almagest/csv/constellation_composition.csv"));
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String[] compositions = new String[2];
			
			while((container = reader.readLine()) != null) {
				StringTokenizer tokens = new StringTokenizer(container, ",");
				for(int i = 0; tokens.hasMoreTokens(); i++) {
					compositions[i] = tokens.nextToken();
				}
				constStar.put(Constellation.getConstFromString(compositions[0]), Integer.valueOf(compositions[1]));
			}
			reader.close();
			
			//	resolve constellation_star.csv.
			inputStream = jar.getInputStream(jar.getJarEntry("assets/almagest/csv/constellation_line.csv"));
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String[] lines = new String[3];
			
			while((container = reader.readLine()) != null) {
				StringTokenizer tokens = new StringTokenizer(container, ",");
				for(int i = 0; tokens.hasMoreTokens(); i++) {
					lines[i] = tokens.nextToken();
				}
				LineData line = new LineData(Constellation.getConstFromString(lines[0]),
						Integer.valueOf(lines[1]), Integer.valueOf(lines[2]));
				lineList.put(Constellation.getConstFromString(lines[0]), line);
			}
			reader.close();			
			
			jar.close();
		}
		catch(IOException e) {
			AlmagestCore.log(e.getMessage());
		}		
	
	}

}
