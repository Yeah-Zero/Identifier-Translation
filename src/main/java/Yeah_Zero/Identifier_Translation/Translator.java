package Yeah_Zero.Identifier_Translation;

import Yeah_Zero.Identifier_Translation.Configure.Configuration;
import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class Translator {
    public static String 获取键名字符串(Pair<BlockPos, ? extends RegistryEntry<?>> 结果) {
        return 结果.getSecond().getKey().map((键名) -> {
            return 键名.getValue().toString();
        }).orElse(Text.translatable("[unregistered]").getString());
    }

    private static String 生物群系名称附加型翻译(String 标识符翻译键名) {
        //进行中……
        return "";
    }

    public static Text 翻译(String 类型, String 标识符) {
        if (标识符.startsWith("#")) {
            return 翻译("tag", 标识符.substring(1));
        } else {
            String 标识符翻译键名 = 类型 + "." + 标识符.replace(":", ".");
            if (类型.equals("poi")) {
                标识符翻译键名 = Text.translatable(标识符翻译键名).getString();
            }
            //进行中……
            return Text.translatable("");
        }
    }

    public static Text 翻译(String 类型, String 标签, String 标识符) {
        String 标签描述键名 = "tag." + 标签.substring(1).replace(":", ".");
        String 标识符翻译键名 = 类型 + "." + 标识符.replace(":", ".");
        if (类型.equals("poi")) {
            标识符翻译键名 = Text.translatable(标识符翻译键名).getString();
        }
        String 标识符翻译;
        if (标识符.contains("ocean_ruin") || (标识符.contains("ruined_portal") && !标识符.equals("ruined_portal")) || (标识符.contains("shipwreck") && !标识符.equals("shipwreck")) || 标识符.contains("village")) {
            标识符翻译 = 生物群系名称附加型翻译(标识符翻译键名);
        } else {
            标识符翻译 = Text.translatable(标识符翻译键名).getString();
        }
        return Text.translatable("%s (%s)", Text.literal(标签).setStyle(Style.EMPTY.withColor(Configuration.配置项.标签颜色.获取格式代码()).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable(标签描述键名)))), Text.literal(标识符翻译).setStyle(Style.EMPTY.withColor(Configuration.配置项.标识符颜色.获取格式代码()).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(标识符)))));
    }
}
