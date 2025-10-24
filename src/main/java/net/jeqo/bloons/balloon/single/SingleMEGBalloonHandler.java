package net.jeqo.bloons.balloon.single;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import com.ticxo.modelengine.api.model.bone.BoneBehaviorTypes;
import com.ticxo.modelengine.api.model.bone.SimpleManualAnimator;
import com.ticxo.modelengine.api.model.bone.behavior.BoneBehavior;
import com.ticxo.modelengine.api.model.bone.behavior.BoneBehaviorType;
import org.bukkit.entity.ArmorStand;
import org.joml.Quaternionf;

/**
 * A handler for single Model Engine balloons
 */
public class SingleMEGBalloonHandler {
    private ModeledEntity modeledEntity;
    private ActiveModel activeModel;
    private AnimationHandler animationHandler;
    private SimpleManualAnimator animator;
    private final String defaultIdleAnimationID = "idle";

    /**
     *                   Initializes the MEG balloon handler
     * @param armorStand The armor stand to attach the model to
     * @param megModelID The MEG model ID to use
     * @throws Exception If there is an error initializing the model
     */
    public void initialize(ArmorStand armorStand, String megModelID) throws Exception {
        ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(armorStand);
        ActiveModel activeModel = ModelEngineAPI.createActiveModel(megModelID);
        this.activeModel = activeModel;

        modeledEntity.addModel(activeModel, true);
        this.modeledEntity = modeledEntity;

        this.animationHandler = activeModel.getAnimationHandler();
        this.animationHandler.playAnimation(this.defaultIdleAnimationID, 0.3, 0.3, 1, true);

        // Set up the manual animator for all bones
        SimpleManualAnimator animator = new SimpleManualAnimator();
        activeModel.getBones().values().forEach(bone -> {
            bone.setManualAnimator(animator);
            bone.setHasGlobalRotation(true);
        });
        activeModel.setLockPitch(false);
        this.animator = animator;
    }

    /**
     *              Updates the rotation of the balloon based on pitch and roll
     * @param pitch The pitch
     * @param roll  The roll
     */
    public void updateRotation(double pitch, double roll) {
        if (this.animator != null) {
            Quaternionf rot = new Quaternionf()
                    .rotateX((float) pitch)
                    .rotateZ((float) roll);
            this.animator.getRotation().set(rot);
        }
    }

    /**
     * Updates the animation state of the balloon
     */
    public void updateAnimation() {
        if (this.animationHandler != null) {
            if (this.animationHandler.getAnimation(this.defaultIdleAnimationID) != null) {
                if (!this.animationHandler.isPlayingAnimation(this.defaultIdleAnimationID)) {
                    this.animationHandler.playAnimation(this.defaultIdleAnimationID, 0.3, 0.3, 1, true);
                }
            }
        }
    }

    /**
     * Destroys the MEG balloon handler and cleans up resources
     */
    public void destroy() {
        if (this.modeledEntity != null) {
            this.modeledEntity.destroy();
        }
        if (this.activeModel != null) {
            this.activeModel.destroy();
        }
    }
}