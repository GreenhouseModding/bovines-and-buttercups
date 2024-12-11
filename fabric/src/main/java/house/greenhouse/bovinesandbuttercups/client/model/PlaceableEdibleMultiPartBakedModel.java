package house.greenhouse.bovinesandbuttercups.client.model;

import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.client.api.model.condition.PlaceableEdibleSelector;
import house.greenhouse.bovinesandbuttercups.content.block.entity.PlaceableEdibleBlockEntity;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PlaceableEdibleMultiPartBakedModel implements BakedModel {
    private final List<Pair<PlaceableEdibleSelector, BakedModel>> selectors;
    protected final boolean hasAmbientOcclusion;
    protected final boolean isGui3d;
    protected final boolean usesBlockLight;
    protected final TextureAtlasSprite particleIcon;
    protected final ItemTransforms transforms;
    private final Map<PlaceableEdibleBlockEntity.EdibleBlockEntityValues, BitSet> selectorCache = new Reference2ObjectOpenHashMap<>();

    public PlaceableEdibleMultiPartBakedModel(List<Pair<PlaceableEdibleSelector, BakedModel>> selectors) {
        this.selectors = selectors;
        BakedModel model = selectors.iterator().next().getSecond();
        this.hasAmbientOcclusion = model.useAmbientOcclusion();
        this.isGui3d = model.isGui3d();
        this.usesBlockLight = model.usesBlockLight();
        this.particleIcon = model.getParticleIcon();
        this.transforms = model.getTransforms();
    }

    @Override
    public void emitBlockQuads(QuadEmitter emitter, BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, Predicate<@Nullable Direction> cullTest) {
        BlockEntity blockEntity = blockView.getBlockEntity(pos);
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

            for (int j = 0; j < bitset.length(); j++) {
                if (bitset.get(j)) {
                    BakedModel model = selectors.get(j).getSecond();
                    for (int i = 0; i <= ModelHelper.NULL_FACE_ID; i++) {
                        final Direction cullFace = ModelHelper.faceFromIndex(i);

                        if (cullTest.test(cullFace))
                            continue;

                        final List<BakedQuad> quads = model.getQuads(state, cullFace, randomSupplier.get());

                        for (final BakedQuad q : quads) {
                            emitter.fromVanilla(q, emitter.material(), cullFace);
                            emitter.emit();
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource) {
        return Collections.emptyList();
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
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
    public TextureAtlasSprite getParticleIcon() {
        return this.particleIcon;
    }

    @Override
    public ItemTransforms getTransforms() {
        return this.transforms;
    }
}
