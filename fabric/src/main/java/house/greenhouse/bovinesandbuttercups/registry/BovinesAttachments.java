package house.greenhouse.bovinesandbuttercups.registry;

import com.mojang.serialization.Codec;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercupsFabric;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypes;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.LockdownAttachment;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BovinesAttachments {
    public static final AttachmentType<LockdownAttachment> LOCKDOWN = AttachmentRegistry.<LockdownAttachment>builder()
            .persistent(LockdownAttachment.CODEC)
            .initializer(() -> new LockdownAttachment(Map.of()))
            .buildAndRegister(LockdownAttachment.ID);
    public static final AttachmentType<CowTypeAttachment> COW_TYPE = AttachmentRegistry.<CowTypeAttachment>builder()
            .persistent(CowTypeAttachment.CODEC)
            .buildAndRegister(CowTypeAttachment.ID);

    public static final AttachmentType<Boolean> PRODUCES_RICH_HONEY = AttachmentRegistry.<Boolean>builder()
            .persistent(Codec.BOOL)
            .buildAndRegister(BovinesAndButtercups.asResource("produces_rich_honey"));
    public static final AttachmentType<UUID> POLLINATING_MOOBLOOM = AttachmentRegistry.<UUID>builder()
            .persistent(UUIDUtil.CODEC)
            .buildAndRegister(BovinesAndButtercups.asResource("pollinating_moobloom"));
    public static final AttachmentType<Map<Holder<CowType<?>>, List<Vec3>>> BABY_PARTICLE_POSITIONS = AttachmentRegistry.createDefaulted(BovinesAndButtercups.asResource("baby_particle_positions"), HashMap::new);
}
