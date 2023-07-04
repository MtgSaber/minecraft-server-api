package com.saber_softworks.dev.hosting.mc.features.minecraft_server;

import com.saber_softworks.dev.hosting.mc.features.minecraft_server.process.MinecraftServerProcessState;
import com.saber_softworks.dev.lib.common.vavr.Maps;
import com.saber_softworks.dev.lib.common.vavr.Sets;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import lombok.NonNull;
import lombok.With;

import java.util.UUID;

@With
public record ServerProcessManagementState(
        @NonNull MinecraftServerProcessState currentProcessState,
        @NonNull Set<UUID> openRequests,
        @NonNull Map<UUID, ServerProcessManagementAction.Response> unacknowledgedResponses
) {
    public ServerProcessManagementState initial() {
        return new ServerProcessManagementState(
                MinecraftServerProcessState.NotInitialized.instance,
                Sets.empty(),
                Maps.empty()
        );
    }
}
