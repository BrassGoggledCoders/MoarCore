package xyz.brassgoggledcoders.opentransport.boats.items;

import com.teamacronymcoders.base.client.models.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityBoat.Type;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.brassgoggledcoders.opentransport.api.OpenTransportAPI;
import xyz.brassgoggledcoders.opentransport.api.wrappers.block.IBlockWrapper;
import xyz.brassgoggledcoders.opentransport.boats.BoatTransport;
import xyz.brassgoggledcoders.opentransport.boats.entities.EntityBoatHolder;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemBoatHolder extends ItemBoat implements IHasModel {
    boolean creativeTabSet = false;

    public ItemBoatHolder(CreativeTabs tab) {
        super(Type.OAK);
        this.setUnlocalizedName("boat.holder");
        this.setCreativeTab(tab);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack itemStack, World world,
                                                    EntityPlayer entityPlayer, EnumHand hand) {
        float f = 1.0F;
        float f1 = entityPlayer.prevRotationPitch + (entityPlayer.rotationPitch - entityPlayer.prevRotationPitch) * f;
        float f2 = entityPlayer.prevRotationYaw + (entityPlayer.rotationYaw - entityPlayer.prevRotationYaw) * f;
        double d0 = entityPlayer.prevPosX + (entityPlayer.posX - entityPlayer.prevPosX) * (double) f;
        double d1 =
                entityPlayer.prevPosY + (entityPlayer.posY - entityPlayer.prevPosY) * (double) f + (double) entityPlayer
                        .getEyeHeight();
        double d2 = entityPlayer.prevPosZ + (entityPlayer.posZ - entityPlayer.prevPosZ) * (double) f;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        Vec3d vec3d1 = vec3d.addVector((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
        RayTraceResult raytraceresult = world.rayTraceBlocks(vec3d, vec3d1, true);

        if (raytraceresult == null) {
            return new ActionResult<>(EnumActionResult.PASS, itemStack);
        } else {
            Vec3d vec3d2 = entityPlayer.getLook(f);
            boolean flag = false;
            List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(entityPlayer,
                    entityPlayer.getEntityBoundingBox()
                            .addCoord(vec3d2.xCoord * d3, vec3d2.yCoord * d3, vec3d2.zCoord * d3).expandXyz(1.0D));

            for (Entity entity : list) {
                if (entity.canBeCollidedWith()) {
                    AxisAlignedBB axisalignedbb =
                            entity.getEntityBoundingBox().expandXyz((double) entity.getCollisionBorderSize());

                    if (axisalignedbb.isVecInside(vec3d)) {
                        flag = true;
                    }
                }
            }

            if (flag) {
                return new ActionResult<>(EnumActionResult.PASS, itemStack);
            } else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
                return new ActionResult<>(EnumActionResult.PASS, itemStack);
            } else {
                Block block = world.getBlockState(raytraceresult.getBlockPos()).getBlock();
                boolean isWater = block == Blocks.WATER || block == Blocks.FLOWING_WATER;
                EntityBoatHolder entityBoatHolder = new EntityBoatHolder(world);
                double boatPosX = raytraceresult.hitVec.xCoord;
                double boatPosY = isWater ? raytraceresult.hitVec.yCoord - 0.12D : raytraceresult.hitVec.yCoord;
                double boatPosZ = raytraceresult.hitVec.zCoord;
                entityBoatHolder.setPosition(boatPosX, boatPosY, boatPosZ);
                entityBoatHolder.setBoatType(this.getType(itemStack));
                entityBoatHolder.setItemBoat(itemStack);
                entityBoatHolder.setBlockWrapper(this.getBlockWrapper(itemStack).copy());
                entityBoatHolder.rotationYaw = entityPlayer.rotationYaw;

                if (!world.getCollisionBoxes(entityBoatHolder, entityBoatHolder.getEntityBoundingBox().expandXyz(-0.1D))
                        .isEmpty()) {
                    return new ActionResult<>(EnumActionResult.FAIL, itemStack);
                } else {
                    if (!entityPlayer.capabilities.isCreativeMode) {
                        --itemStack.stackSize;
                    }

                    if (!world.isRemote) {
                        world.spawnEntityInWorld(entityBoatHolder);
                        entityBoatHolder.getBlockWrapper().onPlace(entityPlayer, hand, itemStack);
                    }

                    this.increaseStat(entityPlayer);
                    return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
                }
            }
        }
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack itemStack) {
        return this.getUnlocalizedName() + "." + this.getType(itemStack).toString().toLowerCase();
    }

    @Override
    @Nonnull
    public String getItemStackDisplayName(@Nonnull ItemStack boatItemStack) {
        String displayName = "";

        displayName += this.getBoatItem(boatItemStack).getItemStackDisplayName(boatItemStack);

        ItemStack wrapperItemStack = this.getBlockWrapper(boatItemStack).getItemStack();
        displayName += " " + I18n.format("separator.with") + " ";
        displayName += wrapperItemStack.getItem().getItemStackDisplayName(wrapperItemStack);

        return displayName;
    }

    public void increaseStat(EntityPlayer entityPlayer) {
        StatBase stat = StatList.getObjectUseStats(this);
        if (stat != null) {
            entityPlayer.addStat(stat);
        }
    }

    @Override
    @Nonnull
    public Item setCreativeTab(@Nonnull CreativeTabs tab) {
        if (!creativeTabSet) {
            super.setCreativeTab(tab);
            this.creativeTabSet = true;
        }
        return this;
    }

    public IBlockWrapper getBlockWrapper(ItemStack itemStack) {
        return OpenTransportAPI.getBlockWrapperRegistry().getLoadedBlockWrapper(itemStack);
    }

    public Type getType(ItemStack itemStack) {
        return Type.values()[itemStack.getItemDamage()];
    }

    public Item getBoatItem(ItemStack itemStack) {
        Type type = this.getType(itemStack);
        Item itemBoat = Items.BOAT;
        switch (type) {
            case ACACIA:
                itemBoat = Items.ACACIA_BOAT;
                break;
            case BIRCH:
                itemBoat = Items.BIRCH_BOAT;
                break;
            case DARK_OAK:
                itemBoat = Items.DARK_OAK_BOAT;
                break;
            case JUNGLE:
                itemBoat = Items.JUNGLE_BOAT;
                break;
            case SPRUCE:
                itemBoat = Items.SPRUCE_BOAT;
                break;
        }
        return itemBoat;
    }

    @Override
    public List<String> getModelNames(List<String> modelNames) {
        modelNames.add("boat.holder");
        return modelNames;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull Item item, CreativeTabs tab, List<ItemStack> itemStacks) {
        itemStacks.addAll(this.getAllSubItems(new ArrayList<>()));
    }

    @Override
    public List<ItemStack> getAllSubItems(List<ItemStack> itemStacks) {
        OpenTransportAPI.getBlockWrapperRegistry().getAllBlockWrappers().forEach((name, blockWrapper) -> {
            for (Type type : Type.values()) {
                itemStacks.add(getItemStackForBlockWrapper(type, blockWrapper));
            }
        });
        return itemStacks;
    }

    public static ItemStack getItemStackForBlockWrapper(Type type, IBlockWrapper blockWrapper) {
        ItemStack itemStack = new ItemStack(BoatTransport.itemBoatHolder, 1, type.ordinal());
        NBTTagCompound nbtTagCompound = itemStack.getSubCompound("blockWrapper", true);
        nbtTagCompound.setString("name", blockWrapper.getUnlocalizedName());
        return itemStack;
    }
}
