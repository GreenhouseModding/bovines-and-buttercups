package house.greenhouse.bovinesandbuttercups.content.attachment;

import com.mojang.serialization.Codec;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.LockdownAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.MooshroomExtrasAttachment;
import house.greenhouse.bovinesandbuttercups.api.variant.model.CowModelType;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BovinesAttachments {
    public static final AttachmentType<LockdownAttachment> LOCKDOWN = register(LockdownAttachment.ID, AttachmentType
            .builder(() -> new LockdownAttachment(new HashMap<>()))
            .serialize(LockdownAttachment.CODEC)
            .build());

    public static final AttachmentType<CowVariantAttachment> COW_VARIANT = register(CowVariantAttachment.ID, AttachmentType
            .builder(() -> new CowVariantAttachment(Holder.direct(null), Optional.empty()))
            .serialize(CowVariantAttachment.CODEC)
            .build());

    public static final AttachmentType<MooshroomExtrasAttachment> MOOSHROOM_EXTRAS = register(MooshroomExtrasAttachment.ID, AttachmentType
            .builder(MooshroomExtrasAttachment::new)
            .serialize(MooshroomExtrasAttachment.CODEC)
            .build());

    public static final AttachmentType<Boolean> PRODUCES_RICH_HONEY = register(BovinesAndButtercups.asResource("produces_rich_honey"), AttachmentType
            .builder(() -> false)
            .serialize(Codec.BOOL)
            .build());
    public static final AttachmentType<UUID> POLLINATING_MOOBLOOM = register(BovinesAndButtercups.asResource("pollinating_moobloom"), AttachmentType
            .builder(() -> (UUID)null)
            .serialize(UUIDUtil.CODEC)
            .build());
    public static final AttachmentType<Map<Holder<CowVariant<?>>, List<Vec3>>> BABY_PARTICLE_POSITIONS = register(BovinesAndButtercups.asResource("baby_particle_positions"), AttachmentType
            .builder(() -> (Map<Holder<CowVariant<?>>, List<Vec3>>)new HashMap<Holder<CowVariant<?>>, List<Vec3>>())
            .build());

    public static void registerAll() {}

    private static <T> AttachmentType<T> register(ResourceLocation id, AttachmentType<T> type) {
        return Registry.register(NeoForgeRegistries.ATTACHMENT_TYPES, id, type);
    }
}
