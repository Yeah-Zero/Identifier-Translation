package Yeah_Zero.Identifier_Translation;

import Yeah_Zero.Identifier_Translation.Configure.Configuration;
import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
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
            case "cold" -> Text.translatable("biome.minecraft.cold_ocean");
            case "desert" -> Text.translatable("biome.minecraft.desert");
            case "jungle" ->
                    Text.translatable("%s %s %s", Text.translatable("biome.minecraft.bamboo_jungle"), Text.translatable("biome.minecraft.sparse_jungle"), Text.translatable("biome.minecraft.jungle"));
            case "mesa" -> Text.translatable("biome.minecraft.badlands");
            case "mountain" ->
                    Text.translatable("%s %s %s %s %s %s %s %s %s", Text.translatable("biome.minecraft.badlands"), Text.translatable("biome.minecraft.eroded_badlands"), Text.translatable("biome.minecraft.savanna_plateau"), Text.translatable("biome.minecraft.stony_shore"), Text.translatable("biome.minecraft.windswept_forest"), Text.translatable("biome.minecraft.windswept_gravelly_hills"), Text.translatable("biome.minecraft.windswept_hills"), Text.translatable("biome.minecraft.windswept_savanna"), Text.translatable("biome.minecraft.wooded_badlands"));
            case "nether" ->
                    Text.translatable("%s %s %s %s %s", Text.translatable("biome.minecraft.basalt_deltas"), Text.translatable("biome.minecraft.crimson_forest"), Text.translatable("biome.minecraft.nether_wastes"), Text.translatable("biome.minecraft.soul_sand_valley"), Text.translatable("biome.minecraft.warped_forest"));
            case "ocean" -> Text.translatable("biome.minecraft.ocean");
            case "plains" ->
                    Text.translatable("%s %s", Text.translatable("biome.minecraft.meadow"), Text.translatable("biome.minecraft.plains"));
            case "savanna" -> Text.translatable("biome.minecraft.savanna");
            case "snowy" -> Text.translatable("biome.minecraft.snowy_plains");
            case "swamp" -> Text.translatable("biome.minecraft.swamp");
            case "taiga" -> Text.translatable("biome.minecraft.taiga");
            case "warm" -> Text.translatable("biome.minecraft.warm_ocean");
            default -> Text.translatable("biome.minecraft." + 分割[分割.length - 1]);
        };
    }

    public static Text 翻译(String 类型, String 标识符) {
        if (标识符.startsWith("#")) {
            return 翻译("tag", 标识符.substring(1));
        } else {
            String 标识符翻译键名 = 类型 + "." + 标识符.replace(":", ".");
            if (类型.equals("poi")) {
                标识符翻译键名 = Text.translatable(标识符翻译键名).getString();
            }
            if (类型.equals("tag")) {
                return Text.literal("#" + 标识符).setStyle(Configuration.配置项.标签样式.生成样式().withHoverEvent(Configuration.配置项.标签样式.显示悬停文本 ? new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable(标识符翻译键名)) : null));
            } else {
                String 标识符翻译;
                if (Configuration.配置项.附加生物群系名称 && ((标识符.contains("mineshaft") && !标识符.equals("mineshaft")) || 标识符.contains("ocean_ruin") || (标识符.contains("ruined_portal") && !标识符.equals("ruined_portal")) || (标识符.contains("shipwreck") && !标识符.equals("shipwreck")) || 标识符.contains("village"))) {
                    标识符翻译 = Text.translatable("%s - %s", Text.translatable(标识符翻译键名), 生物群系名称附加(标识符翻译键名)).getString();
                } else {
                    标识符翻译 = Text.translatable(标识符翻译键名).getString();
                }
                return Text.literal(标识符翻译).setStyle(Configuration.配置项.标识符样式.生成样式().withHoverEvent(Configuration.配置项.标识符样式.显示悬停文本 ? new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(标识符)) : null));
            }
        }
    }

    public static Text 翻译(String 类型, String 标签, String 标识符) {
        String 标签描述键名 = "tag." + 标签.substring(1).replace(":", ".");
        String 标识符翻译键名 = 类型 + "." + 标识符.replace(":", ".");
        if (类型.equals("poi")) {
            标识符翻译键名 = Text.translatable(标识符翻译键名).getString();
        }
        String 标识符翻译;
        if (Configuration.配置项.附加生物群系名称 && ((标识符.contains("mineshaft") && !标识符.equals("mineshaft")) || 标识符.contains("ocean_ruin") || (标识符.contains("ruined_portal") && !标识符.equals("ruined_portal")) || (标识符.contains("shipwreck") && !标识符.equals("shipwreck")) || 标识符.contains("village"))) {
            标识符翻译 = Text.translatable("%s - %s", Text.translatable(标识符翻译键名), 生物群系名称附加(标识符翻译键名)).getString();
        } else {
            标识符翻译 = Text.translatable(标识符翻译键名).getString();
        }
        return Text.translatable("%s (%s)", Text.literal(标签).setStyle(Configuration.配置项.标签样式.生成样式().withHoverEvent(Configuration.配置项.标签样式.显示悬停文本 ? new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable(标签描述键名)) : null)), Text.literal(标识符翻译).setStyle(Configuration.配置项.标识符样式.生成样式().withHoverEvent(Configuration.配置项.标识符样式.显示悬停文本 ? new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(标识符)) : null)));
    }
}
