package com.mt.worldgen.pipeline;

import java.text.MessageFormat;
public class ConsoleProgressMonitor implements ProgressListener {
  @Override
  public void onProgressUpdate(int currentStageIndex, int totalStages, String levelName, String stageName, String status) {
    double percentage = (double) currentStageIndex / totalStages * 100;
    String statusMessage = MessageFormat.format("[{0}] {1}% step {2}/{3}: {4}", levelName.toUpperCase(), (int)percentage,currentStageIndex,totalStages,status);
    System.out.println(statusMessage);
  }
}
