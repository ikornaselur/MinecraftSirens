package is.absalon.sirens;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Sirens.MODID, version = Sirens.VERSION)
public class Sirens
{
    public static final String MODID = "sirens";
    public static final String VERSION = "0.1";
    
    public static Block nuclearSirenOn;
    public static Block nuclearSirenOff;
    
    @Instance(value = MODID)
    public static Sirens instance;
    
    @SidedProxy(clientSide="is.absalon.sirens.client.ClientProxy", serverSide="is.absalon.sirens.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	// Nuclear Warning Siren
    	nuclearSirenOn = new NuclearWarningSiren(true);
    	nuclearSirenOff = new NuclearWarningSiren(false);
    	GameRegistry.registerBlock(nuclearSirenOn, "nuclearSirenOn");
    	GameRegistry.registerBlock(nuclearSirenOff, "nuclearSirenOff");
    	
    	ItemStack jukebox = new ItemStack(Blocks.jukebox);
    	ItemStack ironBar = new ItemStack(Blocks.iron_bars);
    	GameRegistry.addRecipe(new ItemStack(nuclearSirenOff), 
    			"xyx", 
    			"yxy", 
    			"xyx",
    			'x', ironBar,
    			'y', jukebox);
    }
    
    @EventHandler
    public void load(FMLInitializationEvent event) {
    	proxy.registerRenderers();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	// Stub
    }
}
