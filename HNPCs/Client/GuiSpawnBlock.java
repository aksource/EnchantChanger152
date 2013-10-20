package HNPCs.Client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import HNPCs.ContainerSpawnBlock;
import HNPCs.EntityHNPCs;
import HNPCs.PacketHandler;
import HNPCs.TileSpawnBlock;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSpawnBlock extends GuiContainer
{
	private TileSpawnBlock tilespawn;
	private String tex = "/mods/hnpcs/textures/guis/spawnSetting.png";
	private GuiButtonSpawnBlock nextSpawnMax;
	private GuiButtonSpawnBlock prevSpawnMax;
	private GuiButtonSpawnInf infSpawn;
	private EntityHNPCs entityHNPC;
	
	public GuiSpawnBlock(InventoryPlayer inv, TileSpawnBlock tile)
	{
		super(new ContainerSpawnBlock(inv,tile));
		tilespawn = tile;
	}
	@Override
	public void initGui()
	{
		super.initGui();
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.buttonList.add(this.nextSpawnMax = new GuiButtonSpawnBlock(1, i + 120, j + 30, true));
		this.buttonList.add(this.prevSpawnMax = new GuiButtonSpawnBlock(2, i + 80, j + 30, false));
		this.buttonList.add(this.infSpawn = new GuiButtonSpawnInf(3, i + 80, j + 49));
		this.nextSpawnMax.enabled = false;
		this.prevSpawnMax.enabled = false;
		this.infSpawn.enabled = true;
		entityHNPC = new EntityHNPCs(this.mc.theWorld);
	}
	public void updateScreen()
	{
		super.updateScreen();
		this.nextSpawnMax.enabled = !this.tilespawn.isSpawnInfinitely() || this.tilespawn.getMaxSpawnNum() > Integer.MAX_VALUE - 1;
		this.prevSpawnMax.enabled = !this.tilespawn.isSpawnInfinitely() || this.tilespawn.getMaxSpawnNum() < 2;
		this.infSpawn.sw = this.tilespawn.isSpawnInfinitely();
		for(int j = 0;j<5;j++)
		{
			if(j != 0)
				entityHNPC.setCurrentItemOrArmor(j, tilespawn.getStackInSlot(5 - j));
			else
				entityHNPC.setCurrentItemOrArmor(j, tilespawn.getStackInSlot(j));
		}
	}
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		int maxS = this.tilespawn.getMaxSpawnNum();
		boolean inf = this.tilespawn.isSpawnInfinitely();
		int color = (inf)? 0:4210752;
//		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
		this.fontRenderer.drawString(String.valueOf(maxS), 95, this.ySize - 130, color);
		this.fontRenderer.drawString("Max", 136, this.ySize - 130, color);
		this.fontRenderer.drawString("Spawn Infinitly", 96, this.ySize - 113, color);
		this.fontRenderer.drawString(String.format("X:%d, Y: %d, Z: %d", tilespawn.goalPosX,tilespawn.goalPosY,tilespawn.goalPosZ), 80, this.ySize - 100, color);
	}
	@Override
    protected void actionPerformed(GuiButton button)
    {
		boolean flag = false;
		if(button == this.nextSpawnMax)
		{
			this.tilespawn.setMaxSpawnNum(this.tilespawn.getMaxSpawnNum() + 1);
			flag = true;
		}
		else if(button == this.prevSpawnMax)
		{
			this.tilespawn.setMaxSpawnNum(this.tilespawn.getMaxSpawnNum() - 1);
			flag = true;
		}
		else if(button == this.infSpawn)
		{
			this.tilespawn.setSpawnInfinitely(!this.tilespawn.isSpawnInfinitely());
			flag = true;
		}
		if(flag)
		{
			PacketDispatcher.sendPacketToServer(PacketHandler.getPacketTileSpawn(tilespawn));
		}
    }
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(tex);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        drawMobOnGui(entityHNPC, k + 51, l + 75, 30);
	}
	public static void drawMobOnGui(Entity entity, int x, int y, int z)
	{
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y, 50.0F);
		GL11.glScalef((float)(-z), (float)z, (float)z);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);

		GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-((float)Math.atan((double)(0 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);

//		GL11.glTranslatef(0.0F, mc.thePlayer.yOffset, 0.0F);
		RenderManager.instance.playerViewY = 180.0F;
		RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);

		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
}