package com.mt.worldgen.pipeline;


import java.util.Objects;

public class Pipeline<I,O> {
    private final Handler<I,O> currentHandler;
    
    public Pipeline(Handler<I,O> currentHandler) {
        this.currentHandler=Objects.requireNonNull(currentHandler);
       
    }

    
    public <K> Pipeline<I,K> addHandler(Handler<O,K> newHandler) {
        return new Pipeline<>(input -> {
            
            return Objects.requireNonNull(newHandler).process(currentHandler.process(input));
        });
    }
    
    public O execute(I input) {
        return currentHandler.process(input);
    }
    
    public static <I,O> Pipeline<I,O> create(Handler<I,O> currentHandler) {
    	return new Pipeline<>(currentHandler);
    }
    
}
