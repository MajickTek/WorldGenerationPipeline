public class ConsoleProgressMonitor implements ProgressListener {
  @Override
  public void onProgressUpdate(nt currentStageIndex, int totalStages, String stageName, String status) {
    double percentage = (double) currentStageIndex / totalStages * 100;
    System.out.println("\r[%.1f%%] Step %d/%d: %s", percentage, currentStageIndex, status);
  }
}
