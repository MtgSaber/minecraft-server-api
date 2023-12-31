package com.saber_softworks.dev.hosting.mc.configs;

import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.With;

@With
public record MinecraftServerConfig(
        @NonNull String pathToServerDirectory,
        @NonNull String serverJarFilename,
        @NonNull Option<String> pathToJreIfNotDefaultJavaAlias,
        @NonNull Map<String, String> jvmArgs,
        @NonNull Seq<String> serverJarArgs
) {}
