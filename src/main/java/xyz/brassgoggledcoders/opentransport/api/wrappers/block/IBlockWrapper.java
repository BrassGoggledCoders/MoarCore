package xyz.brassgoggledcoders.opentransport.api.wrappers.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import xyz.brassgoggledcoders.opentransport.api.wrappers.block.actions.IActionListener;
import xyz.brassgoggledcoders.opentransport.api.wrappers.block.guiinterfaces.IGuiInterface;
import xyz.brassgoggledcoders.opentransport.api.entities.IHolderEntity;
import xyz.brassgoggledcoders.opentransport.api.wrappers.block.rendering.RenderType;
import xyz.brassgoggledcoders.opentransport.api.wrappers.world.IWorldHarness;
import xyz.brassgoggledcoders.opentransport.api.wrappers.world.WorldWrapper;

import javax.annotation.Nonnull;
import java.util.List;

public interface IBlockWrapper {
    @Nonnull
    Block getBlock();

    @Nonnull
    IBlockState getBlockState();

    /*
     * This method is called from World Wrapper to change the blockstate in an entity.
     * It's not recommended to use this to set blockstate for Registration.
     *
     * @param newBlockState The new Block State for the block to use
     *
     * @return if the blockstate has changed
     */
    void alterBlockState(IBlockState newBlockState);

    @Nonnull
    String getUnlocalizedName();

    @Nonnull
    RenderType getRenderType();

    @Nonnull
    List<IActionListener> getActionListeners();

    IGuiInterface getInterface();

    boolean onPlace(EntityPlayer entityPlayer, EnumHand hand, ItemStack itemStack);

    boolean onInteract(EntityPlayer entityPlayer, EnumHand hand, ItemStack itemStack);

    void tick();

    void markDirty();

    void setHolder(IHolderEntity entity);

    void setWorldHarness(IWorldHarness worldHarness);

    boolean hasTileEntity();

    TileEntity getTileEntity();

    WorldWrapper getWorldWrapper();

    ItemStack getItemStack();

    NBTTagCompound writeToNBT(NBTTagCompound tagCompound);

    void readFromNBT(NBTTagCompound tagCompound);

    boolean hasCapability(Capability<?> capability, EnumFacing facing);

    <T> T getCapability(Capability<T> capability, EnumFacing facing);

    IBlockWrapper copy();
}
