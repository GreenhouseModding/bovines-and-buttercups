package house.greenhouse.bovinesandbuttercups.content.attachment;

import com.mojang.serialization.Codec;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.LockdownAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.MooshroomExtrasAttachment;
import house.greenhouse.bovinesandbuttercups.registry.RegistrationCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BovinesAttachments {
    public static final AttachmentType<LockdownAttachment> LOCKDOWN = AttachmentType
            .builder(() -> new LockdownAttachment(new HashMap<>()))
            .serialize(LockdownAttachment.CODEC)
            .build();

    public static final AttachmentType<CowTypeAttachment> COW_TYPE = AttachmentType
            .builder(() -> new CowTypeAttachment(Holder.direct(null), Optional.empty()))
            .serialize(CowTypeAttachment.CODEC)
            .build();

    public static final AttachmentType<MooshroomExtrasAttachment> MOOSHROOM_EXTRAS = AttachmentType
            .builder(MooshroomExtrasAttachment::new)
            .serialize(MooshroomExtrasAttachment.CODEC)
            .build();

    public static final AttachmentType<Boolean> PRODUCES_RICH_HONEY = AttachmentType
            .builder(() -> false)
            .serialize(Codec.BOOL)
            .build();
    public static final AttachmentType<UUID> POLLINATING_MOOBLOOM = AttachmentType
            .builder(() -> (UUID)null)
            .serialize(UUIDUtil.CODEC)
            .build();
    public static final AttachmentType<Map<Holder<CowType<?>>, List<Vec3>>> BABY_PARTICLE_POSITIONS = AttachmentType
            .builder(() -> (Map<Holder<CowType<?>>, List<Vec3>>)new HashMap<Holder<CowType<?>>, List<Vec3>>())
            .build();

    public static void registerAll(RegistrationCallback<AttachmentType<?>> callback) {
        callback.register(NeoForgeRegistries.ATTACHMENT_TYPES, CowTypeAttachment.ID, COW_TYPE);
        callback.register(NeoForgeRegistries.ATTACHMENT_TYPES, LockdownAttachment.ID, LOCKDOWN);
        callback.register(NeoForgeRegistries.ATTACHMENT_TYPES,MooshroomExtrasAttachment.ID,MOOSHROOM_EXTRAS);
        callback.register(NeoForgeRegistries.ATTACHMENT_TYPES, BovinesAndButtercups.asResource("produces_rich_honey"), PRODUCES_RICH_HONEY);
        callback.register(NeoForgeRegistries.ATTACHMENT_TYPES, BovinesAndButtercups.asResource("pollinating_moobloom"), POLLINATING_MOOBLOOM);
    }
}
