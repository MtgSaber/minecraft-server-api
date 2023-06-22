package com.saber_softworks.hosting.mc.configs;

import lombok.With;

@With
public record SystemConfig(
        int coreThreadPoolSize,
        int threadTimeoutMillis,
        boolean isThreadPoolFair
) {

}
