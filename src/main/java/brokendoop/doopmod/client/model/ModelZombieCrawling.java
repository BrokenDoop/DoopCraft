package brokendoop.doopmod.client.model;


import brokendoop.doopmod.core.entity.MobZombieCrawling;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.Cube;
import net.minecraft.client.render.model.ModelZombie;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.util.helper.MathHelper;

@Environment(EnvType.CLIENT)
public class ModelZombieCrawling extends ModelZombie {

	public MobZombieCrawling crawlingZombie;

	public ModelZombieCrawling() {
		this.parts();
	}

	public void parts() {
		this.holdingLeftHand = false;
		this.holdingRightHand = false;
		this.sneaking = false;
		this.head = new Cube(0, 0);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
		this.head.setRotationPoint(0.0F, 22.0F, -2.0F);
		this.body = new Cube(16, 16);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4);
		this.body.setRotationPoint(0.0F, 22.0F, 0.0F);
		this.armRight = new Cube(40, 16);
		this.armRight.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4);
		this.armRight.setRotationPoint(-5.0F, 22.0F, 0.0F);
		this.armLeft = new Cube(40, 16);
		this.armLeft.mirror = true;
		this.armLeft.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4);
		this.armLeft.setRotationPoint(5.0F, 22.0F, 0.0F);
	}
	public void setLivingAnimations(Mob entityLiving, float limbSwing, float limbYaw, float partialTick) {
		this.crawlingZombie = (MobZombieCrawling) entityLiving;

	}

	public void setupAnimation(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale) {
		super.setupAnimation(limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale);
		this.body.xRot = (float) Math.PI / 2;
		this.head.y = 22.0F;
		this.head.z = -3.0F;
		this.armRight.xRot = -(float) Math.PI / 2 + MathHelper.cos(limbSwing * 0.6662F + 3.141593F) * 2.0F * limbYaw * 0.2F;
		this.armLeft.xRot = -(float) Math.PI / 2 + MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbYaw * 0.2F;
		this.body.yRot =  MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbYaw * 0.11F;
		this.armRight.yRot = (float) Math.PI / 20;
		this.armLeft.yRot = -(float) Math.PI / 20;

	}
	public void render(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale) {
		this.setupAnimation(limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale);
		this.head.render(scale);
		this.body.render(scale);
		this.armRight.render(scale);
		this.armLeft.render(scale);
		this.hair.render(scale);
	}

}
