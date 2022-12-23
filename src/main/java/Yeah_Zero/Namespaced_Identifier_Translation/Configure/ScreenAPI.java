package Yeah_Zero.Namespaced_Identifier_Translation.Configure;

public class ScreenAPI implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return Configuration::屏幕;
    }
}
