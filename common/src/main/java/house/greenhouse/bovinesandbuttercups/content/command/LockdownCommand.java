package house.greenhouse.bovinesandbuttercups.content.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.attachment.LockdownAttachment;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LockdownCommand {
    public static void register(CommandNode<CommandSourceStack> root, CommandBuildContext context) {

        LiteralCommandNode<CommandSourceStack> lockdownNode = Commands
                .literal("lockdown")
                .build();

        LiteralCommandNode<CommandSourceStack> giveEffectNode = Commands
                .literal("give")
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(RequiredArgumentBuilder.<CommandSourceStack, Holder.Reference<MobEffect>>argument("effect", ResourceArgument.resource(context, Registries.MOB_EFFECT))
                                .suggests((context2, builder) ->
                                        ResourceArgument.resource(context, Registries.MOB_EFFECT).listSuggestions(context2, builder).thenApply(suggestions -> {
                                            var list = new ArrayList<>(suggestions.getList());
                                            list.removeIf(suggestion -> suggestion.getText().equals(BovinesEffects.LOCKDOWN.getRegisteredName()));
                                            return new Suggestions(suggestions.getRange(), list);
                                        })
                                ).executes(context1 -> addEffect(context1, ResourceArgument.getMobEffect(context1, "effect"), 30, false))
                                .then(Commands.argument("seconds", IntegerArgumentType.integer(1, 1000000))
                                        .executes(context1 -> addEffect(context1, ResourceArgument.getMobEffect(context1, "effect"), IntegerArgumentType.getInteger(context1, "seconds"), false))
                                        .then(Commands.argument("hideParticles", BoolArgumentType.bool())
                                                .executes(context1 -> addEffect(context1, ResourceArgument.getMobEffect(context1, "effect"), IntegerArgumentType.getInteger(context1, "seconds"), BoolArgumentType.getBool(context1, "hideParticles")))))
                                .then(Commands.literal("infinite")
                                        .executes(context1 -> addEffect(context1, ResourceArgument.getMobEffect(context1, "effect"), -1, false))
                                        .then(Commands.argument("hideParticles", BoolArgumentType.bool())
                                                .executes(context1 -> addEffect(context1, ResourceArgument.getMobEffect(context1, "effect"), -1, BoolArgumentType.getBool(context1, "hideParticles"))))))
                ).build();

        LiteralCommandNode<CommandSourceStack> clearEffectNode = Commands
                .literal("clear")
                .then(Commands.argument("targets", EntityArgument.entities())
                        .executes(context1 -> clearEffect(context1, null))
                        .then(Commands.argument("effect", ResourceArgument.resource(context, Registries.MOB_EFFECT))
                                .executes(context1 -> clearEffect(context1, ResourceArgument.getMobEffect(context1, "effect")))))
                .build();

        lockdownNode.addChild(clearEffectNode);
        lockdownNode.addChild(giveEffectNode);

        root.addChild(lockdownNode);
    }

    private static final SimpleCommandExceptionType LOCKDOWN_OF_LOCKDOWN = new SimpleCommandExceptionType(Component.translatable("commands.bovinesandbuttercups.lockdown.give.failed.lockdown"));
    private static final SimpleCommandExceptionType GIVE_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.effect.give.failed"));

    private static int addEffect(CommandContext<CommandSourceStack> context, Holder<MobEffect> effect, int specifiedSeconds, boolean hideParticles) throws CommandSyntaxException {
        if (effect.is(BovinesEffects.LOCKDOWN))
            throw LOCKDOWN_OF_LOCKDOWN.create();

        Collection<? extends Entity> entities = EntityArgument.getEntities(context, "targets");

        int ticks = specifiedSeconds != -1 ? specifiedSeconds * 20 : -1;

        int successes = 0;
        Entity firstEntity = null;

        for (Entity entity : entities) {
            if (firstEntity == null)
                firstEntity = entity;
            if (!(entity instanceof LivingEntity living))
                continue;
            boolean lockedEffect = BovinesAndButtercups.getHelper().getLockdownAttachment(living).addLockdownMobEffect(effect, ticks);
            boolean addedLockdown = living.addEffect(new MobEffectInstance(BovinesEffects.LOCKDOWN, ticks, 0, false, !hideParticles));
            if (lockedEffect || addedLockdown) {
                ++successes;
                LockdownAttachment.sync(living);
            }
        }

        if (successes == 0) {
            throw GIVE_FAILED.create();
        } else {
            if (entities.size() > 1) {
                int finalSuccesses = successes;
                context.getSource().sendSuccess(() -> Component.translatable("commands.bovinesandbuttercups.lockdown.give.success.multiple", effect.value().getDisplayName(), finalSuccesses), true);
            } else {
                Entity finalFirstEntity = firstEntity;
                context.getSource().sendSuccess(() -> Component.translatable("commands.bovinesandbuttercups.lockdown.give.success.single", effect.value().getDisplayName(), finalFirstEntity.getDisplayName()), true);
            }
        }
        return successes;
    }

    private static final SimpleCommandExceptionType CLEAR_EVERYTHING_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.bovinesandbuttercups.lockdown.clear.everything.failed"));
    private static final SimpleCommandExceptionType CLEAR_SPECIFIC_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.bovinesandbuttercups.lockdown.clear.specific.failed"));

    private static int clearEffect(CommandContext<CommandSourceStack> context, @Nullable Holder<MobEffect> effect) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgument.getEntities(context, "targets");

        int successes = 0;
        Entity firstEntity = null;

        for (Entity entity : entities) {
            if (firstEntity == null)
                firstEntity = entity;
            if (!(entity instanceof LivingEntity living))
                continue;
            if (effect != null && BovinesAndButtercups.getHelper().getLockdownAttachment(living).removeLockdownMobEffect(effect) || effect == null && living.removeEffect(BovinesEffects.LOCKDOWN)) {
                ++successes;
                LockdownAttachment.sync(living);
                if (BovinesAndButtercups.getHelper().getLockdownAttachment(living).effects().isEmpty())
                    living.removeEffect(BovinesEffects.LOCKDOWN);
            }
        }

        if (effect == null) {
            if (successes == 0) {
                throw CLEAR_EVERYTHING_FAILED.create();
            } else {
                if (entities.size() > 1) {
                    int finalSuccesses = successes;
                    context.getSource().sendSuccess(() -> Component.translatable("commands.bovinesandbuttercups.lockdown.clear.everything.success.multiple", finalSuccesses), true);
                } else {
                    Entity finalFirstEntity = firstEntity;
                    context.getSource().sendSuccess(() -> Component.translatable("commands.bovinesandbuttercups.lockdown.clear.everything.success.single", finalFirstEntity.getDisplayName()), true);
                }
            }
        } else {

            if (successes == 0) {
                throw CLEAR_SPECIFIC_FAILED.create();
            } else {
                if (entities.size() > 1) {
                    int finalSuccesses = successes;
                    context.getSource().sendSuccess(() -> Component.translatable("commands.bovinesandbuttercups.lockdown.clear.specific.success.multiple", effect.value().getDisplayName(), finalSuccesses), true);
                } else {
                    Entity finalFirstEntity = firstEntity;
                    context.getSource().sendSuccess(() -> Component.translatable("commands.bovinesandbuttercups.lockdown.clear.specific.success.single", effect.value().getDisplayName(), finalFirstEntity.getDisplayName()), true);
                }
            }
        }
        return successes;
    }
}