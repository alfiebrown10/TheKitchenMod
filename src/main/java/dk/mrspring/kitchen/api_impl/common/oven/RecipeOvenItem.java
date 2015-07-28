package dk.mrspring.kitchen.api_impl.common.oven;

import dk.mrspring.kitchen.KitchenItems;
import dk.mrspring.kitchen.api.oven.IOven;
import dk.mrspring.kitchen.api.oven.IOvenItem;
import dk.mrspring.kitchen.recipe.IRecipe;
import dk.mrspring.kitchen.recipe.OvenRecipe;
import dk.mrspring.kitchen.recipe.OvenRecipes;
import dk.mrspring.kitchen.util.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Konrad on 11-07-2015.
 */
public class RecipeOvenItem implements IOvenItem
{
    public static final String RECIPE_INPUT = "RecipeInput";
    public static final String RECIPE_OUTPUT = "RecipeOutput";
    public static final String RECIPE_BURNT_OUTPUT = "RecipeBurntOutput";

    @Override
    public String getName()
    {
        return "basic_recipe";
    }

    @Override
    public String getDisplayName()
    {
        return "Recipe";
    }

    @Override
    public boolean isForItem(IOven oven, ItemStack item, EntityPlayer player, boolean[] freeSlots)
    {
        return OvenRecipes.instance().hasOutput(item) && itemOverride(item);
    }

    public boolean itemOverride(ItemStack item)
    {
        return true;
    }

    @Override
    public boolean canAdd(IOven oven, ItemStack adding, EntityPlayer player, boolean[] freeSlots)
    {
        return true;
    }

    @Override
    public void onAdded(IOven oven, ItemStack clicked, EntityPlayer player, int[] slots)
    {
//        int slot = slots[0];
        ItemStack input = clicked.copy();
        input.stackSize = 1;
        IRecipe recipe = OvenRecipes.instance().getRecipeFor(input);
        ItemStack output = recipe.getOutput(input);
        ItemStack burntOutput = recipe instanceof OvenRecipe ?
                ((OvenRecipe) recipe).getBurntResult(input) :
                new ItemStack(KitchenItems.burnt_meat);
        NBTTagCompound outputCompound = new NBTTagCompound(),
                inputCompound = new NBTTagCompound(),
                burntCompound = new NBTTagCompound();
        input.writeToNBT(inputCompound);
        output.writeToNBT(outputCompound);
        burntOutput.writeToNBT(burntCompound);
        for (int slot : slots)
        {
            NBTTagCompound slotCompound = oven.getSpecialInfo(slot);
            slotCompound.setTag(RECIPE_INPUT, inputCompound.copy());
            slotCompound.setTag(RECIPE_OUTPUT, outputCompound.copy());
            slotCompound.setTag(RECIPE_BURNT_OUTPUT, burntCompound.copy());
        }
        clicked.stackSize--;
//        System.out.println("On added, input: " + ItemUtils.name(input) + ", clicked: " + ItemUtils.name(clicked));
    }

    @Override
    public boolean readyToCook(IOven oven, int slot)
    {
        return true;
    }

    @Override
    public int getSize(IOven oven)
    {
        return 1;
    }

    @Override
    public boolean consecutive(IOven oven)
    {
        return false;
    }

    @Override
    public int getCookTime(IOven oven)
    {
        return 200;
    }

    @Override
    public int getBurnTime(IOven oven)
    {
        return getCookTime(oven) + 150;
    }

    protected int getMaxOvenStackSize(IOven oven, ItemStack stack, int slot)
    {
        return 4;
    }

    @Override
    public boolean onRightClicked(IOven oven, ItemStack clicked, EntityPlayer player, int slot)
    {
        NBTTagCompound slotCompound = oven.getSpecialInfo(slot);
        NBTTagCompound inputCompound = slotCompound.getCompoundTag(RECIPE_INPUT);
        ItemStack input = ItemStack.loadItemStackFromNBT(inputCompound);
        if (input == null)
        {
            input = clicked.copy();
            input.stackSize = 1;
        }
        if (ItemUtils.areStacksEqual(clicked, input, false) && input.stackSize < getMaxOvenStackSize(oven, clicked, slot))
        {
            input.stackSize++;
            clicked.stackSize--;
            ItemStack output = OvenRecipes.instance().getOutputFor(input);
            if (output != null)
            {
                output.stackSize *= input.stackSize;
                NBTTagCompound outputCompound = new NBTTagCompound();
                output.writeToNBT(outputCompound);
                slotCompound.setTag(RECIPE_OUTPUT, outputCompound);
            }
            NBTTagCompound newInputCompound = new NBTTagCompound();
            input.writeToNBT(newInputCompound);
            slotCompound.setTag(RECIPE_INPUT, newInputCompound);
            return true;
        } else return false;
    }

    @Override
    public boolean canBeRemoved(IOven oven, ItemStack clicked, EntityPlayer player, int slot)
    {
        return true;
    }

    @Override
    public ItemStack[] onRemoved(IOven oven, ItemStack clicked, EntityPlayer player, int slot)
    {
        NBTTagCompound slotCompound = oven.getSpecialInfo(slot);
        boolean done = oven.isFinished(), burnt = oven.isBurnt();
        NBTTagCompound resultCompound = slotCompound.getCompoundTag(done ? (burnt ? RECIPE_BURNT_OUTPUT : RECIPE_OUTPUT) : RECIPE_INPUT);
        ItemStack dropping = ItemStack.loadItemStackFromNBT(resultCompound);
        System.out.println(ItemUtils.name(dropping));
        return new ItemStack[]{dropping};
    }
}
