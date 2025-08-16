package brokendoop.doopmod.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.Cube;

@Environment(EnvType.CLIENT)
public class ModelCremator extends ModelExoskeleton{

	public Cube nozzle;
	public Cube burner;


	public ModelCremator(float expandAmount) {
		super(expandAmount);
	}

	public void parts(float expandAmount){
		super.parts(expandAmount);
		this.armRight = new Cube(40, 16,64,64);
		this.armRight.addBox(-1.0F, -2.0F, -1.0F, 2, 14, 2, expandAmount);
		this.armRight.setRotationPoint(-5.0F, 2.0F, 0.0F);
		this.nozzle = new Cube(40, 32,64,64);
		this.nozzle.addBox(-1.5F, 5.0F, -1.5F, 3, 7, 3, expandAmount);
		this.nozzle.setRotationPoint(-5.0F, 2.0F, 0.0F);

		this.burner = new Cube(0, 40,64,64);
		this.burner.addBox(0.0F, -1.0F, -1.0F, 0, 16, 8, expandAmount);
		this.burner.setRotationPoint(-5.0F, 2.0F, 0.0F);
	}

	public void setupAnimation(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale) {
		super.setupAnimation(limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale);
		this.nozzle.yRot = this.armRight.yRot;
		this.nozzle.xRot = this.armRight.xRot;
		this.nozzle.zRot = this.armRight.zRot;

		this.burner.yRot = this.armRight.yRot;
		this.burner.xRot = this.armRight.xRot;
		this.burner.zRot = this.armRight.zRot;

		this.nozzle.y = this.armRight.y;
		this.burner.y = this.armRight.y;
	}

	public void render(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale) {
		super.render(limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale);
		this.nozzle.render(scale);
		this.burner.render(scale);

	}
}
