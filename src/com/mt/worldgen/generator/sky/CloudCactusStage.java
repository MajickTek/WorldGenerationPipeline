package com.mt.worldgen.generator.sky;

import java.util.Random;

import com.mt.worldgen.generator.LayerMap;
import com.mt.worldgen.generator.LayerRatio;
import com.mt.worldgen.generator.TileType;
import com.mt.worldgen.pipeline.ProcessingContext;
import com.mt.worldgen.pipeline.ProgressListener;
import com.mt.worldgen.pipeline.Stage;

public class CloudCactusStage implements Stage {

	@Override
	public void execute(ProcessingContext context, ProgressListener listener) {
		LayerMap input = context.getMap();
		final Random random = input.setting().random();
		final int w = input.getWidth();
		final int h = input.getHeight();
		byte[] map = input.mapData()[0].clone();
		stairsLoop: for (int i = 0; i < w * h / LayerRatio.SKY.RATIO_CLOUDCACTUS; i++) {
			int x = random.nextInt(w - 2) + 1;
			int y = random.nextInt(h - 2) + 1;

			for (int yy = y - 1; yy <= y + 1; yy++)
				for (int xx = x - 1; xx <= x + 1; xx++) {
					if (map[xx + yy * w] != TileType.CLOUD.getID())
						continue stairsLoop;
				}

			map[x + y * w] = TileType.CLOUDCACTUS.getID();
		}
		input.mapData()[0]=map;
		context.advanceStage();
		listener.onProgressUpdate(context.getCurrentStageIndex(), context.getTotalStages(), getName(), "Created cloud Cacti");
	}

	@Override
	public String getName() {
		return "Cloud Cactus";
	}

}
