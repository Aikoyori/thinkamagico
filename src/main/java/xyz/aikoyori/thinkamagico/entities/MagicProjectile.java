package xyz.aikoyori.thinkamagico.entities;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.aikoyori.thinkamagico.init.ModAudio;
import xyz.aikoyori.thinkamagico.init.ModEntities;
import xyz.aikoyori.thinkamagico.init.ModMobEffects;
import xyz.aikoyori.thinkamagico.init.ModParticles;

import java.util.List;
import java.util.Random;

public class MagicProjectile extends DamagingProjectileEntity {
    int age = 0;

    boolean actuallyCollidedWithSomethingThatIsNotPlayer=false;
    protected MagicProjectile(EntityType<? extends MagicProjectile> type, double x, double y, double z, World worldIn) {
        this(type, worldIn);
        this.setPosition(x, y, z);
    }
    public MagicProjectile(World worldIn, LivingEntity throwerIn) {
        this(ModEntities.MAGICPROJTYPE,throwerIn.getPosX(),throwerIn.getPosY()+throwerIn.getEyeHeight(throwerIn.getPose()),throwerIn.getPosZ(), worldIn);
        this.setShooter(throwerIn);
    }

    public MagicProjectile(EntityType<? extends MagicProjectile> magicProjectileEntityType, World world) {
        super(ModEntities.MAGICPROJTYPE,world);
    }

    @Override
    protected void registerData() {

    }

    @Override
    public void tick() {
        Entity entity = this.func_234616_v_();
        if (this.world.isRemote || (entity == null || !entity.removed) && this.world.isBlockLoaded(this.getPosition())) {
            if(this.age>600)
            {
                this.remove();
            }
            this.age++;
        }

        RayTraceResult raytraceresult = ProjectileHelper.func_234618_a_(this, this::func_230298_a_);
        if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            this.onImpact(raytraceresult);
        }
        this.doBlockCollisions();
        Vector3d vector3d = this.getMotion();
        double d0 = this.getPosX() + vector3d.x;
        double d1 = this.getPosY() + vector3d.y;
        double d2 = this.getPosZ() + vector3d.z;
        this.world.addParticle(this.getParticle(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
        this.setPosition(d0, d1, d2);
        //this.getEntityWorld().addParticle(ModParticles.MAGIC_SIGNAL.get(),this.getPosX(),this.getPosY(),this.getPosZ(),0,0,0);
    }
    @Override
    protected void onImpact(RayTraceResult result) {
        //super.onImpact(result);
        Entity entity = this.func_234616_v_();
        Vector3d hitvec = result.getHitVec();
        if(entity!=null)
        {
            if (result.getType() != RayTraceResult.Type.ENTITY)
            {
                if(result.getType() == RayTraceResult.Type.BLOCK)
                {
                    BlockRayTraceResult blockresult = (BlockRayTraceResult)result;
                    entity.world.setBlockState(blockresult.getPos(), Blocks.BLACKSTONE.getDefaultState());

                }
                actuallyCollidedWithSomethingThatIsNotPlayer=true;
            }else if(!((EntityRayTraceResult)result).getEntity().isEntityEqual(entity)) {
            if (!this.world.isRemote) {
                EntityRayTraceResult enthit = (EntityRayTraceResult)result;
                Vector3d pos = enthit.getEntity().getPositionVec();
                Vector3d pos2 = this.func_234616_v_().getPositionVec();
                enthit.getEntity().setPosition(pos2.x,pos2.y,pos2.z);
                enthit.getEntity().attackEntityFrom(DamageSource.GENERIC,0.00001f);
                this.func_234616_v_().moveForced(pos);
                if(enthit.getEntity() instanceof LivingEntity)
                {
                    LivingEntity live = (LivingEntity) enthit.getEntity();
                    live.addPotionEffect(new EffectInstance(ModMobEffects.IDENTITYCRISIS.get(),60,1));
                }
                actuallyCollidedWithSomethingThatIsNotPlayer=true;
            }
            }
            else
            {
                actuallyCollidedWithSomethingThatIsNotPlayer=false;
            }
        }


        if (true) {
            Random rand = new Random();
            double velocity = 3.0;

            world.playSound(hitvec.x,hitvec.y,hitvec.z, ModAudio.POOF.get(), SoundCategory.NEUTRAL,10.0f,1.0f,false);
            for (int i=0;i<40;i++)
            {
                this.getEntityWorld().addParticle(ModParticles.MAGIC.get(),this.getPosX(),this.getPosY(),this.getPosZ(),rand.nextDouble()*velocity-velocity/2.0,rand.nextDouble()*velocity-velocity/2.0,rand.nextDouble()*velocity-velocity/2.0);
            }
            this.getEntityWorld().addParticle(ParticleTypes.FLASH,this.getPosX(),this.getPosY(),this.getPosZ(),rand.nextDouble()*velocity-velocity/2.0,rand.nextDouble()*velocity-velocity/2.0,rand.nextDouble()*velocity-velocity/2.0);

            this.setDead();
        }
        else
        {
            actuallyCollidedWithSomethingThatIsNotPlayer=false;
        }


    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBeAttackedWithItem() {
        return false;
    }

    protected boolean isFireballFiery() {
        return false;
    }
    protected IParticleData getParticle() {
        return ModParticles.MAGIC_SIGNAL.get();
    }
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return true;
    }
}
