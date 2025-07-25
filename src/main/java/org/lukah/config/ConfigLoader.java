package org.lukah.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

public class ConfigLoader {

    private static final String configPath = getConfigPath();

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Settings.KeyBindings.class, new KeyMapper())
            .create();

    public static Settings load() throws IOException {

        try (FileReader reader = new FileReader(configPath)) {

            return gson.fromJson(reader, Settings.class);

        } catch (IOException | JsonIOException e) {

            System.err.println("Could not load settings config, loading defaults: " + e.getMessage());

            Settings settings = loadDefaults();
            save(settings);

            return settings;
        }
    }

    public static void save(Settings settings) { //will be used later to save user preferences

        try {
            Files.createDirectories(Paths.get(configPath).getParent());
        } catch (IOException e) {

            System.err.println("Failed to create config directory: " + e.getMessage());
            return;
        }

        try (FileWriter writer = new FileWriter(configPath)) {

            gson.toJson(settings, writer);

        } catch (IOException e) {

            System.err.println("Failed to save config: " + e.getMessage());
        }
    }

    public static Settings loadDefaults() throws IOException {

        // get from default location
        try (InputStream stream = ConfigLoader.class.getClassLoader().getResourceAsStream("defaults/config.json")) {

            if (stream == null) {
                throw new FileNotFoundException("Default file not found");
            }

            Reader reader = new InputStreamReader(stream);

            return gson.fromJson(reader, Settings.class);
        }
    }

    private static Path getProjectDir() {

        try {

            Path path = Paths.get(ConfigLoader.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI());

            if (path.toString().endsWith(".jar")) {

                return path.getParent();

            } else {

                // used for development environment
                return Paths.get(System.getProperty("user.dir")).getParent();
            }

        } catch (Exception e) {

            throw new RuntimeException("Could not get Project Directory: " + e);
        }
    }

    private static String getConfigPath() {

        Path projectPath = getProjectDir();

        Path configPath = projectPath
                .resolve("N-Body Simulation Configs")
                .resolve("settings.json");

        return configPath.toString();
    }
}
