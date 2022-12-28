package Yeah_Zero.Identifier_Translation;

import Yeah_Zero.Identifier_Translation.Configure.Configuration;
import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;

public class Translator {
    public static String 获取键名字符串(Pair<BlockPos, ? extends RegistryEntry<?>> 结果) {
        return 结果.getSecond().getKey().map((键名) -> {
            return 键名.getValue().toString();
        }).orElse(Text.translatable("[unregistered]").getString());
    }

    private static Text 生物群系名称附加(String 标识符翻译键名) {
        String[] 分割 = 标识符翻译键名.split("_");
        return switch (分割[分割.length - 1]) {
            case "beached" -> Text.translatable("biome.minecraft.beach");
            case "cold", "warm" -> Text.translatable("biome.minecraft." + 分割[分割.length - 1] + "_ocean");
            case "mesa" -> Text.translatable("biome.minecraft.badlands");
            case "mountain" -> Text.translatable("biomes.minecraft.mountains");
            case "nether" -> Text.translatable("dimension.minecraft.nether");
            case "snowy" -> Text.translatable("biome.minecraft.snowy_plains");
            default -> Text.translatable("biome.minecraft." + 分割[分割.length - 1]);
        };
    }

    public static Text 翻译(String 类型, String 标识符) {
        if (标识符.startsWith("#")) {
            return 翻译("tag", 标识符.substring(1));
        } else {
            String 标识符翻译键名 = Util.createTranslationKey(类型, new Identifier(标识符));
            if (类型.equals("poi")) {
                标识符翻译键名 = Text.translatable(标识符翻译键名).getString();
            }
            if (类型.equals("tag")) {
                return Text.literal("#" + 标识符).setStyle(Configuration.配置项.标签样式.生成样式().withHoverEvent(Configuration.配置项.标签样式.显示悬停文本 ? new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable(标识符翻译键名)) : null));
            } else {
                String 标识符翻译;
                if ((标识符.contains("mineshaft") && !标识符.equals("minecraft:mineshaft")) || 标识符.contains("ocean_ruin") || (标识符.contains("ruined_portal") && !标识符.equals("minecraft:ruined_portal")) || (标识符.contains("shipwreck") && !标识符.equals("minecraft:shipwreck")) || 标识符.contains("village")) {
                    标识符翻译 = Text.translatable("%s (%s)", Text.translatable(标识符翻译键名), 生物群系名称附加(标识符翻译键名)).getString();
                } else {
                    标识符翻译 = Text.translatable(标识符翻译键名).getString();
                }
                return Text.literal(标识符翻译).setStyle(Configuration.配置项.标识符样式.生成样式().withHoverEvent(Configuration.配置项.标识符样式.显示悬停文本 ? new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(标识符)) : null));
            }
        }
    }

    public static Text 翻译(String 类型, String 标签, String 标识符) {
        String 标签描述键名 = Util.createTranslationKey("tag", new Identifier(标签.substring(1)));
        String 标识符翻译键名 = Util.createTranslationKey(类型, new Identifier(标识符));
        if (类型.equals("poi")) {
            标识符翻译键名 = Text.translatable(标识符翻译键名).getString();
        }
        String 标识符翻译;
        if ((标识符.contains("mineshaft") && !标识符.equals("minecraft:mineshaft")) || 标识符.contains("ocean_ruin") || (标识符.contains("ruined_portal") && !标识符.equals("minecraft:ruined_portal")) || (标识符.contains("shipwreck") && !标识符.equals("minecraft:shipwreck")) || 标识符.contains("village")) {
            标识符翻译 = Text.translatable("%s (%s)", Text.translatable(标识符翻译键名), 生物群系名称附加(标识符翻译键名)).getString();
        } else {
            标识符翻译 = Text.translatable(标识符翻译键名).getString();
        }
        return Text.translatable("%s (%s)", Text.literal(标签).setStyle(Configuration.配置项.标签样式.生成样式().withHoverEvent(Configuration.配置项.标签样式.显示悬停文本 ? new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable(标签描述键名)) : null)), Text.literal(标识符翻译).setStyle(Configuration.配置项.标识符样式.生成样式().withHoverEvent(Configuration.配置项.标识符样式.显示悬停文本 ? new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(标识符)) : null)));
    }
}
