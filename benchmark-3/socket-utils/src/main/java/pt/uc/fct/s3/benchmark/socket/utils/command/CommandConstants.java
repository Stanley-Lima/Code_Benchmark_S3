package pt.uc.fct.s3.benchmark.socket.utils.command;

import java.rmi.MarshalException;
import java.util.UUID;

public final class CommandConstants {
    private CommandConstants() {
    }

    public static final String SPLIT_TOKEN = ";";
    private static final String COMMAND_FORMAT = ":connectionId" + SPLIT_TOKEN + ":correlationId" + SPLIT_TOKEN + ":comando" + SPLIT_TOKEN + ":payload";
    public static final String EMPTY = "";
    public static final String LF = "\n";

    public static String createCommandString(final UUID connectionId, final UUID correlationId, final Enum comando, final String payload) {
        return COMMAND_FORMAT
                .replace(":connectionId", connectionId.toString())
                .replace(":correlationId", correlationId == null ? EMPTY : correlationId.toString())
                .replace(":comando", comando.name())
                .replace(":payload", payload);
    }
}
