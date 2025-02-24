package house.greenhouse.bovinesandbuttercups.mixin.fabric;

import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import house.greenhouse.bovinesandbuttercups.api.util.ConversionUtil;
import house.greenhouse.bovinesandbuttercups.api.variant.ConvertData;
import house.greenhouse.bovinesandbuttercups.content.attachment.BovinesAttachments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mixin(LightningBolt.class)
public abstract class LightningBoltMixin extends Entity {
    @Shadow @Final private Set<Entity> hitEntities;

    public LightningBoltMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;thunderHit(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LightningBolt;)V"))
    private void bovinesandbuttercups$thunderHit(CallbackInfo ci, @Local Entity entity) {
        if (hitEntities.contains(entity) || ConversionUtil.CONVERTED_BY_BOVINES.contains(entity) || !(entity instanceof Mob mob) || !entity.hasAttached(BovinesAttachments.COW_VARIANT))
            return;
        CowVariantAttachment attachment = entity.getAttached(BovinesAttachments.COW_VARIANT);
        if (!attachment.cowVariant().value().type().isLightningLogicIndirect())
            return;
        if (!attachment.cowVariant().isBound())
            return;
        if (!(level() instanceof ServerLevel serverLevel))
            return;
        if (!attachment.cowVariant().value().configuration().allowsConversion(entity)) {
            ConversionUtil.CONVERTED_BY_BOVINES.add(entity);
            return;
        }
        if (attachment.previousCowVariant().isEmpty()) {
            if (attachment.cowVariant().value().configuration().settings().thunderConverts().isEmpty())
                return;

            LootParams params = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.THIS_ENTITY, this)
                    .withParameter(LootContextParams.ORIGIN, position())
                    .withParameter(LootContextParams.DAMAGE_SOURCE, this.damageSources().lightningBolt())
                    .withParameter(LootContextParams.ATTACKING_ENTITY, this)
                    .create(LootContextParamSets.ENTITY);
            LootContext context = new LootContext.Builder(params).create(Optional.empty());

            List<WeightedEntry.Wrapper<ConvertData>> compatibleList = attachment.cowVariant().value().configuration().settings().thunderConverts().unwrap().stream().filter(convertDataWrapper -> convertDataWrapper.data().conditions().isEmpty() || convertDataWrapper.data().conditions().stream().allMatch(condition -> condition.test(context))).toList();

            ConversionUtil.convert(mob, serverLevel, (LightningBolt)(Object)this, compatibleList);
        } else {
            ConversionUtil.revertFromPrevious(mob, serverLevel, (LightningBolt)(Object)this);
        }
    }

}
