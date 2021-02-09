package xyz.aikoyori.thinkamagico.goals;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;
import xyz.aikoyori.thinkamagico.init.ModMobEffects;

import java.util.EnumSet;

public class RunAroundGoal extends Goal{
    private CreatureEntity mob;
    private double speed = 0.0;
    private double targetX;
    private double targetY;
    private double targetZ;

    public RunAroundGoal(CreatureEntity mob,double speedIn) {
        this.mob = mob;
        this.speed = speedIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute() {
        if (!(this.mob.getActivePotionEffect(ModMobEffects.DIZZY.get())==null)) {
            Vector3d vector3d = RandomPositionGenerator.findRandomTarget(this.mob, 3, 2);
            if (vector3d == null) {
                return false;
            } else {
                this.targetX = vector3d.x;
                this.targetY = vector3d.y;
                this.targetZ = vector3d.z;
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.mob.getNavigator().tryMoveToXYZ(this.targetX, this.targetY, this.targetZ, this.speed);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        return !(this.mob.getActivePotionEffect(ModMobEffects.DIZZY.get())==null);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {

        Vector3d vector3d = RandomPositionGenerator.findRandomTarget(this.mob, 4, 4);
        if (vector3d == null) {

        } else {
            this.targetX = vector3d.x;
            this.targetY = vector3d.y;
            this.targetZ = vector3d.z;
        }
        this.mob.getNavigator().tryMoveToXYZ(this.targetX, this.targetY, this.targetZ, this.speed);
        this.mob.setIdleTime(0);

        /*
        if ((this.mob.getActivePotionEffect(ModMobEffects.DIZZY.get())==null)) {

        }*/

    }
}
