package dk.mrspring.kitchen.item;

import dk.mrspring.kitchen.KitchenItems;
import dk.mrspring.kitchen.item.food.ItemFoodBase;
import dk.mrspring.kitchen.item.render.ItemRenderMuffin;
import dk.mrspring.kitchen.util.ItemUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;
import java.util.Map;

/**
 * Created by Konrad on 10-08-2015.
 */
public class ItemMuffin extends ItemFoodBase
{
    public static final String MUFFIN_TYPE = "MuffinType";

    public static String getMuffinType(ItemStack stack)
    {
        return ItemUtils.getStringTag(stack, MUFFIN_TYPE);
    }

    public static ItemStack makeMuffinStack(String muffinType, boolean cooked)
    {
        return makeMuffinStack(muffinType, cooked, ItemMuffinCup.WHITE);
    }

    public static ItemStack makeMuffinStack(String muffinType, boolean cooked, int color)
    {
        ItemStack muffin = new ItemStack(cooked ? KitchenItems.cooked_muffin : KitchenItems.uncooked_muffin, 1, color);
        ItemUtils.setStringTag(muffin, MUFFIN_TYPE, muffinType);
        return muffin;
    }

    public ItemMuffin(String name, int healAmount)
    {
        super(name, healAmount, false, true);
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(int damage, int pass)
    {
        if (pass != 2) return KitchenItems.empty_muffin_cup.getIconFromDamageForRenderPass(damage, pass);
        else return super.getIconFromDamageForRenderPass(damage, pass);
    }

    @Override
    public int getRenderPasses(int metadata)
    {
        return 3;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs p_150895_2_, List list)
    {
        for (Map.Entry<String, Integer> entry : ItemRenderMuffin.muffinColors.entrySet())
            list.add(makeMuffinStack(entry.getKey(), item == KitchenItems.cooked_muffin));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_)
    {
        super.addInformation(stack, player, list, p_77624_4_);
        list.add(getMuffinType(stack));
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int pass)
    {
        if (pass != 2) return KitchenItems.empty_muffin_cup.getColorFromItemStack(stack, pass);
        else return ItemRenderMuffin.getColorAsInteger(stack);
    }
}