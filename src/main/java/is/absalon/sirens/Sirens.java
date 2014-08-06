package is.absalon.sirens;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
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
    
    public static Item nuclearSiren;
    
    @Instance(value = MODID)
    public static Sirens instance;
    
    @SidedProxy(clientSide="is.absalon.sirens.client.ClientProxy", serverSide="is.absalon.sirens.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	nuclearSiren = new NuclearWarningSiren();
    	GameRegistry.registerItem(nuclearSiren, "siren");
    }
    
    @EventHandler
    public void load(FMLInitializationEvent event) {
    	proxy.registerRenderers();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	// Stub
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }
}
