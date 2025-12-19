public class ProcessingContext{
  private Object input;
  //private O output;
  private int currentStageIndex = 0;
  private final int totalStages;

  //other data stored here?

  public ProcessingContext(Object input, int totalStages) {
    this.input = input;
    this.totalStages = totalStages;
  }

  public void advanceStage() {
    this.currentStageIndex++;
  }

  public int getCurrentProgress() {
    return (int) ((double) currentStageIndex / totalStages * 100);
  }

  //maybe add other getters/setters for other included data
}
