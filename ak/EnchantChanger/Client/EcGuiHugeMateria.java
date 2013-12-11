package ak.EnchantChanger.Client;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import ak.EnchantChanger.EcContainerHugeMateria;
import ak.EnchantChanger.EcTileEntityHugeMateria;
import ak.EnchantChanger.EnchantChanger;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class EcGuiHugeMateria extends GuiContainer {

	private EcTileEntityHugeMateria tileentity;
	public EcGuiHugeMateria (InventoryPlayer inventoryPlayer, EcTileEntityHugeMateria te)
	{
		super(new EcContainerHugeMateria(inventoryPlayer, te));
		this.tileentity = te;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString(StatCollector.translateToLocal("container.hugeMateria"), 8, 6, 4210752);
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		int texture;
		texture = mc.renderEngine.getTexture(EnchantChanger.EcGuiHuge);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(EnchantChanger.EcGuiHuge);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		int var1;

		var1 = this.tileentity.getMaterializingProgressScaled(18);
		this.drawTexturedModalRect(x + 97, y + 34, 176, 0, var1 + 1, 16);
	}
	protected void keyTyped(char c, int keycode)
	{
		if (keycode == 1 || keycode == mc.gameSettings.keyBindInventory.keyCode)
		{
			mc.thePlayer.closeScreen();
		}
	}

	public void updateScreen()
	{
		super.updateScreen();
		if (!mc.thePlayer.isEntityAlive() || mc.thePlayer.isDead)
		{
			mc.thePlayer.closeScreen();
		}
	}

	public boolean doesGuiPauseGame()
	{
		return false;
	}

}