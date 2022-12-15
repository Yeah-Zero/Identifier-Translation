package Yeah_Zero.Namespaced_Identifier_Translation.Mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.LocateCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocateCommand.class)
public class LocateCommandMixin {
    @Inject(method = "getKeyString(Lcom/mojang/datafixers/util/Pair;)Ljava/lang/String;", at = @At("RETURN"), cancellable = true)
    private static void 获取键名字符串注入(Pair<BlockPos, ? extends RegistryEntry<?>> 结果, CallbackInfoReturnable<String> 可返回回调信息) {
        可返回回调信息.setReturnValue(结果.getSecond().getKey().map((key) -> {
            return key.getValue().toString();
        }).orElse(Text.translatable("namespaced_identifier.unregistered").getString()));
    }

    @Redirect(method = "sendCoordinates(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/util/math/BlockPos;Lcom/mojang/datafixers/util/Pair;Ljava/lang/String;ZLjava/lang/String;Ljava/time/Duration;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Lnet/minecraft/text/Text;Z)V"))
    private static void 发送坐标注入(ServerCommandSource 来源, Text 消息, boolean 广播给管理员) {
        来源.sendFeedback(消息, 广播给管理员);
    }
}
