package com.mt.worldgen.pipeline.test;

public interface Stage {
  void execute(ProcessingContext context, ProgressListener listener);
  String getName();
}
