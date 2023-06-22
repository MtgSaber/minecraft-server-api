package com.saber_softworks.hosting.mc.configs;

import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.With;

@With
public record GeneralConfig(
    int apiPort,
    MinecraftServerConfig minecraftServer,
    SystemConfig system
) {
}
