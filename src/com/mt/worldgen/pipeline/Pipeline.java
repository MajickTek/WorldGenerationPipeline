package com.mt.worldgen.pipeline;

import java.util.ArrayList;
import java.util.List;

import com.mt.worldgen.generator.LayerMap;
import com.mt.worldgen.generator.LayerSetting;

public class Pipeline {
  private final List<Stage> stages = new ArrayList<>();

  private Pipeline(Stage initialStage) {
	  addStage(initialStage);
  }
  public Pipeline addStage(Stage stage) {
    stages.add(stage);
    return this;
  }

  public void execute(LayerMap initialInput, ProgressListener listener) {
    ProcessingContext context = new ProcessingContext(initialInput, stages.size());
    for(Stage stage: stages) {
      stage.execute(context, listener);
    }
  }
  
  public static Pipeline create(Stage initialStage) {
	  return new Pipeline(initialStage);
  }
}
