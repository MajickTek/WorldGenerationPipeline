package com.mt.worldgen.pipeline;

public interface Handler<I,O> {
    O process(I input);
}
