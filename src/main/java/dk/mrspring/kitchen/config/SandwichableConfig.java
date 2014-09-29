package dk.mrspring.kitchen.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Konrad on 29-09-2014 for TheKitchenMod.
 */
public class SandwichableConfig extends BaseConfig
{
    List<SandwichableEntry> sandwichable_items = new ArrayList<SandwichableEntry>();

    public SandwichableConfig(File location, String name)
    {
        super(location, name);

        this.sandwichable_items.add(new SandwichableEntry("kitchen:bread_slice", 0, true));
        this.sandwichable_items.add(new SandwichableEntry("kitchen:toast", 0, true));

        this.sandwichable_items.add(new SandwichableEntry("kitchen:bacon_cooked", 2));
        this.sandwichable_items.add(new SandwichableEntry("kitchen:bacon_raw", 1));

        this.sandwichable_items.add(new SandwichableEntry("kitchen:tomato_slice", 1));
        this.sandwichable_items.add(new SandwichableEntry("kitchen:lettuce_leaf", 2));
        this.sandwichable_items.add(new SandwichableEntry("kitchen:potato_slice", 2));
        this.sandwichable_items.add(new SandwichableEntry("kitchen:carrot_slice", 2));

        this.sandwichable_items.add(new SandwichableEntry("kitchen:raw_roast_beef", 1));
        this.sandwichable_items.add(new SandwichableEntry("kitchen:roast_beef", 4));

        this.sandwichable_items.add(new SandwichableEntry("kitchen:chicken_fillet_raw", 1));
        this.sandwichable_items.add(new SandwichableEntry("kitchen:chicken_fillet_cooked", 4));

        this.sandwichable_items.add(new SandwichableEntry("kitchen:cheese_slice", 3));
    }

    private class SandwichableEntry
    {
        String item_name = "minecraft:dirt";
        int heal_amount = 0;
        boolean is_bread = false;

        public SandwichableEntry(String name, int healAmount, boolean isBread)
        {
            this.item_name = name;
            this.heal_amount = healAmount;
            this.is_bread = isBread;
        }

        public SandwichableEntry(String name, int healAmount)
        {
            this(name, healAmount, false);
        }
    }
}
