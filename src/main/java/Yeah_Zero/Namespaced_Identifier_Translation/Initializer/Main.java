package Yeah_Zero.Namespaced_Identifier_Translation.Initializer;

import Yeah_Zero.Namespaced_Identifier_Translation.Configure.Configuration;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
    @Override
    public void onInitialize() {
        Configuration.加载();
    }
}
