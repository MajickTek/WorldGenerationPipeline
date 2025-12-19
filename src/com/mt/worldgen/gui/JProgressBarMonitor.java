package com.mt.worldgen.gui;

import java.text.MessageFormat;

import javax.swing.JProgressBar;

import com.mt.worldgen.pipeline.ProgressListener;

public class JProgressBarMonitor implements ProgressListener {
	private final JProgressBar bar;
	public JProgressBarMonitor(JProgressBar bar) {
		this.bar=bar;
	}
	
	@Override
	public void onProgressUpdate(int currentStageIndex, int totalStages, String levelName, String stageName,
			String status) {
		double percentage = (double) currentStageIndex / totalStages * 100;
		int percentComplete = (int) percentage;
		
		bar.setValue(percentComplete);
		bar.setToolTipText(MessageFormat.format("[{0}] step {1}/{2}: {3}", levelName.toUpperCase(), currentStageIndex, totalStages, status));
	}

}
