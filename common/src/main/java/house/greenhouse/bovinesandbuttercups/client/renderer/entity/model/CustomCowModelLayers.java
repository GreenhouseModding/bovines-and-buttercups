package house.greenhouse.bovinesandbuttercups.client.renderer.entity.model;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class CustomCowModelLayers {
    // TODO: 1.21.5 Warm Cow
    public static LayerDefinition createWarm() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F)
                .texOffs(27, 0).addBox(-8.0F, -3.0F, -5.0F, 4.0F, 2.0F, 2.0F)
                .texOffs(39, 0).addBox(-8.0F, -5.0F, -5.0F, 2.0F, 2.0F, 2.0F)
                .texOffs(27, 0).mirror().addBox(4.0F, -3.0F, -5.0F, 4.0F, 2.0F, 2.0F).mirror(false)
                .texOffs(39, 0).mirror().addBox(6.0F, -5.0F, -5.0F, 2.0F, 2.0F, 2.0F).mirror(false), PartPose.offset(0.0F, 4.0F, -8.0F));
        partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F)
                .texOffs(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        CubeListBuilder cubeListBuilder = CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F);
        partDefinition.addOrReplaceChild("right_hind_leg", cubeListBuilder, PartPose.offset(-4.0F, 12.0F, 7.0F));
        partDefinition.addOrReplaceChild("left_hind_leg", cubeListBuilder, PartPose.offset(4.0F, 12.0F, 7.0F));
        partDefinition.addOrReplaceChild("right_front_leg", cubeListBuilder, PartPose.offset(-4.0F, 12.0F, -6.0F));
        partDefinition.addOrReplaceChild("left_front_leg", cubeListBuilder, PartPose.offset(4.0F, 12.0F, -6.0F));
        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    public static LayerDefinition createCold() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition head = partDefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F), PartPose.offset(0.0F, 4.0F, -8.0F));
        head.addOrReplaceChild("right_horn", CubeListBuilder.create().texOffs(0, 32).addBox(-1.5F, -4.5F, -0.5F, 2.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(-4.5F, -2.5F, -3.5F, 1.5708F, 0.0F, 0.0F));
        head.addOrReplaceChild("left_horn", CubeListBuilder.create().texOffs(0, 32).addBox(-1.5F, -3.0F, -0.5F, 2.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(5.5F, -2.5F, -5.0F, 1.5708F, 0.0F, 0.0F));
        partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(20, 32).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F, new CubeDeformation(0.5F)).texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F)
                .texOffs(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        CubeListBuilder cubeListBuilder = CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F);
        partDefinition.addOrReplaceChild("right_hind_leg", cubeListBuilder, PartPose.offset(-4.0F, 12.0F, 7.0F));
        partDefinition.addOrReplaceChild("left_hind_leg", cubeListBuilder, PartPose.offset(4.0F, 12.0F, 7.0F));
        partDefinition.addOrReplaceChild("right_front_leg", cubeListBuilder, PartPose.offset(-4.0F, 12.0F, -6.0F));
        partDefinition.addOrReplaceChild("left_front_leg", cubeListBuilder, PartPose.offset(4.0F, 12.0F, -6.0F));
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    // TODO: Limelight/Zebu
    public static LayerDefinition createLush() {
        return CowModel.createBodyLayer();
    }

    public static LayerDefinition createSculk() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild(
                "head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F)
                        .texOffs(52, 7).addBox(4.0F, -5.0F, -3.5F, 5.0F, 5.0F, 0.01F, true) // Right horn.
                        .texOffs(52, 7).addBox(-9.0F, -5.0F, -3.5F, 5.0F, 5.0F, 0.01F), PartPose.offset(0.0F, 4.0F, -8.0F)); // Left horn
        partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F)
                .texOffs(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        CubeListBuilder cubeListBuilder = CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F);
        partDefinition.addOrReplaceChild("right_hind_leg", cubeListBuilder, PartPose.offset(-4.0F, 12.0F, 7.0F));
        partDefinition.addOrReplaceChild("left_hind_leg", cubeListBuilder, PartPose.offset(4.0F, 12.0F, 7.0F));
        partDefinition.addOrReplaceChild("right_front_leg", cubeListBuilder, PartPose.offset(-4.0F, 12.0F, -6.0F));
        partDefinition.addOrReplaceChild("left_front_leg", cubeListBuilder, PartPose.offset(4.0F, 12.0F, -6.0F));
        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Deprecated
    public static LayerDefinition createBuffalo() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F)
                .texOffs(22, 0).addBox("right_horn_base", 4.0F, -4.0F, -6.0F, 2.0F, 2.0F, 2.0F)
                .texOffs(30, 0).addBox("right_horn", -7.0F, -4.0F, -6.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(22, 0).addBox("left_horn_base", -6.0F, -4.0F, -6.0F, 2.0F, 2.0F, 2.0F)
                .texOffs(30, 0).addBox("left_horn", 6.0F, -4.0F, -6.0F, 1.0F, 1.0F, 2.0F), PartPose.offset(0.0F, 4.0F, -8.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F)
                .texOffs(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(-4.0F, 12.0F, 7.0F));
        partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(4.0F, 12.0F, 7.0F));
        partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(-4.0F, 12.0F, -6.0F));
        partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(4.0F, 12.0F, -6.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Deprecated
    public static LayerDefinition createHighland() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F)
                .texOffs(22, 2).addBox("right_horn", 4.0F, -3.0F, -4.0F, 2.0F, 1.0F, 1.0F)
                .texOffs(28, 1).addBox("right_horn_top", 5.0F, -5.0F, -4.0F, 1.0F, 2.0F, 1.0F)
                .texOffs(23, 2).addBox("left_horn", -6.0F, -3.0F, -4.0F, 2.0F, 1.0F, 1.0F)
                .texOffs(28, 1).addBox("left_horn_top", -6.0F, -5.0F, -4.0F, 1.0F, 2.0F, 1.0F), PartPose.offset(0.0F, 4.0F, -8.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F)
                .texOffs(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(-4.0F, 12.0F, 7.0F));
        partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(4.0F, 12.0F, 7.0F));
        partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(-4.0F, 12.0F, -6.0F));
        partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(4.0F, 12.0F, -6.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Deprecated
    public static LayerDefinition createOx() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F)
                .texOffs(22, 0).addBox("right_horn_base", -6.0F, -3.0F, -6.0F, 2.0F, 2.0F, 2.0F)
                .texOffs(52, 7).addBox("right_horn", -7.0F, -5.0F, -6.0F, 1.0F, 4.0F, 2.0F)
                .texOffs(22, 0).addBox("left_horn_base", 4.0F, -3.0F, -6.0F, 2.0F, 2.0F, 2.0F)
                .texOffs(52, 7).addBox("left_horn", 6.0F, -5.0F, -6.0F, 1.0F, 4.0F, 2.0F), PartPose.offset(0.0F, 4.0F, -8.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F)
                .texOffs(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(-4.0F, 12.0F, 7.0F));
        partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(4.0F, 12.0F, 7.0F));
        partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(-4.0F, 12.0F, -6.0F));
        partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(4.0F, 12.0F, -6.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }
}
