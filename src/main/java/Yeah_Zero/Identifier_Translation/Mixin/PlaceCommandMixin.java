package Yeah_Zero.Identifier_Translation.Mixin;

import Yeah_Zero.Identifier_Translation.Translator;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.PlaceCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlaceCommand.class)
public class PlaceCommandMixin {
    @Redirect(method = "register(Lcom/mojang/brigadier/CommandDispatcher;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;"))
    private static <T> RequiredArgumentBuilder<ServerCommandSource, T> 提供建议(String 名称, ArgumentType<T> 类型) {
        return switch (名称) {
            case "structure" -> CommandManager.argument(名称, 类型).suggests(Translator.具体结构建议提供者);
            case "feature" -> CommandManager.argument(名称, 类型).suggests(Translator.已配置地物建议提供者);
            default -> CommandManager.argument(名称, 类型);
        };
    }

    @ModifyArg(method = "executePlaceFeature(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/registry/entry/RegistryEntry$Reference;Lnet/minecraft/util/math/BlockPos;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Lnet/minecraft/text/Text;Z)V"), index = 0)
    private static Text 地物反馈消息修改(Text 消息) {
        Object[] 文本参数列表 = ((TranslatableTextContent) 消息.getContent()).getArgs();
        文本参数列表[0] = Translator.翻译("configured_feature", (String) 文本参数列表[0]);
        return Text.translatable("commands.place.feature.success", 文本参数列表);
    }

    @ModifyArg(method = "executePlaceStructure(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/registry/entry/RegistryEntry$Reference;Lnet/minecraft/util/math/BlockPos;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Lnet/minecraft/text/Text;Z)V"), index = 0)
    private static Text 结构反馈消息修改(Text 消息) {
        Object[] 文本参数列表 = ((TranslatableTextContent) 消息.getContent()).getArgs();
        文本参数列表[0] = Translator.翻译("structure", (String) 文本参数列表[0]);
        return Text.translatable("commands.place.structure.success", 文本参数列表);
    }
}
