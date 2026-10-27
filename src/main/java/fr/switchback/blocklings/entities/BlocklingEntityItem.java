package fr.switchback.blocklings.entities;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BlocklingEntityItem extends EntityItem {
    public BlocklingEntityItem(World worldIn, double x, double y, double z, ItemStack stack) {
        super(worldIn, x, y, z, stack);
        this.isImmuneToFire = true;
        this.setPickupDelay(40);

    }

    public BlocklingEntityItem(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.isImmuneToFire = true;
        this.setPickupDelay(40);
    }

    public BlocklingEntityItem(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
        this.setPickupDelay(40);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.isFireDamage() || source.isExplosion()) {
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote) {
            this.lifespan = Integer.MAX_VALUE;
        }
    }
}
