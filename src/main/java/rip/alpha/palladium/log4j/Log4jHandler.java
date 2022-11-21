package rip.alpha.palladium.log4j;

import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.SneakyThrows;
import rip.alpha.palladium.PalladiumPlugin;

import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Getter
public class Log4jHandler {

    private final JsonParser parser;
    private final Executor executor;
    private final File file;

    private final String log4jStart;
    private final String log4jEnd;

    @SneakyThrows
    public Log4jHandler(PalladiumPlugin plugin) {
        this.parser = new JsonParser();
        this.executor = Executors.newSingleThreadExecutor();
        this.file = new File(plugin.getDataFolder(), "log4j.txt");

        if (!this.file.exists()) {
            plugin.getDataFolder().mkdirs();
            this.file.createNewFile();
        }

        this.log4jStart = "\\$\\{";
        this.log4jEnd = "}";

        System.setOut(new Log4jOutputStream(this, System.out));
    }

    public boolean isLog4jFormat(String message) {
        if (message == null) {
            return false;
        }
        String[] endSplit = message.split(this.log4jEnd);
        if (endSplit.length <= 0) {
            return false;
        }
        String[] startSplit = endSplit[0].split(this.log4jStart);
        if (startSplit.length <= 1) {
            return false;
        }
        String s = startSplit[Math.max(1, startSplit.length - 1)];
        if (s.isEmpty()) {
            return false;
        }
        return s.contains(":") || s.contains(".");
    }

    public void logToFile(String executor, String message) {
        this.executor.execute(() -> {
            try {
                FileWriter writer = new FileWriter(this.file, true);
                writer.write("Executor: " + executor + " Message: " + message + "\n");
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
