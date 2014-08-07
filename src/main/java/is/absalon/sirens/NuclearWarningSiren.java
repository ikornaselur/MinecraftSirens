package is.absalon.sirens;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockNote;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
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
	private final boolean isOn;
	private boolean isPlaying = false;
	
	public NuclearWarningSiren(boolean isOn) {
		super(Material.iron);
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
		String sideText = Sirens.MODID + ":nuclearSiren_side_" + (this.isOn ? "on" : "off");
		this.blockIcon = ir.registerIcon(sideText);
		this.top = ir.registerIcon(Sirens.MODID + ":nuclearSiren_top");
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		updateBlock(world, x, y, z);
		if (this.isOn) {
			world.playSoundEffect((double)x, (double)y, (double)z, Sirens.MODID + ":siren.nuclear", 10f, 0.75f);
		}
	}
	
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
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
    
    public void updateTick(World world, int x, int y, int z, Random random) {
    	if (world.isRemote) {
    		return;
    	}
    	else if (this.isOn && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
			this.isPlaying = false;
			world.setBlock(x, y, z, Sirens.nuclearSirenOff, 0, 2);
    	}
    }
    
    public Item getItemDropped(int i, Random random, int j) {
    	return Item.getItemFromBlock(Sirens.nuclearSirenOff);
    }
    
    public ItemStack createStackedBlock(int i) {
    	return new ItemStack(Sirens.nuclearSirenOff);
    }
}
