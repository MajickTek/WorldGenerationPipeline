public interface ProgressListener {
  void onProgressUpdate(int currentStageIndex, int totalStages, String stageName, String status);
}
