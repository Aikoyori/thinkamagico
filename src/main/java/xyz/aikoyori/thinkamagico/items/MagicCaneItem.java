package xyz.aikoyori.thinkamagico.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import xyz.aikoyori.thinkamagico.entities.MagicProjectile;
import xyz.aikoyori.thinkamagico.init.ModEntities;
import xyz.aikoyori.thinkamagico.particles.MagicParticle;

public class MagicCaneItem extends Item {

    public MagicCaneItem(Properties properties) {
        super(properties);

        
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.getCooldownTracker().setCooldown(this, 1);
        if (!worldIn.isRemote) {
            MagicProjectile magic = new MagicProjectile(worldIn,playerIn);
            Vector3d spawnVec = playerIn.getLookVec().scale(1.0f);
            Vector3d spawnVecOffset = playerIn.getLookVec().scale(0.2);
            magic.shoot(spawnVec.x,spawnVec.y,spawnVec.z,3.0f,0.0f);
            //magic.moveRelative(0.1f,spawnVec);
            worldIn.addEntity(magic);

            return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
