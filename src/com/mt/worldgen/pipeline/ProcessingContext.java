package com.mt.worldgen.pipeline;

import com.mt.worldgen.generator.LayerMap;

public class ProcessingContext {
	// TODO: make this class abstract?
	private LayerMap input;
	// private O output;
	private int currentStageIndex = 0;
	private final int totalStages;

	// other data stored here?

	public ProcessingContext(LayerMap input, int totalStages) {
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
	
	public LayerMap getMap() {
		return input;
	}
	// maybe add other getters/setters for other included data
}
