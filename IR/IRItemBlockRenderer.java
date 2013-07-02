package IR;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;

import org.lwjgl.opengl.GL11;

/**
 * IDが256以上のブロックを3Dで描画するためのレンダラー
 * 割と適当
 * @author gnsk
 *
 */
public class IRItemBlockRenderer implements IItemRenderer {

	private final Block targetBlock;
	private Random random = new Random();

	public IRItemBlockRenderer(Block targetBlock) {
		this.targetBlock = targetBlock;
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		if (4095 < item.itemID) return false;
		Block b = Block.blocksList[item.itemID];
		if (b == null) return false;
		if (!RenderBlocks.renderItemIn3d(b.getRenderType())) return false ;

		return type == ItemRenderType.INVENTORY || type == ItemRenderType.EQUIPPED || type == ItemRenderType.ENTITY;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if (type == ItemRenderType.INVENTORY) {
			RenderBlocks renderBlock = (RenderBlocks)data[0];
			renderBlock.renderBlockAsItem(targetBlock, 0, 1);
		} else if (type == ItemRenderType.EQUIPPED) {
			RenderBlocks renderBlock = (RenderBlocks)data[0];
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			renderBlock.renderBlockAsItem(targetBlock, 0, 1);
		} else if (type == ItemRenderType.ENTITY) {
			RenderBlocks renderBlock = (RenderBlocks)data[0];
			int stacksize = ((EntityItem)data[1]).getEntityItem().stackSize;
			renderEntityItem(renderBlock, stacksize);
		}
	}

	private void renderEntityItem(RenderBlocks renderBlock, int stacksize) {
		int rendertype = targetBlock.getRenderType();
		float scale = (rendertype == 1 || rendertype == 19 || rendertype == 12 || rendertype == 2) ? 1f : 0.5F;
		int drawcount = stacksize < 2 ? 1 : stacksize < 6 ? 2 : stacksize  < 21 ? 3 : 4;
		this.random.setSeed(187L);
		GL11.glScalef(scale, scale, scale);

		for(int j = 0; j < drawcount; j++)
		{
		    GL11.glPushMatrix();
		    if (j > 0)
		    {
		        GL11.glTranslatef(
		            ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / 0.5F,
		            ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / 0.5F,
		            ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / 0.5F);
		    }
		    renderBlock.renderBlockAsItem(targetBlock, 0, 1);
		    GL11.glPopMatrix();
		}
	}

}
