package HNPCs.Client;

import HNPCs.CommonProxy;
import HNPCs.EntityHNPCs;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerClientInfomations()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityHNPCs.class, new RenderHNPC());
//		RenderingRegistry.registerEntityRenderingHandler(EntityDummyGoal.class, new RenderLiving(new ModelBiped(),0f));
	}
}