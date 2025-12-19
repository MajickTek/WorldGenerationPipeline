package com.mt.worldgen.pipeline.test;

public class ProcessingContext {
	// TODO: make this class abstract?
	private Object input;
	// private O output;
	private int currentStageIndex = 0;
	private final int totalStages;

	// other data stored here?

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

	public int getCurrentStageIndex() {
		return currentStageIndex;
	}
	
	public int getTotalStages() {
		return totalStages;
	}
	// maybe add other getters/setters for other included data
}
