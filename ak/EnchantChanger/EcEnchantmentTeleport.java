package ak.EnchantChanger;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

public class EcEnchantmentTeleport extends Enchantment
{
    public EcEnchantmentTeleport(int var1, int var2)
    {
        super(var1, var2, EnumEnchantmentType.weapon);
        this.setName("Teleport");
    }
    public boolean canApplyTogether(Enchantment par1Enchantment)
    {
    	return this != par1Enchantment && par1Enchantment.effectId != EnchantChanger.EnchantmentMeteoId &&par1Enchantment.effectId != EnchantChanger.EndhantmentHolyId&&par1Enchantment.effectId != EnchantChanger.EnchantmentThunderId;
    }
}