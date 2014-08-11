package is.absalon.sirens;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockNote;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
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
	private IIcon _top;
	private final boolean _isOn;
	private boolean _isPlaying = false;
	private final String _nuclearSoundString = Sirens.MODID + ":siren.nuclear";
	private String UUID;
	private static SoundManager _soundManager;
	private static Map _playingSounds;
	
	private final boolean isDevEnv = false;
	private String _soundHandlerFieldName = isDevEnv ? "mcSoundHandler" : "field_147127_av";
	private String _soundManagerFieldName = isDevEnv ? "sndManager" : "field_147694_f";
	private String _playingSoundsFieldName = isDevEnv ? "playingSounds" : "field_148629_h";
	
	public NuclearWarningSiren(boolean isOn) {
		super(Material.iron);
		this.setHardness(2f);
		this.setStepSound(Block.soundTypeStone);
		this.setBlockName("nuclearWarningSiren");
		this._isOn = isOn;
		
		if (!isOn) {
			this.setCreativeTab(CreativeTabs.tabRedstone);
		}
		else {
			this.setLightLevel(1.0f);			
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void postInit() {
		// Get the minecraft sound manager
		
		Minecraft mc = Minecraft.getMinecraft();
		try {
			Field soundHandlerField = mc.getClass().getDeclaredField(_soundHandlerFieldName);
			soundHandlerField.setAccessible(true);
			SoundHandler soundHandler = (SoundHandler) soundHandlerField.get(mc);
			
			Field soundManagerField = soundHandler.getClass().getDeclaredField(_soundManagerFieldName);
			soundManagerField.setAccessible(true);
			_soundManager = (SoundManager) soundManagerField.get(soundHandler);
			
			Field playingSoundsField = _soundManager.getClass().getDeclaredField(_playingSoundsFieldName);
			playingSoundsField.setAccessible(true);
			_playingSounds = (Map) playingSoundsField.get(_soundManager);
			
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int _) {
		return side == 1 || side == 0 ? this._top : this.blockIcon;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister ir) {
		String sideText = Sirens.MODID + ":nuclearSiren_side_" + (this._isOn ? "on" : "off");
		this.blockIcon = ir.registerIcon(sideText);
		this._top = ir.registerIcon(Sirens.MODID + ":nuclearSiren_top");
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		updateBlock(world, x, y, z);
		if (this._isOn) {
			world.playSoundEffect((double)x, (double)y, (double)z, _nuclearSoundString, 10f, 0.75f);
		}
	}
	
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
    	updateBlock(world, x, y, z);
    }
    
    private void updateBlock(World world, int x, int y, int z) {
		if (world.isRemote) {
			return;
		}
		if (this._isOn && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
			world.scheduleBlockUpdate(x, y, z, this, 4);
		}
		else if (!this._isOn && world.isBlockIndirectlyGettingPowered(x, y, z)) {
			world.setBlock(x, y, z, Sirens.nuclearSirenOn, 0, 2);
		}
    }
    
    public void updateTick(World world, int x, int y, int z, Random random) {
    	if (world.isRemote) {
    		return;
    	}
    	else if (this._isOn && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
			this._isPlaying = false;
			world.setBlock(x, y, z, Sirens.nuclearSirenOff, 0, 2);
    	}
    }
    
    public void breakBlock(World world, int x, int y, int z, Block block, int foo) {
    	if (_isOn) {
    		Iterator iterator = _playingSounds.keySet().iterator();

            while (iterator.hasNext())
            {
                String s = (String)iterator.next();
                PositionedSound sound = (PositionedSound) _playingSounds.get(s);
                
                float soundX = sound.getXPosF();
                float soundY = sound.getYPosF();
                float soundZ = sound.getZPosF();

                if ((int) soundX == x && (int) soundY == y && (int) soundZ == z) {
                	_soundManager.stopSound(sound);
                	break;
                }
            }
    	}
    }
    
    public Item getItemDropped(int i, Random random, int j) {
    	return Item.getItemFromBlock(Sirens.nuclearSirenOff);
    }
    
    public ItemStack createStackedBlock(int i) {
    	return new ItemStack(Sirens.nuclearSirenOff);
    }
}
