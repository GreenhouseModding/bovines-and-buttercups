package house.greenhouse.bovinesandbuttercups.util.dfu;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import house.greenhouse.bovinesandbuttercups.mixin.DataFixTypesAccessor;
import house.greenhouse.bovinesandbuttercups.util.dfu.fixer.NectarDecomponentizeFix;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;

import java.util.function.BiFunction;

public record BovinesDataFixer(DataFixer fixer) {
    // Bump to the nearest rounded down 100 on the later version if two versions are being maintained.
    public static final int CURRENT_VERSION = 100;
    private static final BiFunction<Integer, Schema, Schema> SAME = Schema::new;
    private static BovinesDataFixer instance;

    public static BovinesDataFixer get() {
        return instance;
    }

    public static void register() {
        if (instance == null)
            instance = new BovinesDataFixer(registerFixers());
    }

    private static DataFixer registerFixers() {
        DataFixerBuilder builder = new DataFixerBuilder(CURRENT_VERSION);
        builder.addSchema(0, (integer, schema) -> DataFixers.getDataFixer()
                .getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().getDataVersion().getVersion())));
        Schema schema100 = builder.addSchema(100, SAME);
        builder.addFixer(new NectarDecomponentizeFix(schema100));
        return builder.build().fixer();
    }

    public <T> Dynamic<T> updateWithFixers(DataFixTypes types, Dynamic<T> dynamic, int originalMinecraftVersion) {
        return fixer.update(((DataFixTypesAccessor)(Object)types).bovinesandbuttercups$getType(), dynamic, getModDataVersion(dynamic, originalMinecraftVersion), CURRENT_VERSION);
    }

    public static <T> int getModDataVersion(Dynamic<T> dynamic, int originalMinecraftVersion) {
        return dynamic.get("bovinesandbuttercups:data_version").asInt((originalMinecraftVersion < 4189) ? 0 : CURRENT_VERSION); // 4189 is 1.21.4's DFU version.
    }

    public static void setModDataVersion(CompoundTag tag) {
        tag.putInt("bovinesandbuttercups:data_version", CURRENT_VERSION);
    }
}
