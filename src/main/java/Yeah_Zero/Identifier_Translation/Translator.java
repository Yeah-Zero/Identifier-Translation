package Yeah_Zero.Identifier_Translation;

import Yeah_Zero.Identifier_Translation.Configure.Configuration;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;

import java.util.Locale;

public class Translator {
    public static final SuggestionProvider<ServerCommandSource> 结构建议提供者 = Translator.建议提供("structure", RegistryKeys.STRUCTURE, CommandSource.SuggestedIdType.ALL);
    public static final SuggestionProvider<ServerCommandSource> 生物群系建议提供者 = Translator.建议提供("biome", RegistryKeys.BIOME, CommandSource.SuggestedIdType.ALL);
    public static final SuggestionProvider<ServerCommandSource> 兴趣点建议提供者 = Translator.建议提供("poi", RegistryKeys.POINT_OF_INTEREST_TYPE, CommandSource.SuggestedIdType.ALL);
    public static final SuggestionProvider<ServerCommandSource> 具体生物群系建议提供者 = Translator.建议提供("specific_biome", RegistryKeys.BIOME, CommandSource.SuggestedIdType.ELEMENTS);
    public static final SuggestionProvider<ServerCommandSource> 具体结构建议提供者 = Translator.建议提供("specific_structure", RegistryKeys.STRUCTURE, CommandSource.SuggestedIdType.ELEMENTS);
    public static final SuggestionProvider<ServerCommandSource> 已配置地物建议提供者 = Translator.建议提供("configured_feature", RegistryKeys.CONFIGURED_FEATURE, CommandSource.SuggestedIdType.ELEMENTS);

    public static String 获取键名字符串(Pair<BlockPos, ? extends RegistryEntry<?>> 结果) {
        return 结果.getSecond().getKey().map((键名) -> {
            return 键名.getValue().toString();
        }).orElse(Text.translatable("[unregistered]").getString());
    }

    private static <T> SuggestionProvider<ServerCommandSource> 建议提供(String 类型, RegistryKey<Registry<T>> 注册项键, CommandSource.SuggestedIdType 可建议类型) {
        return SuggestionProviders.register(new Identifier(类型), (命令内容, 建议构建器) -> {
            return 命令内容.getSource().getRegistryManager().getOptional(注册项键).map((注册项) -> {
                if (可建议类型.canSuggestTags()) {
                    CommandSource.forEachMatching(注册项.streamTags().map(TagKey::id)::iterator, 建议构建器.getRemaining().toLowerCase(Locale.ROOT), "#", (标识符) -> {
                        return 标识符;
                    }, (标识符) -> {
                        建议构建器.suggest("#" + 标识符, Text.translatable(Util.createTranslationKey("tag", 标识符)));
                    });
                }
                if (可建议类型.canSuggestElements()) {
                    CommandSource.forEachMatching(注册项.getIds(), 建议构建器.getRemaining().toLowerCase(Locale.ROOT), (标识符) -> {
                        return 标识符;
                    }, (标识符) -> {
                        if (类型.equals("configured_feature")) {
                            建议构建器.suggest(标识符.toString(), Text.translatable(Util.createTranslationKey("configured_feature", 标识符)));
                        } else {
                            建议构建器.suggest(标识符.toString(), 翻译(类型, 标识符.toString()).copy().setStyle(Style.EMPTY));
                        }
                    });
                }
                return 建议构建器.buildFuture();
            }).orElseGet(() -> {
                if (命令内容.getSource() instanceof ClientCommandSource) {
                    return 命令内容.getSource().getCompletions(命令内容);
                } else {
                    return Suggestions.empty();
                }
            });
        });
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

    public static Text 翻译(String 类型, String 标识符字符串) {
        if (标识符字符串.startsWith("#")) {
            return 翻译("tag", 标识符字符串.substring(1));
        } else {
            String 标识符翻译键名 = Util.createTranslationKey(类型, new Identifier(标识符字符串));
            if (类型.equals("poi")) {
                标识符翻译键名 = Text.translatable(标识符翻译键名).getString();
            }
            if (类型.equals("tag")) {
                return Text.literal("#" + 标识符字符串).setStyle(Configuration.配置项.标签样式.生成样式().withHoverEvent(Configuration.配置项.标签样式.显示悬停文本 ? new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable(标识符翻译键名)) : null));
            } else if (类型.equals("configured_feature")) {
                return Text.literal(标识符字符串).setStyle(Configuration.配置项.标识符样式.生成样式().withHoverEvent(Configuration.配置项.标识符样式.显示悬停文本 ? new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable(标识符翻译键名)) : null));
            } else {
                String 标识符翻译;
                if ((标识符字符串.contains("mineshaft") && !标识符字符串.equals("minecraft:mineshaft")) || 标识符字符串.contains("ocean_ruin") || (标识符字符串.contains("ruined_portal") && !标识符字符串.equals("minecraft:ruined_portal")) || (标识符字符串.contains("shipwreck") && !标识符字符串.equals("minecraft:shipwreck")) || 标识符字符串.contains("village")) {
                    标识符翻译 = Text.translatable("%s (%s)", Text.translatable(标识符翻译键名), 生物群系名称附加(标识符翻译键名)).getString();
                } else {
                    标识符翻译 = Text.translatable(标识符翻译键名).getString();
                }
                return Text.literal(标识符翻译).setStyle(Configuration.配置项.标识符样式.生成样式().withHoverEvent(Configuration.配置项.标识符样式.显示悬停文本 ? new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(标识符字符串)) : null));
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
