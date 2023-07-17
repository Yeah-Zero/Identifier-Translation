package Yeah_Zero.Identifier_Translation.Configure;

import Yeah_Zero.Identifier_Translation.Util.ColorCode;
import net.minecraft.text.Style;

public class Settings {
    public StyleSetting 标签样式 = new StyleSetting(ColorCode.金色);
    public StyleSetting 标识符样式 = new StyleSetting(ColorCode.天蓝色);

    public class StyleSetting {
        public ColorCode 颜色;
        public boolean 随机字符 = false;
        public boolean 粗体 = false;
        public boolean 删除线 = false;
        public boolean 下划线 = false;
        public boolean 斜体 = false;
        public boolean 显示悬停文本 = true;

        StyleSetting(ColorCode 颜色) {
            this.颜色 = 颜色;
        }

        public Style 生成样式() {
            return Style.EMPTY.withColor(颜色.获取格式代码()).withObfuscated(随机字符).withBold(粗体).withStrikethrough(删除线).withUnderline(下划线).withItalic(斜体);
        }
    }
}
