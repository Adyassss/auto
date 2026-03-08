package api.configs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    private final Properties properties = new Properties();
    public static final String ADMIN_USERNAME_KEY = "admin.username";
    public static final String ADMIN_PASSWORD_KEY = "admin.password";

    private Config(){
        try(InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")){
            if(input == null){
                throw new RuntimeException("config.properties not found");
            }
            properties.load(input);
        }
        catch (IOException e){
            throw new RuntimeException("config.properties not load");
        }
    }

    public static String getProperty (String key){
        String systemProperty = System.getProperty(key);
        if (systemProperty != null && !systemProperty.isBlank()) {
            return systemProperty;
        }

        String envKey = key.toUpperCase().replace('.', '_');
        String envProperty = System.getenv(envKey);
        if (envProperty != null && !envProperty.isBlank()) {
            return envProperty;
        }

        return INSTANCE.properties.getProperty(key);
    }
}
