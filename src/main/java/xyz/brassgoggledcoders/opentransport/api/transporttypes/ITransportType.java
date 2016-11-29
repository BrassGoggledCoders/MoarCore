package xyz.brassgoggledcoders.opentransport.api.transporttypes;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import xyz.brassgoggledcoders.opentransport.api.wrappers.block.IBlockWrapper;

import javax.annotation.Nonnull;
import java.util.Map;

public interface ITransportType<E extends Entity> {
    @Nonnull
    String getName();

    @Nonnull
    Class<E> getBaseEntity();

    @Nonnull
    CreativeTabs getCreativeTab();

    void registerItems(Map<String, IBlockWrapper> blockWrappers);

    void registerEntities();

    boolean getIsActive();

    void setIsActive(boolean isActive);
}
