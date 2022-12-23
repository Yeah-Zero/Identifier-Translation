package Yeah_Zero.Namespaced_Identifier_Translation.Mixin;

import Yeah_Zero.Namespaced_Identifier_Translation.Translator;
import com.mojang.datafixers.util.Pair;
import net.minecraft.command.argument.RegistryEntryPredicateArgumentType;
import net.minecraft.command.argument.RegistryPredicateArgumentType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.LocateCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.Duration;

@Mixin(LocateCommand.class)
public class LocateCommandMixin {
    private static Text 标识符翻译;

    @ModifyArg(method = "executeLocateStructure(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/command/argument/RegistryPredicateArgumentType$RegistryPredicate;)I", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/exceptions/DynamicCommandExceptionType;create(Ljava/lang/Object;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;"), index = 0)
    private static Object STRUCTURE_NOT_FOUND_EXCEPTION参数修改(Object 谓词字符串) {
        return Translator.翻译("structure", (String) 谓词字符串);
    }

    @Inject(method = "sendCoordinates(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/command/argument/RegistryEntryPredicateArgumentType$EntryPredicate;Lnet/minecraft/util/math/BlockPos;Lcom/mojang/datafixers/util/Pair;Ljava/lang/String;ZLjava/time/Duration;)I", at = @At("HEAD"))
    private static void 获取谓词(ServerCommandSource 来源, RegistryEntryPredicateArgumentType.EntryPredicate<?> 谓词, BlockPos 当前坐标, Pair<BlockPos, ? extends RegistryEntry<?>> 结果, String 成功消息, boolean 包括Y坐标, Duration 用时, CallbackInfoReturnable<Integer> 可返回回调信息) {
        标识符翻译 = 谓词.getEntry().map((条目) -> {
            return Translator.翻译(成功消息.split("\\.")[2], 谓词.asString());
        }, (标签) -> {
            return Translator.翻译(成功消息.split("\\.")[2], 谓词.asString(), Translator.获取键名字符串(结果));
        });
    }

    @Inject(method = "sendCoordinates(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/command/argument/RegistryPredicateArgumentType$RegistryPredicate;Lnet/minecraft/util/math/BlockPos;Lcom/mojang/datafixers/util/Pair;Ljava/lang/String;ZLjava/time/Duration;)I", at = @At("HEAD"))
    private static void 获取结构(ServerCommandSource 来源, RegistryPredicateArgumentType.RegistryPredicate<?> 结构, BlockPos 当前坐标, Pair<BlockPos, ? extends RegistryEntry<?>> 结果, String 成功消息, boolean 包括Y坐标, Duration 用时, CallbackInfoReturnable<Integer> 可返回回调信息) {
        标识符翻译 = 结构.getKey().map((键名) -> {
            return Translator.翻译(成功消息.split("\\.")[2], 键名.getValue().toString());
        }, (键名) -> {
            return Translator.翻译(成功消息.split("\\.")[2], "#" + 键名.id().toString(), Translator.获取键名字符串(结果));
        });
    }

    @Redirect(method = "sendCoordinates(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/util/math/BlockPos;Lcom/mojang/datafixers/util/Pair;Ljava/lang/String;ZLjava/lang/String;Ljava/time/Duration;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Lnet/minecraft/text/Text;Z)V"))
    private static void 发送坐标重定向(ServerCommandSource 来源, Text 消息, boolean 广播给管理员) {
        Object[] 参数列表 = ((TranslatableTextContent) 消息.getContent()).getArgs();
        参数列表[0] = 标识符翻译;
        来源.sendFeedback(Text.translatable(((TranslatableTextContent) 消息.getContent()).getKey(), 参数列表), 广播给管理员);
    }
}
