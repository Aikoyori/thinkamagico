package xyz.aikoyori.thinkamagico.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import xyz.aikoyori.thinkamagico.MainClass;

import java.util.function.Supplier;

public class ModCreativeTab extends ItemGroup {
    public static final ItemGroup MOD_ITEM_GROUP = new ModCreativeTab(MainClass.MODID, () -> new ItemStack(ModItems.SANDBLUE.get()));
    private final Supplier<ItemStack> iconSupplier;

    public ModCreativeTab(final String name, final Supplier<ItemStack> iconSupplier) {
        super(name);
        this.iconSupplier = iconSupplier;
    }

    @Override
    public ItemStack createIcon() {
        return iconSupplier.get();
    }

}

