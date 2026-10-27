package fr.switchback.blocklings.entities;

import fr.switchback.blocklings.util.helpers.EntityHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.passive.EntityTameable;

public class BlocklingAIOwnerHurtByTarget extends EntityAITarget {
    EntityTameable tameable;
    EntityLivingBase attacker;
    private int timestamp;
    private EntityBlockling blockling;

    public BlocklingAIOwnerHurtByTarget(EntityTameable theDefendingTameableIn) {
        super(theDefendingTameableIn, false);
        this.blockling = (EntityBlockling) theDefendingTameableIn;
        this.tameable = theDefendingTameableIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (blockling.getGuard() != EntityHelper.Guard.GUARD || blockling.getTask() != EntityHelper.Task.HUNT) return false;
        if (!this.tameable.isTamed()) {
            return false;
        } else {
            EntityLivingBase entitylivingbase = this.tameable.getOwner();
            if (entitylivingbase == null) {
                return false;
            } else {
                this.attacker = entitylivingbase.getRevengeTarget();
                int i = entitylivingbase.getRevengeTimer();
                return i != this.timestamp && this.isSuitableTarget(this.attacker, false) && this.tameable.shouldAttackEntity(this.attacker, entitylivingbase);
            }
        }
    }

    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.attacker);
        EntityLivingBase entitylivingbase = this.tameable.getOwner();
        if (entitylivingbase != null) {
            this.timestamp = entitylivingbase.getRevengeTimer();
        }
        super.startExecuting();
    }
}