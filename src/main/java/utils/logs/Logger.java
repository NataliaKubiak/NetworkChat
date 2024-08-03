package utils.logs;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {

    private static Logger INSTANCE = null;

    private Logger() {
    }

    public static Logger getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Logger();
        }
        return INSTANCE;
    }

    public void log(String source, String action) {

        String str = LocalDateTime.now() + " | " + source.toUpperCase() + " | " + action + "\n";
        try (FileWriter writer = new FileWriter("log.txt", true)) {
            writer.write(str);
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
