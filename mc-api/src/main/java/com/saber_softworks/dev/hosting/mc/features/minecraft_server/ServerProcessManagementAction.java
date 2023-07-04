package com.saber_softworks.dev.hosting.mc.features.minecraft_server;

import com.saber_softworks.dev.hosting.mc.features.minecraft_server.process.MinecraftServerProcessState;
import lombok.NonNull;
import lombok.With;

import java.util.UUID;

public sealed interface ServerProcessManagementAction {
    sealed interface Request extends ServerProcessManagementAction {
        @NonNull UUID uuid();
    }
    record Initialize(@NonNull UUID uuid, String configYaml) implements Request {}
    record Start(@NonNull UUID uuid) implements Request {}
    record Stop(@NonNull UUID uuid) implements Request {}
    record ExportServerData(@NonNull UUID uuid) implements Request {}


    sealed interface Response extends ServerProcessManagementAction {
        @NonNull Request request();
        @NonNull MinecraftServerProcessState currentProcessState();
    }
    record Rejected(@NonNull Request request, @NonNull MinecraftServerProcessState currentProcessState) implements Response {}
    record Accepted(@NonNull Request request, @NonNull MinecraftServerProcessState currentProcessState) implements Response {}

    @With record ResponseAck(@NonNull Response response) {}

    @With record ChangeNotification(
            @NonNull MinecraftServerProcessState state
    ) implements ServerProcessManagementAction {}
}
