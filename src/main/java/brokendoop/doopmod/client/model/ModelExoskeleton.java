package brokendoop.doopmod.client.model;


import brokendoop.doopmod.core.entity.MobExoskeleton;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.Cube;
import net.minecraft.client.render.model.ModelSkeleton;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.util.helper.MathHelper;

@Environment(EnvType.CLIENT)
public class ModelExoskeleton extends ModelSkeleton {

	public Cube headInterior;
	public Cube bodyInterior;

	public float jitterSin;
	public float jitterCos;

	public ModelExoskeleton(float expandAmount){
		this.parts(expandAmount);

	}

	public void parts(float expandAmount){
		this.head = new Cube(0, 0,64,64);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, expandAmount);
		this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.headInterior = new Cube(0, 32,64,64);
		this.headInterior.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, expandAmount - 0.5F);
		this.headInterior.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.hair = new Cube(32, 0,64,64);
		this.hair.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, expandAmount + 0.5F);
		this.hair.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.body = new Cube(16, 16,64,64);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, expandAmount);
		this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bodyInterior = new Cube(16, 48,64,64);
		this.bodyInterior.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, expandAmount - 0.5F);
		this.bodyInterior.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.armRight = new Cube(40, 16,64,64);
		this.armRight.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, expandAmount);
		this.armRight.setRotationPoint(-5.0F, 2.0F, 0.0F);
		this.armLeft = new Cube(48, 16,64,64);
		this.armLeft.mirror = true;
		this.armLeft.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, expandAmount);
		this.armLeft.setRotationPoint(5.0F, 2.0F, 0.0F);
		this.legRight = new Cube(0, 16,64,64);
		this.legRight.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, expandAmount);
		this.legRight.setRotationPoint(-2.0F, 12.0F, 0.0F);
		this.legLeft = new Cube(8, 16,64,64);
		this.legLeft.mirror = true;
		this.legLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, expandAmount);
		this.legLeft.setRotationPoint(2.0F, 12.0F, 0.0F);
	}

	public void setupAnimation(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale) {
		super.setupAnimation( limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale);
		this.headInterior.yRot = this.head.yRot;
		this.headInterior.xRot = this.head.xRot;
		this.bodyInterior.yRot = this.body.yRot;

		this.armRight.yRot = this.head.yRot;
		this.armRight.xRot = (-(float)Math.PI / 2F) + this.head.xRot;

		this.armLeft.zRot = (-(float)Math.PI / 8F) - (MathHelper.cos(limbPitch * 0.09F) * 0.05F + 0.05F);
		this.armLeft.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbYaw * 0.3F;

		this.bodyInterior.y = 0 + this.jitterSin;
		this.headInterior.y = 0 + this.jitterSin;

		this.body.y = 0 + this.jitterSin;
		this.head.y = 0 + this.jitterSin;
		this.hair.y = 0 + this.jitterSin;

		this.armLeft.y = 2 + this.jitterCos;
		this.armRight.y = 2 + this.jitterCos;

	}

	public void setLivingAnimations(Mob mob, float limbSwing, float limbYaw, float partialTick) {
		MobExoskeleton entityExoskeleton = (MobExoskeleton) mob;
		float ageInTicks = entityExoskeleton.tickCount + partialTick;
		this.jitterSin = MathHelper.sin(ageInTicks * 2F) * 0.3F;
		this.jitterCos = MathHelper.cos(ageInTicks * 2F) * 0.3F;
	}

	public void render(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale) {
		this.setupAnimation(limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale);
		this.headInterior.render(scale);
		this.head.render(scale);
		this.bodyInterior.render(scale);
		this.body.render(scale);
		this.armRight.render(scale);
		this.armLeft.render(scale);
		this.legRight.render(scale);
		this.legLeft.render(scale);
		this.hair.render(scale);
	}
}
