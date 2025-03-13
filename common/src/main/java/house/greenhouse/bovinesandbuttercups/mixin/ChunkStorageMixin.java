package house.greenhouse.bovinesandbuttercups.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.util.dfu.BovinesDataFixer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.storage.ChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChunkStorage.class)
public class ChunkStorageMixin {
    @ModifyExpressionValue(method = "upgradeChunkTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/DataVersion;getVersion()I"))
    private int bovinesandbuttercups$trickGameIntoUpdatingChunkStorageWithBovinesDFU(int original, @Local(argsOnly = true) CompoundTag chunkData) {
        if (BovinesDataFixer.getModDataVersion(chunkData) != BovinesDataFixer.CURRENT_VERSION)
            return -1; // Guarantees that the check will fail.
        return original;
    }
}
