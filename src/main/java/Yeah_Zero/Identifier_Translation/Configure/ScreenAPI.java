package Yeah_Zero.Identifier_Translation.Configure;

import Yeah_Zero.Identifier_Translation.ColorCode;
import Yeah_Zero.Identifier_Translation.Main;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ScreenAPI implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (上级界面) -> {
            try {
                ConfigBuilder 构建器 = ConfigBuilder.create().setParentScreen(上级界面).setTitle(Text.translatable("identifier-translation.configure.title")).setDoesConfirmSave(false).setSavingRunnable(Configuration::保存).setDefaultBackgroundTexture(new Identifier("minecraft", "textures/block/white_concrete.png"));
                构建器.getOrCreateCategory(Text.translatable("identifier-translation.configure.general")).addEntry(构建器.entryBuilder().startEnumSelector(Text.translatable("identifier-translation.configure.general.tag_color"), ColorCode.class, Configuration.配置项.标签颜色).setDefaultValue(ColorCode.金色).setSaveConsumer((新值) -> {
                    Configuration.配置项.标签颜色 = 新值;
                }).build()).addEntry(构建器.entryBuilder().startEnumSelector(Text.translatable("identifier-translation.configure.general.identifier_color"), ColorCode.class, Configuration.配置项.标识符颜色).setDefaultValue(ColorCode.天蓝色).setSaveConsumer((新值) -> {
                    Configuration.配置项.标识符颜色 = 新值;
                }).build());
                return 构建器.build();
            } catch (NullPointerException e) {
                Main.记录器.warn(Text.translatable("identifier-translation.configure.warning").getString());
                Configuration.配置项 = new Settings();
                Configuration.保存();
                return getModConfigScreenFactory().create(上级界面);
            }
        };
    }
}
