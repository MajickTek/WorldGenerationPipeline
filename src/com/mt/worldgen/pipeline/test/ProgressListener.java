package com.mt.worldgen.pipeline.test;

public interface ProgressListener {
  void onProgressUpdate(int currentStageIndex, int totalStages, String stageName, String status);
}
