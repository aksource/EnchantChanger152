package IR;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.MinecraftForgeClient;
import IR.IRUtil.IRInvalidValueException;
import IR.IRUtil.Record;
import IR.IRUtil.RecordBlock;

public class IRConstructorBlock extends IRConstructorBase {

	protected static Map<String, Material> materialMap = new HashMap<String, Material>();
	protected static Map<String, StepSound> soundMap = new HashMap<String, StepSound>();

	static {
		materialMap.put("air", Material.air);
		materialMap.put("cactus", Material.cactus);
		materialMap.put("cake", Material.cake);
		materialMap.put("circuits", Material.circuits);
		materialMap.put("clay", Material.clay);
		materialMap.put("cloth", Material.cloth);
		materialMap.put("craftedsnow", Material.craftedSnow);
		materialMap.put("dragonegg", Material.dragonEgg);
		materialMap.put("fire", Material.fire);
		materialMap.put("glass", Material.glass);
		materialMap.put("grass", Material.grass);
		materialMap.put("ground", Material.ground);
		materialMap.put("ice", Material.ice);
		materialMap.put("iron", Material.iron);
		materialMap.put("lava", Material.lava);
		materialMap.put("leaves", Material.leaves);
		materialMap.put("piston", Material.piston);
		materialMap.put("plants", Material.plants);
		materialMap.put("portal", Material.portal);
		materialMap.put("pumpkin", Material.pumpkin);
		materialMap.put("redstonelight", Material.redstoneLight);
		materialMap.put("rock", Material.rock);
		materialMap.put("sand", Material.sand);
		materialMap.put("snow", Material.snow);
		materialMap.put("sponge", Material.sponge);
		materialMap.put("tnt", Material.tnt);
//		materialMap.put("unused", Material.unused);
		materialMap.put("vine", Material.vine);
		materialMap.put("water", Material.water);
		materialMap.put("web", Material.web);
		materialMap.put("wood", Material.wood);

		soundMap.put("cloth", Block.soundClothFootstep);
		soundMap.put("glass", Block.soundGlassFootstep);
		soundMap.put("grass", Block.soundGrassFootstep);
		soundMap.put("gravel", Block.soundGravelFootstep);
		soundMap.put("metal", Block.soundMetalFootstep);
		soundMap.put("power", Block.soundPowderFootstep);
		soundMap.put("sand", Block.soundSandFootstep);
		soundMap.put("stone", Block.soundStoneFootstep);
		soundMap.put("wood", Block.soundWoodFootstep);
	}

	@Override
	public Object construct(Record record) throws IRInvalidValueException {
		RecordBlock rb = (RecordBlock)record;
		Material material = materialMap.get(rb.material.toLowerCase());
		if (material == null)
			throw new IRInvalidValueException("未知のmaterialが指定されました");

		if (record.id < 0 || 4096 <= record.id)
			throw new IRInvalidValueException("BlockIDは0から4095までの値を設定してください");

		return construct(rb, material);

	}

	protected Object construct(RecordBlock record, Material material) throws IRInvalidValueException {
		IRForgeBlock block = new IRForgeBlock(record.id, record.textureFile, material, record.isOpaqueCube, record.renderType, record.collidable, record.alphaBlending);
		block.setDropItems(record.dropItems);
		return setParams(block, record);
	}

	protected Object setParams(Block block, RecordBlock record) throws IRInvalidValueException {
		StepSound sound = soundMap.get(record.stepSound.toLowerCase());
		if (sound == null)
			throw new IRInvalidValueException("未知のstepSoundが指定されました");

		block.setStepSound(sound);
		block.setHardness(record.hardness);
		block.setLightValue(record.lightValue);
		block.setResistance(record.blastRegistantce);
		block.setUnlocalizedName("InstantRecipesBlock" + record.id + record.name);
//		block.blockIndexInTexture = record.textrueIndex;
		if (record.blockHeight != 1f) block.setBlockBounds(0, 0, 0, 1, record.blockHeight, 1);

		ItemBlock itemblock = new ItemBlock(record.id - 256);

		if (255 < block.blockID) {
			MinecraftForgeClient.registerItemRenderer(block.blockID, new IRItemBlockRenderer(block));
		}

		return super.setParams(block, record);
	}
}
