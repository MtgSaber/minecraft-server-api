package com.saber_softworks.dev.hosting.mc.configs;

import lombok.With;

@With
public record GeneralConfig(
    int apiPort,
    MinecraftServerConfig minecraftServer,
    SystemConfig system
) {
}
