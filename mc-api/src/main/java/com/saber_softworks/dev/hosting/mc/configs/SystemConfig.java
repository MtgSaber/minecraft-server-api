package com.saber_softworks.dev.hosting.mc.configs;

import lombok.With;

@With
public record SystemConfig(
        int coreThreadPoolSize,
        int threadTimeoutMillis,
        boolean isThreadPoolFair
) {

}
