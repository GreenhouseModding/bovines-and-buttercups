package house.greenhouse.bovinesandbuttercups.content.attachment;

import com.mojang.serialization.Codec;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowExtrasAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.LockdownAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.MooshroomExtrasAttachment;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BovinesAttachments {
    public static final AttachmentType<LockdownAttachment> LOCKDOWN = AttachmentRegistry.createPersistent(LockdownAttachment.ID, LockdownAttachment.CODEC);
    public static final AttachmentType<CowVariantAttachment> COW_VARIANT = AttachmentRegistry.createPersistent(CowVariantAttachment.ID, CowVariantAttachment.CODEC);

    public static final AttachmentType<CowExtrasAttachment> COW_EXTRAS = AttachmentRegistry.createPersistent(CowExtrasAttachment.ID, CowExtrasAttachment.CODEC);
    public static final AttachmentType<MooshroomExtrasAttachment> MOOSHROOM_EXTRAS = AttachmentRegistry.createPersistent(MooshroomExtrasAttachment.ID, MooshroomExtrasAttachment.CODEC);

    public static final AttachmentType<Boolean> PRODUCES_RICH_HONEY = AttachmentRegistry.createPersistent(BovinesAndButtercups.asResource("produces_rich_honey"), Codec.BOOL);
    public static final AttachmentType<UUID> POLLINATING_MOOBLOOM = AttachmentRegistry.createPersistent(BovinesAndButtercups.asResource("pollinating_moobloom"), UUIDUtil.CODEC);
    public static final AttachmentType<Map<Holder<CowVariant<?>>, List<Vec3>>> BABY_PARTICLE_POSITIONS = AttachmentRegistry.createDefaulted(BovinesAndButtercups.asResource("baby_particle_positions"), HashMap::new);

    public static void init() {}
}
