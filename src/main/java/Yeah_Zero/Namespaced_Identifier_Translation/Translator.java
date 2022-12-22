package Yeah_Zero.Namespaced_Identifier_Translation;

import Yeah_Zero.Namespaced_Identifier_Translation.Configure.Configuration;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class Translator {
    public static Text 翻译(String 类型, String 标识符) {
        if (类型.equals("poi")) {
            return Text.translatable(Text.translatable(类型 + "." + 标识符.replace(":", ".")).getString()).setStyle(Style.EMPTY.withColor(Configuration.配置项.标识符颜色.获取格式代码()).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(标识符))));
        }
        return Text.translatable(类型 + "." + 标识符.replace(":", ".")).setStyle(Style.EMPTY.withColor(Configuration.配置项.标识符颜色.获取格式代码()).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(标识符))));
    }

    public static Text 翻译(String 类型, String 标签, String 标识符) {
        if (类型.equals("poi")) {
            return Text.translatable("%s (%s)", Text.translatable(标签).setStyle(Style.EMPTY.withColor(Configuration.配置项.标签颜色.获取格式代码()).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(标签)))), Text.translatable(Text.translatable(类型 + "." + 标识符.replace(":", ".")).getString()).setStyle(Style.EMPTY.withColor(Configuration.配置项.标识符颜色.获取格式代码()).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(标识符)))));
        }
        return Text.translatable("%s (%s)", Text.translatable(标签).setStyle(Style.EMPTY.withColor(Configuration.配置项.标签颜色.获取格式代码()).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(标签)))), Text.translatable(类型 + "." + 标识符.replace(":", ".")).setStyle(Style.EMPTY.withColor(Configuration.配置项.标识符颜色.获取格式代码()).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(标识符)))));
    }
}
