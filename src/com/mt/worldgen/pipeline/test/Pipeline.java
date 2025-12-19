import java.util.ArrayList;
import java.util.List;

public class Pipeline {
  private final List<Stage> stages = new ArrayList<>();

  public Pipeline addStage(Stage stage) {
    stages.add(stage);
    return this;
  }

  public void execute(Object initialInput, ProgressListener listener) {
    ProcessingContext context = new ProcessingContext(initialInput, stages.size());
    for(Stage stage: stages) {
      stage.execute(context, listener);
    }
  }
}
