package ak.EnchantChanger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ak.EnchantChanger.Client.EcGuiHugeMateria;
import ak.EnchantChanger.Client.EcGuiMaterializer;
import ak.EnchantChanger.Client.EcGuiPortableEnchantment;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{
	public CommonProxy(){}
	public void registerRenderInformation(){}

	public void registerTileEntitySpecialRenderer(){}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		if(id == EnchantChanger.guiIdMaterializer)
		{
			return new EcContainerMaterializer(player.inventory);
		}
		else if(id == EnchantChanger.guiIdPortableEnchantmentTable)
		{
			return new EcContainerPortableEnchantment(player.inventory, world, x, y, z);
		}
		else if(id == EnchantChanger.guiIdHugeMateria)
		{
			TileEntity t = world.getBlockTileEntity(x, y, z);
			if(t != null)
				return new EcContainerHugeMateria(player.inventory, (EcTileEntityHugeMateria)t);
			else
			{
				return null;
			}
		}
		else
			return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if(id == EnchantChanger.guiIdMaterializer)
		{
			return new EcGuiMaterializer(player.inventory);
		}
		else if(id == EnchantChanger.guiIdPortableEnchantmentTable)
		{
			return new EcGuiPortableEnchantment(player.inventory, world, x, y, z);
		}
		else if(id == EnchantChanger.guiIdHugeMateria)
		{
			TileEntity t = world.getBlockTileEntity(x, y, z);
			if(t != null)
				return new EcGuiHugeMateria(player.inventory, (EcTileEntityHugeMateria)t);
			else
			{
				return null;
			}
		}
		else
			return null;
	}
	public World getClientWorld()
	{
		return null;
	}
}