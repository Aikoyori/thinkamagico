package xyz.aikoyori.thinkamagico.entities;


import jdk.nashorn.internal.ir.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import static xyz.aikoyori.thinkamagico.init.ModEntities.HALLUTYPE;

public class HallucinationProvider extends Entity {

    private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    private static final DataParameter<Float> HEALTH = EntityDataManager.createKey(Entity.class, DataSerializers.FLOAT);
    private static final DataParameter<CompoundNBT> SUBTAG = EntityDataManager.createKey(Entity.class, DataSerializers.COMPOUND_NBT);
    private static final DataParameter<String> MODELSOURCE = EntityDataManager.createKey(Entity.class, DataSerializers.STRING);
    //private String modelSource = ;
    protected PlayerEntity closestPlayer;
    private AxisAlignedBB boundingBox = ZERO_AABB;


    public HallucinationProvider(EntityType<HallucinationProvider> hallucinationProviderEntityType, World world) {


        super(hallucinationProviderEntityType,world);

    }

    public HallucinationProvider(World worldIn, double x, double y, double z)
    {
        this(HALLUTYPE,worldIn);
        this.noClip=false;
        this.setNoGravity(false);
        this.setPosition(x, y, z);

        registerData();
        this.setBoundingBox(this.getBoundingBox(Pose.STANDING).offset(this.getPositionVec()));

        //this.rotationYaw = (float)(this.rand.nextDouble() * 360.0D);
        //this.setMotion((this.rand.nextDouble() * (double)0.2F - (double)0.1F) * 2.0D, this.rand.nextDouble() * 0.2D * 2.0D, (this.rand.nextDouble() * (double)0.2F - (double)0.1F) * 2.0D);
        //recalculateSize();
    }
    public float getHealth() {
        return this.dataManager.get(HEALTH);
    }
    public void setHealth(float health) {
        this.dataManager.set(HEALTH, MathHelper.clamp(health, 0.0F, this.getMaxHealth()));
    }

    public final float getMaxHealth() {
        return 20.0f;
    }


    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.world.isRemote || this.removed) return false; //Forge: Fixes MC-53850
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            this.markVelocityChanged();
            this.setHealth((int)((float)this.getHealth()- amount));
            if (this.getHealth()<= 0) {
                this.remove();
            }

            return false;
        }
    }

    public String getModelSource() {
       return this.dataManager.get(MODELSOURCE);
    }
    public void setModelSource(String in){
        this.dataManager.set(MODELSOURCE,in);
    }
    public void setSubTag(CompoundNBT taga) {
        this.dataManager.set(SUBTAG, taga);
    }

    public CompoundNBT getSubTag() {
        return this.dataManager.get(SUBTAG)!=null?this.dataManager.get(SUBTAG):new CompoundNBT();
    }

    @Override
    protected float getEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return (float)((this.getBoundingBox(poseIn).getYSize()))* 0.85F;
    }


    private void applyFloatMotion() {
        Vector3d vector3d = this.getMotion();
        this.setMotion(vector3d.x * (double)0.99F, Math.min(vector3d.y + (double)5.0E-4F, (double)0.06F), vector3d.z * (double)0.99F);
    }

    @Override
    public void tick() {
        //this.world.addEntity(new ExperienceOrbEntity(this.world,this.getPosX(),this.getPosY(),this.getPosZ(),1));
        super.tick();

        if (this.areEyesInFluid(FluidTags.WATER)) {
            this.applyFloatMotion();

        }
        this.firstUpdate=true;
        this.prevPosX = this.getPosX();
        this.prevPosY = this.getPosY();
        this.prevPosZ = this.getPosZ();

        if (!this.hasNoGravity()) {
            this.setMotion(this.getMotion().add(0.0D, -0.03D, 0.0D));
        }

        if (this.world.getFluidState(this.getPosition()).isTagged(FluidTags.LAVA)) {
            this.setMotion((double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F), (double)0.2F, (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F));
            this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
        }

        if (!this.world.hasNoCollisions(this.getBoundingBox())) {
            this.pushOutOfBlocks(this.getPosX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getPosZ());
        }

        double d0 = 8.0D;
        if (this.closestPlayer == null || this.closestPlayer.getDistanceSq(this) > 64.0D) {
            this.closestPlayer = this.world.getClosestPlayer(this, 8.0D);


        }



        if (this.closestPlayer != null && this.closestPlayer.isSpectator()) {
            this.closestPlayer = null;
        }

        if (this.closestPlayer != null) {
            Vector3d vector3d = new Vector3d(this.closestPlayer.getPosX() - this.getPosX(), 0, this.closestPlayer.getPosZ() - this.getPosZ());
            Vector3d vector3d2 = new Vector3d(this.closestPlayer.getPosX() - this.getPosX(), this.closestPlayer.getPosY() + (double)this.closestPlayer.getEyeHeight() / 2.0D - this.getPosY(), this.closestPlayer.getPosZ() - this.getPosZ());
            this.rotationYaw =(MathHelper.wrapDegrees(-(float)(MathHelper.atan2(vector3d2.x,vector3d2.z) * (double)(180F / (float)Math.PI))));

            //this.setRotation((float) Math.atan2(vector3d2.x,vector3d2.z),(float) Math.asin(-vector3d2.y));
            //this.setRotation((float) Math.atan2(vector3d2.x,vector3d2.z),(float) Math.asin(-vector3d2.y));

            double d1 = vector3d.lengthSquared();
            if (d1 < 64.0D) {
                double d2 = 1.0D - Math.sqrt(d1) / 8.0D;
                this.setMotion(this.getMotion().add(vector3d.normalize().scale(d2 * d2 * 0.10D)));
            }
        }

        float f = 0.98F;
        if (this.onGround) {
            BlockPos pos =new BlockPos(this.getPosX(), this.getPosY() - 1.0D, this.getPosZ());
            f = this.world.getBlockState(pos).getSlipperiness(this.world, pos, this) * 0.98F;
        }
        this.move(MoverType.SELF, this.getMotion());

        //this.setMotion(this.getMotion().mul((double)f, 0.98D, (double)f));
        if (this.onGround) {
            this.setMotion(this.getMotion().mul(1.0D, -0.9D, 1.0D));
        }

        this.prevRotationYaw=this.rotationYaw;


    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if(MODELSOURCE.equals(key))
        {
            EntityType<?> TYPE = (Registry.ENTITY_TYPE.getOrDefault(new ResourceLocation(this.getModelSource())));
            AxisAlignedBB box = TYPE.getBoundingBoxWithSizeApplied(0,0,0);

            box = box.offset(getPositionVec());
        }
        super.notifyDataManagerChange(key);
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return super.getBoundingBox();
        //return this.getBoundingBox(Pose.STANDING);
    }

    @Override
    protected AxisAlignedBB getBoundingBox(Pose pose) {
        //AxisAlignedBB box = TYPE.getBoundingBoxWithSizeApplied(0,0,0);
        EntitySize entitysize = this.getSize(pose);
        float f = entitysize.width / 2.0F;
        Vector3d vector3d = new Vector3d(this.getPosX() - (double)f, this.getPosY(), this.getPosZ() - (double)f);
        Vector3d vector3d1 = new Vector3d(this.getPosX() + (double)f, this.getPosY() + (double)entitysize.height, this.getPosZ() + (double)f);
        return new AxisAlignedBB(vector3d, vector3d1);

        //return box;//.offset(this.getPositionVec());

        //super.getBoundingBox(pose);
    }

    @Override
    public EntitySize getSize(Pose poseIn) {

        EntityType<?> TYPE = (Registry.ENTITY_TYPE.getOrDefault(new ResourceLocation(this.getModelSource())));
        return TYPE.getSize();
    }

    @Override
    public void baseTick() {

        //this.rotationYaw = 120.0f;
        super.baseTick();
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.removed;
    }
    @Override
    public void applyEntityCollision(Entity entityIn) {
        if (entityIn instanceof HallucinationProvider) {
            if (entityIn.getBoundingBox().minY < this.getBoundingBox().maxY) {
                super.applyEntityCollision(entityIn);
            }
        } else if (entityIn.getBoundingBox().minY <= this.getBoundingBox().minY) {
            super.applyEntityCollision(entityIn);
        }

    }
    @Override
    protected void registerData() {

        this.dataManager.register(HEALTH, 5.0F);
        this.dataManager.register(MODELSOURCE, "minecraft:zombie");
        this.dataManager.register(SUBTAG, new CompoundNBT());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        this.setHealth(compound.getShort("Health"));
        this.setModelSource(compound.getString("ModelSource"));
        this.setSubTag(compound.getCompound("SubTag"));


    }


    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.putShort("Health", (short)this.getHealth());
        compound.putString("ModelSource", this.getModelSource());
        compound.put("SubTag", this.getSubTag());

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
