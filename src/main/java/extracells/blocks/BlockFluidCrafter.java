package extracells.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extracells.Extracells;
import extracells.tile.TileEntityFluidCrafter;

public class BlockFluidCrafter extends RotatableColorBlock
{

	@SideOnly(Side.CLIENT)
	Icon icon;

	public BlockFluidCrafter(int id)
	{
		super(id, Material.rock);
		this.setCreativeTab(extracells.Extracells.ModTab);
		this.setUnlocalizedName("block.fluid.crafter");
		this.setHardness(2.0F);
		this.setResistance(10.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityFluidCrafter();
	}

	public Icon getIcon(int side, int metadata)
	{
		return icon;
	}


	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconregister)
	{
		icon = iconregister.registerIcon("extracells:fluid.crafter");
	}
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float offsetX, float offsetY, float offsetZ)
	{
		if (!world.isRemote)
		{
			if (world.getBlockTileEntity(x, y, z) == null || player.isSneaking())
			{
				return false;
			}
			player.openGui(Extracells.instance, 8, world, x, y, z);
		}
		return true;
	}
}
