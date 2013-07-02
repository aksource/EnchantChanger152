package IR.IRUtil;

import java.util.Map;


/**
 * レコードをファイルの記述形式やマインクラフトの実装に依存せずに表現するためのクラス
 * @author gnsk
 *
 */
public abstract class Record {

	public static class Recipe {
		public String[] form;
		public Map<Character, Stuff> partsMap;
		public Stuff[] parts;
		public Stuff smelting;
		public int meta;
		public int productCount;
	}

	public static class Stuff {
		public int id;
		public int meta;

		public Stuff(int id) {
			this(id, -1);
		}

		public Stuff(int id, int meta) {
			this.id = id;
			this.meta = meta;
		}

		public boolean hasMetadata() {
			return this.meta != -1;
		}
	}

	public static class NameEntry {
		public String region;
		public String name;

		public NameEntry(String region, String name) {
			this.region = region;
			this.name = name;
		}
	}

	public static class Enchantment {
		public int id;
		public int level;

		public Enchantment(int id, int level) {
			this.id = id;
			this.level = level;
		}
	}

	public int id;
	public String name;
	public NameEntry[] localNames;
	public int textrueIndex;
	public String textureFile;
	public Recipe recipe;
	public int burnTime;
	public boolean canBindTexture;
	public Enchantment[] enchantments;

	public abstract String getRecordType();
	public abstract int getItemListIndex();
}
