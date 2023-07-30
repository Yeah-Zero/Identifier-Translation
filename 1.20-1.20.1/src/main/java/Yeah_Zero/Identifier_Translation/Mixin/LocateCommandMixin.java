package Yeah_Zero.Identifier_Translation.Mixin;

import Yeah_Zero.Identifier_Translation.Configure.Manager;
import Yeah_Zero.Identifier_Translation.Util.Translator;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.datafixers.util.Pair;
import net.minecraft.command.argument.RegistryEntryPredicateArgumentType;
import net.minecraft.command.argument.RegistryPredicateArgumentType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.LocateCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;

@Mixin(LocateCommand.class)
public class LocateCommandMixin {
    @Unique
    private static String 谓词字符串提取;
    @Unique
    private static Text 翻译;
    @Final
    @Shadow
    private static DynamicCommandExceptionType STRUCTURE_INVALID_EXCEPTION;

    @Redirect(method = "register(Lcom/mojang/brigadier/CommandDispatcher;Lnet/minecraft/command/CommandRegistryAccess;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;"))
    private static <T> RequiredArgumentBuilder<ServerCommandSource, T> 提供建议(String 名称, ArgumentType<T> 类型) {
        return switch (名称) {
            case "structure" -> CommandManager.argument(名称, 类型).suggests(Translator.结构建议提供者);
            case "biome" -> CommandManager.argument(名称, 类型).suggests(Translator.生物群系建议提供者);
            case "poi" -> CommandManager.argument(名称, 类型).suggests(Translator.兴趣点建议提供者);
            default -> CommandManager.argument(名称, 类型);
        };
    }

    @Inject(method = "executeLocateStructure(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/command/argument/RegistryPredicateArgumentType$RegistryPredicate;)I", at = @At("HEAD"))
    private static void 谓词获取(ServerCommandSource 来源, RegistryPredicateArgumentType.RegistryPredicate<Structure> 谓词, CallbackInfoReturnable<Integer> 可返回回调信息) {
        谓词字符串提取 = 谓词.asString();
    }

    @Redirect(method = "executeLocateStructure(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/command/argument/RegistryPredicateArgumentType$RegistryPredicate;)I", at = @At(value = "INVOKE", target = "Ljava/util/Optional;orElseThrow(Ljava/util/function/Supplier;)Ljava/lang/Object;"))
    private static <X, T> T STRUCTURE_INVALID_EXCEPTION参数修改(Optional<T> 实例, Supplier<? extends X> 异常提供者) throws CommandSyntaxException {
        return 实例.orElseThrow(() -> {
            return STRUCTURE_INVALID_EXCEPTION.create(Text.literal(谓词字符串提取).setStyle(谓词字符串提取.startsWith("#") ? Manager.配置项.标签样式.生成样式() : Manager.配置项.标识符样式.生成样式()));
        });
    }

    @ModifyArg(method = "executeLocateStructure(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/command/argument/RegistryPredicateArgumentType$RegistryPredicate;)I", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/exceptions/DynamicCommandExceptionType;create(Ljava/lang/Object;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;"), index = 0)
    private static Object STRUCTURE_NOT_FOUND_EXCEPTION参数修改(Object 谓词字符串) {
        return Translator.翻译("structure", (String) 谓词字符串);
    }

    @ModifyArg(method = "executeLocateBiome(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/command/argument/RegistryEntryPredicateArgumentType$EntryPredicate;)I", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/exceptions/DynamicCommandExceptionType;create(Ljava/lang/Object;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;"), index = 0)
    private static Object BIOME_NOT_FOUND_EXCEPTION参数修改(Object 谓词字符串) {
        return Translator.翻译("biome", (String) 谓词字符串);
    }

    @ModifyArg(method = "executeLocatePoi(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/command/argument/RegistryEntryPredicateArgumentType$EntryPredicate;)I", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/exceptions/DynamicCommandExceptionType;create(Ljava/lang/Object;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;"), index = 0)
    private static Object POI_NOT_FOUND_EXCEPTION参数修改(Object 谓词字符串) {
        return Translator.翻译("poi", (String) 谓词字符串);
    }

    @Inject(method = "sendCoordinates(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/command/argument/RegistryEntryPredicateArgumentType$EntryPredicate;Lnet/minecraft/util/math/BlockPos;Lcom/mojang/datafixers/util/Pair;Ljava/lang/String;ZLjava/time/Duration;)I", at = @At("HEAD"))
    private static void 获取谓词(ServerCommandSource 来源, RegistryEntryPredicateArgumentType.EntryPredicate<?> 谓词, BlockPos 当前坐标, Pair<BlockPos, ? extends RegistryEntry<?>> 结果, String 成功消息, boolean 包括Y坐标, Duration 用时, CallbackInfoReturnable<Integer> 可返回回调信息) {
        翻译 = 谓词.getEntry().map((条目) -> {
            return Translator.翻译(成功消息.split("\\.")[2], 谓词.asString());
        }, (标签) -> {
            return Translator.翻译(成功消息.split("\\.")[2], 谓词.asString(), Translator.获取键名字符串(结果));
        });
    }

    @Inject(method = "sendCoordinates(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/command/argument/RegistryPredicateArgumentType$RegistryPredicate;Lnet/minecraft/util/math/BlockPos;Lcom/mojang/datafixers/util/Pair;Ljava/lang/String;ZLjava/time/Duration;)I", at = @At("HEAD"))
    private static void 获取结构(ServerCommandSource 来源, RegistryPredicateArgumentType.RegistryPredicate<?> 结构, BlockPos 当前坐标, Pair<BlockPos, ? extends RegistryEntry<?>> 结果, String 成功消息, boolean 包括Y坐标, Duration 用时, CallbackInfoReturnable<Integer> 可返回回调信息) {
        翻译 = 结构.getKey().map((键名) -> {
            return Translator.翻译(成功消息.split("\\.")[2], 键名.getValue().toString());
        }, (键名) -> {
            return Translator.翻译(成功消息.split("\\.")[2], "#" + 键名.id().toString(), Translator.获取键名字符串(结果));
        });
    }

    @ModifyArg(method = "sendCoordinates(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/util/math/BlockPos;Lcom/mojang/datafixers/util/Pair;Ljava/lang/String;ZLjava/lang/String;Ljava/time/Duration;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V"), index = 0)
    private static Supplier<Text> 反馈消息修改(Supplier<Text> 反馈提供者) {
        Object[] 文本参数列表 = ((TranslatableTextContent) 反馈提供者.get().getContent()).getArgs();
        文本参数列表[0] = 翻译;
        return () -> Text.translatable(((TranslatableTextContent) 反馈提供者.get().getContent()).getKey(), 文本参数列表);
    }
}
