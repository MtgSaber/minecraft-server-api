package com.saber_softworks.dev.hosting.mc.features;

import com.saber_softworks.dev.hosting.mc.features.minecraft_server.ServerProcessManagementState;
import lombok.NonNull;
import lombok.With;

@With
public record State(
    @NonNull ServerProcessManagementState processManagement
) {
    public static State initial() {
        return new State(
                ServerProcessManagementState.initial()
        );
    }
}
