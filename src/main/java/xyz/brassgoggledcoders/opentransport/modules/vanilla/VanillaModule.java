package xyz.brassgoggledcoders.opentransport.modules.vanilla;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.brassgoggledcoders.opentransport.OpenTransport;
import xyz.brassgoggledcoders.opentransport.blocks.BlockWrapperBase;
import xyz.brassgoggledcoders.opentransport.modules.vanilla.guiinterfaces.*;
import xyz.brassgoggledcoders.opentransport.modules.vanilla.interactions.JukeBoxAction;
import xyz.brassgoggledcoders.opentransport.renderers.RenderType;

@Module(OpenTransport.MODID)
public class VanillaModule extends ModuleBase {
    @Override
    public String getName() {
        return "Vanilla";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        //Vanilla cart blocks
        new BlockWrapperBase(Blocks.CHEST).setGuiInterface(new ChestGuiInterface()).register();
        new BlockWrapperBase(Blocks.MOB_SPAWNER).register();
        new BlockWrapperBase(Blocks.FURNACE).setGuiInterface(new FurnaceGuiInterface()).register();
        new BlockWrapperBase(Blocks.HOPPER).setGuiInterface(new HopperGuiInterface()).register();
        //TODO: Command Block Wrapper
        //new BlockWrapperBase(Blocks.COMMAND_BLOCK).setGuiInterface(new CommandGuiInterface()).register();
        //TODO:Explosive Blocks
        //new BlockWrapperBase(Blocks.TNT).register();

        //Other Stuff
        new BlockWrapperBase(Blocks.ENDER_CHEST).setGuiInterface(new ChestGuiInterface()).register();
        new BlockWrapperBase(Blocks.JUKEBOX).addActionListener(new JukeBoxAction()).register();
        new BlockWrapperBase(Blocks.CRAFTING_TABLE).setGuiInterface(new CraftingTableGuiInterface()).register();
        new BlockWrapperBase(Blocks.NOTEBLOCK).register();
        new BlockWrapperBase(Blocks.ANVIL).setGuiInterface(new AnvilGuiInterface()).register();
        new BlockWrapperBase(Blocks.CAULDRON).setItemStack(new ItemStack(Items.CAULDRON)).register();
        new BlockWrapperBase(Blocks.CAKE).setItemStack(new ItemStack(Items.CAKE)).register();
        new BlockWrapperBase(Blocks.BREWING_STAND).setGuiInterface(new BrewStandGuiInterface())
                .setItemStack(new ItemStack(Items.BREWING_STAND)).register();
        new BlockWrapperBase(Blocks.ENCHANTING_TABLE).setRenderType(RenderType.COMBO).register();
        //Maybe?
        //new BlockWrapperBase(Blocks.DISPENSER).register();
        //new BlockWrapperBase(Blocks.DROPPER).register();
        //new BlockWrapperBase(Blocks.STANDING_SIGN).register();
        //new BlockWrapperBase(Blocks.STANDING_BANNER).register();
    }
}
