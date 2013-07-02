package IR;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import IR.IRUtil.RecordBlock.DropItem;

public class IRForgeBlock extends Block{

	protected String textureBaseName;
	protected boolean isOpaqueCube;
	protected int renderType;
	protected boolean asNormalBlock;
	protected boolean collidable;
	protected boolean alphaBlending;

	protected DropItem[] dropitems = null;
	private float totalChance = 1;
	private Icon[] icons = new Icon[6];

	public IRForgeBlock(int id, String textureFile, Material material, boolean isOpaqueCube, int renderType, boolean collidable, boolean alphaBlending) {
		super(id, material);
//		this.textureFile = textureFile;
		this.isOpaqueCube = isOpaqueCube;
		this.renderType = renderType;
		this.collidable = collidable;
		this.alphaBlending = alphaBlending;
		this.asNormalBlock = renderType == 0 && !alphaBlending;

		opaqueCubeLookup[id] = this.isOpaqueCube();
        lightOpacity[id] = this.isOpaqueCube() ? 255 : 0;
	}

	public void setDropItems(DropItem[] d) {
		this.dropitems = d;
		totalChance = 0;
		if (d != null && d.length != 0) {
			for (DropItem di : d) {
				totalChance += di.chance;
			}
		}
	}

//	@Override
//	public void setBlockBounds(float par1, float par2, float par3, float par4, float par5, float par6) {
//		super.setBlockBounds(par1, par2, par3, par4, par5, par6);
//		asNormalBlock = false;
//	}

	@Override
	public int getRenderType() {
		return renderType;
	}

	public boolean renderAsNormalBlock()
    {
        return asNormalBlock;
    }

//	@Override
//	public String getTextureFile() {
//		return textureFile;
//	}

	@Override
	public boolean isOpaqueCube() {
		return isOpaqueCube;
	}

	@Override
	public int getRenderBlockPass() {
		return this.alphaBlending ? 1 : 0;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		if (collidable) {
			return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
		} else {
			return null;
		}
	}

	@Override
	public void dropBlockAsItemWithChance(World world, int i, int j, int k, int meta, float lostrate, int fotune) {
		if (!world.isRemote)
        {
			if (dropitems == null || dropitems.length == 0) {
				super.dropBlockAsItemWithChance(world, i, j, k, meta, lostrate, fotune);
			} else {

				DropItem selectedItem = null;
				float f = world.rand.nextFloat() * totalChance;
				for (DropItem di : dropitems) {
					f -= di.chance;
					if(f <= 0) {
						selectedItem = di;
						break;
					}
				}

				int count = selectedItem.min + world.rand.nextInt(1 + fotune);
				if (selectedItem.max != 0) {
					count += world.rand.nextInt(selectedItem.max - selectedItem.min + 1);
					count = Math.min(count, selectedItem.max);
				}

				for (int l = 0; l < count; l++) {
					this.dropBlockAsItem_do(world, i, j, k, new ItemStack(selectedItem.id, 1, selectedItem.meta));
				}

			}
		}
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5) {
		if (alphaBlending) {
			int id = par1iBlockAccess.getBlockId(par2, par3, par4);
			return id == this.blockID ? false : super.shouldSideBeRendered(par1iBlockAccess, par2, par3, par4, par5);
		} else {
			return super.shouldSideBeRendered(par1iBlockAccess, par2, par3, par4, par5);
		}
	}
}
