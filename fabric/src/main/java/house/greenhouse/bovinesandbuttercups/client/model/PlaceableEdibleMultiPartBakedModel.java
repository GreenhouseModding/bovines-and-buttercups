package house.greenhouse.bovinesandbuttercups.client.model;

import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.client.api.model.condition.PlaceableEdibleSelector;
import house.greenhouse.bovinesandbuttercups.client.util.BovinesModelSetUtil;
import house.greenhouse.bovinesandbuttercups.content.block.entity.PlaceableEdibleBlockEntity;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
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
import org.jetbrains.annotations.Nullable;

import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class PlaceableEdibleMultiPartBakedModel implements BakedModel {
    private final List<Pair<PlaceableEdibleSelector, BakedModel>> selectors;
    protected final boolean hasAmbientOcclusion;
    protected final boolean isGui3d;
    protected final boolean usesBlockLight;
    protected final TextureAtlasSprite particleIcon;
    protected final ItemTransforms transforms;
    protected final ItemOverrides overrides;
    private final Map<PlaceableEdibleBlockEntity.EdibleBlockEntityValues, BitSet> selectorCache = new Reference2ObjectOpenHashMap<>();

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
    public void emitBlockQuads(BlockAndTintGetter blockGetter, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        BlockEntity blockEntity = blockGetter.getBlockEntity(pos);
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

            QuadEmitter emitter = context.getEmitter();

            for (int j = 0; j < bitset.length(); j++) {
                if (bitset.get(j)) {
                    BakedModel model = selectors.get(j).getSecond();

                    final RenderMaterial material = model.useAmbientOcclusion() ? RendererAccess.INSTANCE.getRenderer().materialFinder().blendMode(BlendMode.fromRenderLayer(ItemBlockRenderTypes.getChunkRenderType(state))).find() : RendererAccess.INSTANCE.getRenderer().materialFinder().ambientOcclusion(TriState.FALSE).blendMode(BlendMode.fromRenderLayer(ItemBlockRenderTypes.getChunkRenderType(state))).find();

                    for (int i = 0; i <= ModelHelper.NULL_FACE_ID; i++) {
                        final Direction cullFace = ModelHelper.faceFromIndex(i);

                        if (!context.hasTransform() && context.isFaceCulled(cullFace))
                            continue;

                        final List<BakedQuad> quads = model.getQuads(state, cullFace, randomSupplier.get());

                        for (final BakedQuad q : quads) {
                            emitter.fromVanilla(q, material, cullFace);
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
