package Yeah_Zero.Identifier_Translation.Initializer;

import Yeah_Zero.Identifier_Translation.Configure.Manager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
    public static final Logger 记录器 = LoggerFactory.getLogger("identifier_translation");

    @Override
    public void onInitialize() {
        Manager.加载();
    }
}
