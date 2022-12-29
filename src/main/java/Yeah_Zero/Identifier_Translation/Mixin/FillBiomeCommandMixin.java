package Yeah_Zero.Identifier_Translation.Mixin;

import Yeah_Zero.Identifier_Translation.Translator;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.FillBiomeCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Locale;

@Mixin(FillBiomeCommand.class)
public class FillBiomeCommandMixin {
    private static final SuggestionProvider<ServerCommandSource> 具体生物群系建议提供者 = SuggestionProviders.register(new Identifier("specific_biome"), (命令内容, 建议构建器) -> {
        return 命令内容.getSource().getRegistryManager().getOptional(RegistryKeys.BIOME).map((注册项) -> {
            CommandSource.forEachMatching(注册项.getIds(), 建议构建器.getRemaining().toLowerCase(Locale.ROOT), (标识符) -> {
                return 标识符;
            }, (标识符) -> {
                建议构建器.suggest(标识符.toString(), Translator.翻译("biome", 标识符.toString()).copy().setStyle(Style.EMPTY));
            });
            return 建议构建器.buildFuture();
        }).orElseGet(() -> {
            if (命令内容.getSource() instanceof ClientCommandSource) {
                return 命令内容.getSource().getCompletions(命令内容);
            } else {
                return Suggestions.empty();
            }
        });
    });

    @Redirect(method = "register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;"))
    private static <T> RequiredArgumentBuilder<ServerCommandSource, T> 提供结构建议(String 名称, ArgumentType<T> 类型) {
        return switch (名称) {
            case "biome" -> CommandManager.argument(名称, 类型).suggests(具体生物群系建议提供者);
            case "filter" -> CommandManager.argument(名称, 类型).suggests(Translator.生物群系建议提供者);
            default -> CommandManager.argument(名称, 类型);
        };
    }
}
