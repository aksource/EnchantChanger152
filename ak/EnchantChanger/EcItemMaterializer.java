package ak.EnchantChanger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EcItemMaterializer extends Item
{

	public EcItemMaterializer(int par1) {
		super(par1);
		maxStackSize = 1;
        setMaxDamage(0);
	}
	public boolean hasEffect(ItemStack par1ItemStack)
    {
        return false;
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	par3EntityPlayer.openGui(EnchantChanger.instance, EnchantChanger.guiIdMaterializer,par2World,0,0,0);

        return par1ItemStack;
    }
}