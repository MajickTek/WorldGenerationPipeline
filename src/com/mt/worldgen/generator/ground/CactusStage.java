package com.mt.worldgen.generator.ground;

import java.util.Random;

import com.mt.worldgen.generator.LayerMap;
import com.mt.worldgen.generator.LayerRatio;
import com.mt.worldgen.generator.TileType;
import com.mt.worldgen.pipeline.ProcessingContext;
import com.mt.worldgen.pipeline.ProgressListener;
import com.mt.worldgen.pipeline.Stage;

public class CactusStage implements Stage {

	@Override
	public void execute(ProcessingContext context, ProgressListener listener) {
		final LayerMap input = context.getMap();
		final Random random = input.setting().random();
		final int w = input.getWidth();
		final int h = input.getHeight();
		byte[] map = input.mapData()[0].clone();
		for (int i = 0; i < w * h / LayerRatio.GROUND.RATIO_CACTUS; i++) {
			int xx = random.nextInt(w);
			int yy = random.nextInt(h);
			if (xx >= 0 && yy >= 0 && xx < w && yy < h) {
				if (map[xx + yy * w] == TileType.SAND.getID()) {
					map[xx + yy * w] = TileType.CACTUS.getID();
				}
			}
		}
		input.mapData()[0]=map;
		context.advanceStage();
		listener.onProgressUpdate(context.getCurrentStageIndex(), context.getTotalStages(),"GROUND", getName(), "Generated Cacti");
	}

	@Override
	public String getName() {
		return "Cactus";
	}

}
