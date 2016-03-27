package dk.mrspring.kitchen.common.block;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dk.mrspring.kitchen.Kitchen;
import dk.mrspring.kitchen.ModInfo;
import dk.mrspring.kitchen.client.block.effect.IRandomTick;
import dk.mrspring.kitchen.common.tileentity.TileEntityInteractable;
import dk.mrspring.kitchen.common.util.WorldUtils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created on 07-03-2016 for TheKitchenMod.
 */
public class BlockContainerBase extends BlockContainer
{
    Class<? extends TileEntity> tileEntityClass;
    public int rotationAngles = 0;

    @SideOnly(Side.CLIENT)
    List<IRandomTick> randomTicks;

    protected BlockContainerBase(Material material, String name, String textureName, boolean useCreativeTab,
                                 Class<? extends TileEntity> tileEntityClass)
    {
        super(material);

        this.setBlockName(name);
        this.setBlockTextureName(textureName);

        this.setHardness(4.0F);

        this.tileEntityClass = tileEntityClass;

        if (useCreativeTab)
            this.setCreativeTab(Kitchen.mainTab);
    }

    protected BlockContainerBase(Material material, String name, boolean useCreativeTab,
                                 Class<? extends TileEntity> tileEntityClass)
    {
        this(material, name, ModInfo.toResource(name), useCreativeTab, tileEntityClass);
    }

    protected BlockContainerBase(Material material, String name, Class<? extends TileEntity> tileEntityClass)
    {
        this(material, name, true, tileEntityClass);
    }

    protected BlockContainerBase(String name, String textureName, boolean useCreativeTab,
                                 Class<? extends TileEntity> tileEntityClass)
    {
        this(Material.iron, name, textureName, useCreativeTab, tileEntityClass);
    }

    protected BlockContainerBase(String name, boolean useCreativeTab, Class<? extends TileEntity> tileEntityClass)
    {
        this(name, ModInfo.toResource(name), useCreativeTab, tileEntityClass);
    }

    protected BlockContainerBase(Material material, String name, String textureName,
                                 Class<? extends TileEntity> tileEntityClass)
    {
        this(material, name, textureName, true, tileEntityClass);
    }

    protected BlockContainerBase(String name, String textureName, Class<? extends TileEntity> tileEntityClass)
    {
        this(name, textureName, true, tileEntityClass);
    }

    protected BlockContainerBase(String name, Class<? extends TileEntity> tileEntityClass)
    {
        this(name, true, tileEntityClass);
    }

    @SideOnly(Side.CLIENT)
    public void addRandomTickEffect(IRandomTick effect)
    {
        if (randomTicks == null) randomTicks = Lists.newArrayList();
        randomTicks.add(effect);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        super.randomDisplayTick(world, x, y, z, random);

        if (randomTicks != null)
            for (IRandomTick tick : randomTicks)
                tick.onTick(world, x, y, z, random);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side,
                                    float clickX, float clickY, float clickZ)
    {
        TileEntity entity = world.getTileEntity(x, y, z);
        if (entity instanceof TileEntityInteractable)
            ((TileEntityInteractable) entity).activated(player, side, clickX, clickY, clickZ);

        return onRightClicked(world, x, y, z, player, side, clickX, clickY, clickZ);
    }

    public boolean onRightClicked(World world, int x, int y, int z, EntityPlayer clicker, int side,
                                  float clickX, float clickY, float clickZ)
    {
        return false;
    }

    /**
     * Gets called through the {@link net.minecraftforge.event.world.BlockEvent.BreakEvent} in order to get the player
     * that broke the block, as it is not passed through any vanilla methods called before the {@link TileEntity} has
     * been removed.
     *
     * @param player The {@link EntityPlayer} that broke the block.
     */
    public void onBlockBrokenBy(World world, int x, int y, int z, EntityPlayer player, int metadata)
    {
        TileEntity entity = world.getTileEntity(x, y, z);
        if (entity instanceof TileEntityInteractable)
            ((TileEntityInteractable) entity).broken(player);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        try
        {
            return getTileEntityClass().newInstance();
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack placed)
    {
        if (rotationAngles > 0)
        {
            int direction = WorldUtils.getAngle(player.rotationYaw, rotationAngles);
            setRotationWhenPlaced(world, x, y, z, player, placed, direction);
        }
        super.onBlockPlacedBy(world, x, y, z, player, placed);
        TileEntity entity = world.getTileEntity(x, y, z);
        if (entity instanceof TileEntityInteractable)
            ((TileEntityInteractable) entity).placed((EntityPlayer) player);
    }

    public void setRotationWhenPlaced(World world, int x, int y, int z, EntityLivingBase player, ItemStack placed,
                                      int angle)
    {
        world.setBlockMetadataWithNotify(x, y, z, angle, 2);
    }

    public Class<? extends TileEntity> getTileEntityClass()
    {
        return tileEntityClass;
    }
}