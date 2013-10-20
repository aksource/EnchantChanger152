package HNPCs;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if(packet.channel.equals("HNPC|Spawn"))
		{
			ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
			 
			int x = data.readInt();
			int y = data.readInt();
			int z = data.readInt();
	 
			TileEntity tileentity = null;
	 
			World worldClient = FMLClientHandler.instance().getClient().theWorld;
			World worldServer = ((EntityPlayer)player).worldObj;
	 
			if(worldClient != null && worldServer == null){
				tileentity = worldClient.getBlockTileEntity(x, y, z);
			}
			if(worldServer != null){
				tileentity = worldServer.getBlockTileEntity(x, y, z);
			}
			if (tileentity != null) {
				if(tileentity instanceof TileSpawnBlock) ((TileSpawnBlock)tileentity).readToPacketC(data);
			}
		}
	}
	public static Packet getPacketTileSpawn(TileSpawnBlock tile)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeInt(tile.xCoord);
			dos.writeInt(tile.yCoord);
			dos.writeInt(tile.zCoord);
//			if(!tile.worldObj.isRemote)
				tile.writeToPacketC(dos);
//			else
//				tile.writeToPacket(dos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "HNPC|Spawn"; // ここでチャンネルを設定する
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();

		return packet;
	}
}