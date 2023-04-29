package Yeah_Zero.Identifier_Translation;

import Yeah_Zero.Identifier_Translation.Configure.Configuration;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
    public static final Logger 记录器 = LoggerFactory.getLogger("identifier-translation");

    @Override
    public void onInitialize() {
        Configuration.加载();
    }
}
