/*
 */

package exterminatorjeff.undergroundbiomes.intermod;

import exterminatorjeff.undergroundbiomes.api.ModInfo;
import exterminatorjeff.undergroundbiomes.api.common.UBLogger;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import ic2.core.IC2;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

/**
 *
 * @author Zeno410
 */
public class IC2Registrar {
    
    private static final UBLogger LOGGER = new UBLogger(OresRegistry.class, Level.INFO);
    
    public static String copperOreName = "oreCopper";
    public static String leadOreName = "oreLead";
    public static String tinOreName = "oreTin";
    public static String uraniumOreName = "oreUranium";
    
    public static String ic2OreName ="ic2:resource";
    
    public static int copperMeta = 1;
    public static int leadMeta = 2;
    public static int tinMeta = 3;
    public static int uraniumMeta = 4;
    
    public void register() {
        //if (IC2.MODID == null) treturn; that doesn't work?
        Block block = Block.getBlockFromName(ic2OreName);
        if (block == null) return;
        OresRegistry.INSTANCE.requestOreSetup(block, copperMeta);
        OresRegistry.INSTANCE.requestOreSetup(block, leadMeta);
        OresRegistry.INSTANCE.requestOreSetup(block, tinMeta);
        OresRegistry.INSTANCE.requestOreSetup(block, uraniumMeta);
        OresRegistry.INSTANCE.registerOreOverlay(block, copperMeta, new ResourceLocation(ModInfo.MODID + ":blocks/ic2copper_overlay"));
        OresRegistry.INSTANCE.registerOreOverlay(block, leadMeta, new ResourceLocation(ModInfo.MODID + ":blocks/ic2lead_overlay"));
        OresRegistry.INSTANCE.registerOreOverlay(block, tinMeta, new ResourceLocation(ModInfo.MODID + ":blocks/ic2tin_overlay"));
        OresRegistry.INSTANCE.registerOreOverlay(block, uraniumMeta, new ResourceLocation(ModInfo.MODID + ":blocks/ic2uranium_overlay"));
    }
}
