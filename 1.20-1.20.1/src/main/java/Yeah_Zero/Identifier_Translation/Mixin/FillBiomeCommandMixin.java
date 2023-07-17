package Yeah_Zero.Identifier_Translation.Mixin;

import Yeah_Zero.Identifier_Translation.Util.Translator;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.FillBiomeCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FillBiomeCommand.class)
public class FillBiomeCommandMixin {
    @Redirect(method = "register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;"))
    private static <T> RequiredArgumentBuilder<ServerCommandSource, T> 提供建议(String 名称, ArgumentType<T> 类型) {
        return switch (名称) {
            case "biome" -> CommandManager.argument(名称, 类型).suggests(Translator.具体生物群系建议提供者);
            case "filter" -> CommandManager.argument(名称, 类型).suggests(Translator.生物群系建议提供者);
            default -> CommandManager.argument(名称, 类型);
        };
    }
}
