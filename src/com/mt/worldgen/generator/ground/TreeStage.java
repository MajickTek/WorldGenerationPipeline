package com.mt.worldgen.generator.ground;

import java.util.Random;

import com.mt.worldgen.generator.LayerMap;
import com.mt.worldgen.generator.LayerRatio;
import com.mt.worldgen.generator.TileType;
import com.mt.worldgen.pipeline.ProcessingContext;
import com.mt.worldgen.pipeline.ProgressListener;
import com.mt.worldgen.pipeline.Stage;

public class TreeStage implements Stage {

	@Override
	public void execute(ProcessingContext context, ProgressListener listener) {
		final LayerMap input = context.getMap();
		final Random random = input.setting().random();
		final int w = input.width();
		final int h = input.height();
		byte[] map = input.mapData()[0].clone();
		
		for (int i = 0; i < w * h / LayerRatio.GROUND.RATIO_TREE; i++) {
			int x = random.nextInt(w);
			int y = random.nextInt(h);
			for (int j = 0; j < 200; j++) {
				int xx = x + random.nextInt(15) - random.nextInt(15);
				int yy = y + random.nextInt(15) - random.nextInt(15);
				if (xx >= 0 && yy >= 0 && xx < w && yy < h) {
					if (map[xx + yy * w] == TileType.GRASS.getID()) {
						map[xx + yy * w] = TileType.TREE.getID();
					}
				}
			}
		}
		input.mapData()[0]=map;
		context.advanceStage();
		listener.onProgressUpdate(context.getCurrentStageIndex(), context.getTotalStages(),"GROUND", getName(), "Generated trees");
	}

	@Override
	public String getName() {
		return "Trees";
	}

}
