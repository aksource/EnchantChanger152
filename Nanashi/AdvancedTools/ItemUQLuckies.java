package Nanashi.AdvancedTools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemUQLuckies extends ItemUniqueArms
{
    protected ItemUQLuckies(int var1, EnumToolMaterial var2)
    {
        super(var1, var2);
    }

    protected ItemUQLuckies(int var1, EnumToolMaterial var2, int var3)
    {
        super(var1, var2, var3);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
    	this.itemIcon = par1IconRegister.registerIcon(AdvancedTools.textureDomain + "Luckluck");
    }
    /**
     * Called when item is crafted/smelted. Used only by maps so far.
     */
    public void onCreated(ItemStack var1, World var2, EntityPlayer var3)
    {
        super.onCreated(var1, var2, var3);
        var1.addEnchantment(Enchantment.looting, 7);
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(ItemStack var1, World var2, Entity var3, int var4, boolean var5)
    {
        super.onUpdate(var1, var2, var3, var4, var5);

        if (!var1.hasTagCompound())
        {
            var1.addEnchantment(Enchantment.looting, 7);
        }
    }

    /**
     * Returns the damage against a given entity.
     */
    public int getDamageVsEntity(Entity var1)
    {
        int var2 = super.getDamageVsEntity(var1);

        if (var1 instanceof EntityLiving)
        {
            EntityLiving var3 = (EntityLiving)var1;
            int var4 = var3.getHealth();

            if (var4 <= var2 && var4 > 0 && var3.hurtTime <= 0)
            {
                int var5 = (int)(2.0F * (float)var3.experienceValue);
                var3.experienceValue = var5;
            }
        }

        return var2;
    }
}
