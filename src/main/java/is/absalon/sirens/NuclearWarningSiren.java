package is.absalon.sirens;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



public class NuclearWarningSiren extends Block {
	@SideOnly(Side.CLIENT)
	private IIcon top;
	
	public NuclearWarningSiren(Material material) {
		super(material);
		setHardness(2f);
		setStepSound(Block.soundTypeStone);
		setCreativeTab(CreativeTabs.tabBlock);
		setBlockName("nuclearWarningSiren");
//		setBlockTextureName("sirens:nuclearSiren_side");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int _) {
		return side == 1 || side == 0 ? this.top : this.blockIcon;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister ir) {
		this.blockIcon = ir.registerIcon("sirens:nuclearSiren_side");
		this.top = ir.registerIcon("sirens:nuclearSiren_top");
	}
}
