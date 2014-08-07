package is.absalon.sirens;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockNote;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class NuclearWarningSiren extends Block {
	@SideOnly(Side.CLIENT)
	private IIcon top;
	private boolean isOn;
	
	public NuclearWarningSiren(boolean isOn) {
		super(Material.wood);
		this.setHardness(2f);
		this.setStepSound(Block.soundTypeStone);
		this.setBlockName("nuclearWarningSiren");
		this.isOn = isOn;
		
		if (!isOn) {
			this.setCreativeTab(CreativeTabs.tabRedstone);
		}
		else {
			this.setLightLevel(1.0f);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int _) {
		return side == 1 || side == 0 ? this.top : this.blockIcon;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister ir) {
		String sideText = "sirens:nuclearSiren_side_" + (this.isOn ? "on" : "off");
		this.blockIcon = ir.registerIcon(sideText);
		this.top = ir.registerIcon("sirens:nuclearSiren_top");
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		updateBlock(world, x, y, z);
	}
	
    public void onNeighborBlockChange(World world, int x, int y, int z, Block _) {
    	updateBlock(world, x, y, z);    
    }
    
    private void updateBlock(World world, int x, int y, int z) {
		if (world.isRemote) {
			return;
		}
		if (this.isOn && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
			world.scheduleBlockUpdate(x, y, z, this, 4);
		}
		else if (!this.isOn && world.isBlockIndirectlyGettingPowered(x, y, z)) {
			world.setBlock(x, y, z, Sirens.nuclearSirenOn, 0, 2);
		}
    }
    
    public void updateTick(World world, int x, int y, int z, Random _) {
    	if (!world.isRemote && this.isOn && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
    		world.setBlock(x, y, z, Sirens.nuclearSirenOff, 0, 2);
    	}
    }
    
    public Item getItemDropped(int _, Random __, int ___) {
    	return Item.getItemFromBlock(Sirens.nuclearSirenOff);
    }
    
    public ItemStack createStackedBlock(int _) {
    	return new ItemStack(Sirens.nuclearSirenOff);
    }
}
