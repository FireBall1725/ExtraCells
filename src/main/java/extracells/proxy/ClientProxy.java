package extracells.proxy;

import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import extracells.BlockEnum;
import extracells.render.item.ItemRendererBusFluidExport;
import extracells.render.item.ItemRendererBusFluidImport;
import extracells.render.item.ItemRendererBusFluidStorage;
import extracells.render.item.ItemRendererCertusTank;
import extracells.render.item.ItemRendererLevelEmitterFluid;
import extracells.render.item.ItemRendererSolderingStation;
import extracells.render.item.ItemRendererWalrus;
import extracells.render.tileentity.TileEntityRedererWalrus;
import extracells.render.tileentity.TileEntityRendererBusFluidExport;
import extracells.render.tileentity.TileEntityRendererBusFluidImport;
import extracells.render.tileentity.TileEntityRendererBusFluidStorage;
import extracells.render.tileentity.TileEntityRendererCertusTank;
import extracells.render.tileentity.TileEntityRendererLevelEmitterFluid;
import extracells.render.tileentity.TileEntityRendererMonitorStorageFluid;
import extracells.render.tileentity.TileEntityRendererSolderingStation;
import extracells.tile.TileEntityBusFluidExport;
import extracells.tile.TileEntityBusFluidImport;
import extracells.tile.TileEntityBusFluidStorage;
import extracells.tile.TileEntityCertusTank;
import extracells.tile.TileEntityLevelEmitterFluid;
import extracells.tile.TileEntityMonitorStorageFluid;
import extracells.tile.TileEntitySolderingStation;
import extracells.tile.TileEntityWalrus;

public class ClientProxy extends CommonProxy
{
	public void RegisterRenderers()
	{
		try
		{
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySolderingStation.class, new TileEntityRendererSolderingStation());
			MinecraftForgeClient.registerItemRenderer(BlockEnum.SOLDERINGSTATION.getBlockInstance().blockID, new ItemRendererSolderingStation());

			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBusFluidStorage.class, new TileEntityRendererBusFluidStorage());
			MinecraftForgeClient.registerItemRenderer(BlockEnum.FLUIDSTORAGE.getBlockInstance().blockID, new ItemRendererBusFluidStorage());

			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBusFluidImport.class, new TileEntityRendererBusFluidImport());
			MinecraftForgeClient.registerItemRenderer(BlockEnum.FLUIDIMPORT.getBlockInstance().blockID, new ItemRendererBusFluidImport());

			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBusFluidExport.class, new TileEntityRendererBusFluidExport());
			MinecraftForgeClient.registerItemRenderer(BlockEnum.FLUIDEXPORT.getBlockInstance().blockID, new ItemRendererBusFluidExport());

			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCertusTank.class, new TileEntityRendererCertusTank());
			MinecraftForgeClient.registerItemRenderer(BlockEnum.CERTUSTANK.getBlockInstance().blockID, new ItemRendererCertusTank());

			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWalrus.class, new TileEntityRedererWalrus());
			MinecraftForgeClient.registerItemRenderer(BlockEnum.CHROMIA.getBlockInstance().blockID, new ItemRendererWalrus());

			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLevelEmitterFluid.class, new TileEntityRendererLevelEmitterFluid());
			MinecraftForgeClient.registerItemRenderer(BlockEnum.FLUIDLEVELEMITTER.getBlockInstance().blockID, new ItemRendererLevelEmitterFluid());

			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMonitorStorageFluid.class, new TileEntityRendererMonitorStorageFluid());
		} catch (NullPointerException e)
		{
			System.out.println("Mod ExtraCells: Another mod probably overrid an ExtraCells item causing EC to cancel the registration of ItemRenderers!");
		}
	}
}