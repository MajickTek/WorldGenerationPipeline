package com.mt.worldgen.pipeline.test;

import java.text.MessageFormat;
public class ConsoleProgressMonitor implements ProgressListener {
  @Override
  public void onProgressUpdate(int currentStageIndex, int totalStages, String stageName, String status) {
    double percentage = (double) currentStageIndex / totalStages * 100;
    String statusMessage = MessageFormat.format("{0}% step {1}/{2}: {3}",percentage,currentStageIndex,totalStages,status);
    System.out.println(statusMessage);
  }
}
