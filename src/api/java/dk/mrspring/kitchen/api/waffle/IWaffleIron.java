package dk.mrspring.kitchen.api.waffle;

import dk.mrspring.kitchen.api.ISpecialTagInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by Konrad on 11-06-2015.
 */
public interface IWaffleIron extends ISpecialTagInfo
{
    boolean rightClicked(ItemStack clicked, EntityPlayer player);

    float getLidAngle();

    void setLidAngle(float angle);

    int getCookTime();

    boolean setDough(IDough dough);

    IDough overrideDough(IDough dough);

    State getState();

    enum State
    {
        RAW,
        COOKED,
        BURNED
    }
}
