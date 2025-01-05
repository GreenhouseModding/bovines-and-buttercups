package house.greenhouse.bovinesandbuttercups.client.renderer.entity.model;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class CustomCowModelLayers {
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

    public static LayerDefinition createBuffalo() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F)
                .texOffs(22, 0).addBox("right_horn_base", 4.0F, -4.0F, -6.0F, 2.0F, 2.0F, 2.0F)
                .texOffs(30, 0).addBox("right_horn", -7.0F, -5.0F, -6.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(22, 0).addBox("left_horn_base", -6.0F, -4.0F, -6.0F, 2.0F, 2.0F, 2.0F)
                .texOffs(30, 0).addBox("left_horn", 6.0F, -5.0F, -6.0F, 1.0F, 1.0F, 2.0F), PartPose.offset(0.0F, 4.0F, -8.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F)
                .texOffs(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(-4.0F, 12.0F, 7.0F));
        partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(4.0F, 12.0F, 7.0F));
        partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(-4.0F, 12.0F, -6.0F));
        partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(4.0F, 12.0F, -6.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }
    
    public static LayerDefinition createFlat() {
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
