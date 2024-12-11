package house.greenhouse.bovinesandbuttercups.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;

public class WeatherUtil {
    public static boolean isInSnowyWeather(Entity entity) {
        BlockPos pos = entity.blockPosition();
        if (isSnowingAt(pos, entity.level()))
            return true;
        pos = BlockPos.containing(pos.getX(), entity.getBoundingBox().maxY, pos.getZ());
        return isSnowingAt(pos, entity.level());
    }

    private static boolean isSnowingAt(BlockPos pos, Level level) {
        if (!level.isRaining()) {
            return false;
        } else if (!level.canSeeSky(pos)) {
            return false;
        } else if (level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() > pos.getY()) {
            return false;
        } else {
            Biome biome = level.getBiome(pos).value();
            return biome.getPrecipitationAt(pos, level.getSeaLevel()) == Biome.Precipitation.SNOW;
        }
    }
}
