package house.greenhouse.bovinesandbuttercups.util.dfu;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.serialization.Dynamic;
import house.greenhouse.bovinesandbuttercups.mixin.DataFixTypesAccessor;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;

public record BovinesDataFixer(DataFixer fixer) {
    public static final int CURRENT_VERSION = 2110;
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
        return builder.build().fixer();
    }

    public <T> Dynamic<T> updateWithFixers(DataFixTypes types, Dynamic<T> dynamic) {
        return fixer.update(((DataFixTypesAccessor)(Object)types).bovinesandbuttercups$getType(), dynamic, getModDataVersion(dynamic), CURRENT_VERSION);
    }

    public static <T> int getModDataVersion(Dynamic<T> dynamic) {
        return dynamic.get("bovinesandbuttercups:data_version").asInt(CURRENT_VERSION);
    }

    public static void setModDataVersion(CompoundTag tag) {
        tag.putInt("bovinesandbuttercups:data_version", CURRENT_VERSION);
    }
}
