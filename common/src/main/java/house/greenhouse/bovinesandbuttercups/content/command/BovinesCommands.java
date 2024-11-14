
package house.greenhouse.bovinesandbuttercups.content.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class BovinesCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {

        LiteralCommandNode<CommandSourceStack> rootNode = Commands
                .literal("bovinesandbuttercups")
                .build();

        LockdownCommand.register(rootNode, context);

        dispatcher.getRoot().addChild(rootNode);
    }
}