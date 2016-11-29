package xyz.brassgoggledcoders.opentransport.modules.vanilla.interactions;

import com.teamacronymcoders.base.util.ItemStackUtils;
import net.minecraft.block.BlockJukebox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.opentransport.api.wrappers.block.actions.BlockActivationAction;
import xyz.brassgoggledcoders.opentransport.api.wrappers.block.actions.ActionType;
import xyz.brassgoggledcoders.opentransport.api.wrappers.block.IBlockWrapper;
import xyz.brassgoggledcoders.opentransport.api.entities.IHolderEntity;

public class JukeBoxAction extends BlockActivationAction {
    @Override
    public boolean actionOccurred(ActionType actionType, EntityPlayer entityPlayer, EnumHand hand, ItemStack itemStack,
                                  IHolderEntity holderEntity, IBlockWrapper blockWrapper) {
        if (actionType == ActionType.INTERACTION) {
            World realWorld = entityPlayer.getEntityWorld();
            boolean didSomething = false;
            if (!realWorld.isRemote && ItemStackUtils.isItemInstanceOf(itemStack, ItemRecord.class)) {
                ItemRecord itemRecord = (ItemRecord) itemStack.getItem();
                if (blockWrapper.getBlock() == Blocks.JUKEBOX && !blockWrapper.getBlockState().getValue(BlockJukebox.HAS_RECORD)) {
                    BlockJukebox blockJukebox = (BlockJukebox) blockWrapper.getBlock();
                    ((BlockJukebox) Blocks.JUKEBOX).insertRecord(blockWrapper.getWorldWrapper(), BlockPos.ORIGIN, blockWrapper.getBlockState(), itemStack);
                    blockWrapper.getWorldWrapper().playEvent(null, 1010, BlockPos.ORIGIN, Item.getIdFromItem(itemRecord));
                    --itemStack.stackSize;
                    entityPlayer.addStat(StatList.RECORD_PLAYED);
                    didSomething = true;
                }
            }
            return didSomething || super.actionOccurred(actionType, entityPlayer, hand, itemStack, holderEntity, blockWrapper);
        }
        return false;
    }
}
