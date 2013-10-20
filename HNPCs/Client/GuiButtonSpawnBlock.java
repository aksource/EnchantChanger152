package HNPCs.Client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class GuiButtonSpawnBlock extends GuiButton
{
	private boolean mirrored;
	private String tex = "/mods/hnpcs/textures/guis/spawnSetting.png";
	public GuiButtonSpawnBlock(int id, int x, int y, boolean mirror)
	{
		super(id,x,y, 12,19,"");
		mirrored = mirror;
	}
	public void drawButton(Minecraft par1Minecraft, int par2, int par3)
	{
		if (this.drawButton)
		{
			par1Minecraft.renderEngine.bindTexture(tex);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			boolean flag = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
			int k = 0;
			int l = 176;

			if (!this.enabled)
			{
				l += this.width * 2;
			}
			else if (flag)
			{
				l += this.width;
			}

			if (!this.mirrored)
			{
				k += this.height;
			}

			this.drawTexturedModalRect(this.xPosition, this.yPosition, l, k, this.width, this.height);
		}
	}
}