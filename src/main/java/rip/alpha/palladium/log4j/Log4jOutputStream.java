package rip.alpha.palladium.log4j;

import java.io.OutputStream;
import java.io.PrintStream;

public class Log4jOutputStream extends PrintStream {
    private final Log4jHandler handler;

    public Log4jOutputStream(Log4jHandler handler, OutputStream out) {
        super(out);
        this.handler = handler;
    }

    @Override
    public void print(String message) {
        if (this.handler.isLog4jFormat(message)) {
            this.handler.logToFile("SystemOut", message);
            return;
        }
        super.print(message);
    }
}
