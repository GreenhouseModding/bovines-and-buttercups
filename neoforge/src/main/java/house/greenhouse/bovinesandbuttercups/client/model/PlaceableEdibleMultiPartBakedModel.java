package house.greenhouse.bovinesandbuttercups.client.model;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.client.api.model.condition.PlaceableEdibleSelector;
import house.greenhouse.bovinesandbuttercups.client.util.BovinesModelSetUtil;
import house.greenhouse.bovinesandbuttercups.content.block.entity.PlaceableEdibleBlockEntity;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PlaceableEdibleMultiPartBakedModel implements BakedModel {
    private final List<Pair<PlaceableEdibleSelector, BakedModel>> selectors;
    protected final boolean hasAmbientOcclusion;
    protected final boolean isGui3d;
    protected final boolean usesBlockLight;
    protected final TextureAtlasSprite particleIcon;
    protected final ItemTransforms transforms;
    protected final ItemOverrides overrides;
    private final Map<PlaceableEdibleBlockEntity.EdibleBlockEntityValues, BitSet> selectorCache = new Reference2ObjectOpenHashMap<>();

    private final ModelProperty<BitSet> bitSetProperty = new ModelProperty<>();

    public PlaceableEdibleMultiPartBakedModel(List<Pair<PlaceableEdibleSelector, BakedModel>> selectors) {
        this.selectors = selectors;
        BakedModel model = selectors.iterator().next().getSecond();
        this.hasAmbientOcclusion = model.useAmbientOcclusion();
        this.isGui3d = model.isGui3d();
        this.usesBlockLight = model.usesBlockLight();
        this.particleIcon = model.getParticleIcon();
        this.transforms = model.getTransforms();
        this.overrides = model.getOverrides();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random, ModelData data, @Nullable RenderType renderType) {
        if (state == null) {
            return Collections.emptyList();
        } else {

            List<BakedQuad> list = Lists.newArrayList();
            long k = random.nextLong();

            if (!data.has(bitSetProperty))
                return Collections.emptyList();

            for (int j = 0; j < data.get(bitSetProperty).length(); j++) {
                if (data.get(bitSetProperty).get(j)) {
                    list.addAll(this.selectors.get(j).getSecond().getQuads(state, direction, RandomSource.create(k), data, renderType));
                }
            }

            return list;
        }
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof PlaceableEdibleBlockEntity placeableEdibleBlockEntity) {
            var values = PlaceableEdibleBlockEntity.EdibleBlockEntityValues.fromBlockEntity(placeableEdibleBlockEntity);
            BitSet bitset = this.selectorCache.get(values);
            if (bitset == null) {
                bitset = new BitSet();

                for (int i = 0; i < this.selectors.size(); i++) {
                    Pair<PlaceableEdibleSelector, BakedModel> pair = this.selectors.get(i);
                    if (pair.getFirst().test(placeableEdibleBlockEntity)) {
                        bitset.set(i);
                    }
                }

                this.selectorCache.put(values, bitset);
            }
            return ModelData.of(bitSetProperty, bitset);
        }
        return modelData;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.hasAmbientOcclusion;
    }

    @Override
    public boolean isGui3d() {
        return this.isGui3d;
    }

    @Override
    public boolean usesBlockLight() {
        return this.usesBlockLight;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.particleIcon;
    }

    @Override
    public ItemTransforms getTransforms() {
        return this.transforms;
    }

    @Override
    public ItemOverrides getOverrides() {
        return this.overrides;
    }
}
