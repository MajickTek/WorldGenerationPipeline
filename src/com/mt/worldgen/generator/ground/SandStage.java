package com.mt.worldgen.generator.ground;

import java.util.Random;

import com.mt.worldgen.generator.LayerMap;
import com.mt.worldgen.generator.LayerRatio;
import com.mt.worldgen.generator.TileType;
import com.mt.worldgen.pipeline.ProcessingContext;
import com.mt.worldgen.pipeline.ProgressListener;
import com.mt.worldgen.pipeline.Stage;

public class SandStage implements Stage {

	@Override
	public void execute(ProcessingContext context, ProgressListener listener) {
		LayerMap input = context.getMap();
		final Random random = input.setting().random();
		final int w = input.getWidth();
		final int h = input.getHeight();
		
		byte[] map = input.mapData()[0].clone();
		
		for (int i = 0; i < w * h / LayerRatio.GROUND.RATIO_SAND; i++) {
			int xs = random.nextInt(w);
			int ys = random.nextInt(h);
			for (int k = 0; k < 10; k++) {
				int x = xs + random.nextInt(21) - 10;
				int y = ys + random.nextInt(21) - 10;
				for (int j = 0; j < 100; j++) {
					int xo = x + random.nextInt(5) - random.nextInt(5);
					int yo = y + random.nextInt(5) - random.nextInt(5);
					for (int yy = yo - 1; yy <= yo + 1; yy++)
						for (int xx = xo - 1; xx <= xo + 1; xx++)
							if (xx >= 0 && yy >= 0 && xx < w && yy < h) {
								if (map[xx + yy * w] == TileType.GRASS.getID()) {
									map[xx + yy * w] = TileType.SAND.getID();
								}
							}
				}
			}
		}
		input.mapData()[0]=map;
		context.advanceStage();
		listener.onProgressUpdate(context.getCurrentStageIndex(), context.getTotalStages(), getName(), "Generated sand");
	}

	@Override
	public String getName() {
		return "Sand";
	}

}
