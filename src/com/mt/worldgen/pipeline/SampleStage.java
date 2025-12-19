package com.mt.worldgen.pipeline;

public class SampleStage implements Stage {
  @Override
  public String getName() {
    return "Sample";
  }

  @Override
  public void execute(ProcessingContext context, ProgressListener listener) {
        
        System.out.println("This is a sample step");
        
        context.advanceStage();
        listener.onProgressUpdate(context.getCurrentStageIndex(), context.getTotalStages(), getName(), "Completed");
  }
}
