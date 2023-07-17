package Yeah_Zero.Identifier_Translation.Configure;

import Yeah_Zero.Identifier_Translation.Initializer.Main;
import Yeah_Zero.Identifier_Translation.Util.ColorCode;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ScreenAPI implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (上级界面) -> {
            try {
                ConfigBuilder 构建器 = ConfigBuilder.create().setParentScreen(上级界面).setTitle(Text.translatable("identifier_translation.configure.title")).setDoesConfirmSave(false).setSavingRunnable(Manager::保存).setDefaultBackgroundTexture(new Identifier("minecraft", "textures/block/white_concrete.png"));
                SubCategoryBuilder 标签样式 = 构建器.entryBuilder().startSubCategory(Text.translatable("identifier_translation.configure.tag_style")).setExpanded(true);
                标签样式.add(构建器.entryBuilder().startEnumSelector(Text.translatable("identifier_translation.configure.tag_style.color"), ColorCode.class, Manager.配置项.标签样式.颜色).setDefaultValue(ColorCode.金色).setEnumNameProvider(ColorCode::颜色代码翻译).setSaveConsumer((新值) -> {
                    Manager.配置项.标签样式.颜色 = 新值;
                }).build());
                标签样式.add(构建器.entryBuilder().startBooleanToggle(Text.translatable("formatting_code.obfuscated"), Manager.配置项.标签样式.随机字符).setDefaultValue(false).setSaveConsumer((新值) -> {
                    Manager.配置项.标签样式.随机字符 = 新值;
                }).build());
                标签样式.add(构建器.entryBuilder().startBooleanToggle(Text.translatable("formatting_code.bold"), Manager.配置项.标签样式.粗体).setDefaultValue(false).setSaveConsumer((新值) -> {
                    Manager.配置项.标签样式.粗体 = 新值;
                }).build());
                标签样式.add(构建器.entryBuilder().startBooleanToggle(Text.translatable("formatting_code.strikethrough"), Manager.配置项.标签样式.删除线).setDefaultValue(false).setSaveConsumer((新值) -> {
                    Manager.配置项.标签样式.删除线 = 新值;
                }).build());
                标签样式.add(构建器.entryBuilder().startBooleanToggle(Text.translatable("formatting_code.underline"), Manager.配置项.标签样式.下划线).setDefaultValue(false).setSaveConsumer((新值) -> {
                    Manager.配置项.标签样式.下划线 = 新值;
                }).build());
                标签样式.add(构建器.entryBuilder().startBooleanToggle(Text.translatable("formatting_code.italic"), Manager.配置项.标签样式.斜体).setDefaultValue(false).setSaveConsumer((新值) -> {
                    Manager.配置项.标签样式.斜体 = 新值;
                }).build());
                标签样式.add(构建器.entryBuilder().startBooleanToggle(Text.translatable("identifier_translation.configure.tag_style.hover"), Manager.配置项.标签样式.显示悬停文本).setDefaultValue(true).setSaveConsumer((新值) -> {
                    Manager.配置项.标签样式.显示悬停文本 = 新值;
                }).build());
                构建器.getOrCreateCategory(Text.empty()).addEntry(标签样式.build());
                SubCategoryBuilder 标识符样式 = 构建器.entryBuilder().startSubCategory(Text.translatable("identifier_translation.configure.identifier_style")).setExpanded(true);
                标识符样式.add(构建器.entryBuilder().startEnumSelector(Text.translatable("identifier_translation.configure.identifier_style.color"), ColorCode.class, Manager.配置项.标识符样式.颜色).setDefaultValue(ColorCode.天蓝色).setEnumNameProvider(ColorCode::颜色代码翻译).setSaveConsumer((新值) -> {
                    Manager.配置项.标识符样式.颜色 = 新值;
                }).build());
                标识符样式.add(构建器.entryBuilder().startBooleanToggle(Text.translatable("formatting_code.obfuscated"), Manager.配置项.标识符样式.随机字符).setDefaultValue(false).setSaveConsumer((新值) -> {
                    Manager.配置项.标识符样式.随机字符 = 新值;
                }).build());
                标识符样式.add(构建器.entryBuilder().startBooleanToggle(Text.translatable("formatting_code.bold"), Manager.配置项.标识符样式.粗体).setDefaultValue(false).setSaveConsumer((新值) -> {
                    Manager.配置项.标识符样式.粗体 = 新值;
                }).build());
                标识符样式.add(构建器.entryBuilder().startBooleanToggle(Text.translatable("formatting_code.strikethrough"), Manager.配置项.标识符样式.删除线).setDefaultValue(false).setSaveConsumer((新值) -> {
                    Manager.配置项.标识符样式.删除线 = 新值;
                }).build());
                标识符样式.add(构建器.entryBuilder().startBooleanToggle(Text.translatable("formatting_code.underline"), Manager.配置项.标识符样式.下划线).setDefaultValue(false).setSaveConsumer((新值) -> {
                    Manager.配置项.标识符样式.下划线 = 新值;
                }).build());
                标识符样式.add(构建器.entryBuilder().startBooleanToggle(Text.translatable("formatting_code.italic"), Manager.配置项.标识符样式.斜体).setDefaultValue(false).setSaveConsumer((新值) -> {
                    Manager.配置项.标识符样式.斜体 = 新值;
                }).build());
                标识符样式.add(构建器.entryBuilder().startBooleanToggle(Text.translatable("identifier_translation.configure.identifier_style.hover"), Manager.配置项.标识符样式.显示悬停文本).setDefaultValue(true).setSaveConsumer((新值) -> {
                    Manager.配置项.标识符样式.显示悬停文本 = 新值;
                }).build());
                构建器.getOrCreateCategory(Text.empty()).addEntry(标识符样式.build());
                return 构建器.build();
            } catch (NullPointerException e) {
                Main.记录器.warn(Text.translatable("identifier_translation.configure.warning").getString());
                Manager.配置项 = new Settings();
                Manager.保存();
                return getModConfigScreenFactory().create(上级界面);
            }
        };
    }
}
