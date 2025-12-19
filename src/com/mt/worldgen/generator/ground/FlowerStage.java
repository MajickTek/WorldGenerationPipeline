package com.mt.worldgen.generator.ground;

import java.util.Random;

import com.mt.worldgen.generator.LayerMap;
import com.mt.worldgen.generator.LayerRatio;
import com.mt.worldgen.generator.TileType;
import com.mt.worldgen.pipeline.ProcessingContext;
import com.mt.worldgen.pipeline.ProgressListener;
import com.mt.worldgen.pipeline.Stage;

public class FlowerStage implements Stage {

	@Override
	public void execute(ProcessingContext context, ProgressListener listener) {
		final LayerMap input = context.getMap();
		final Random random = input.setting().random();
		final int w = input.getWidth();
		final int h = input.getHeight();
		
		byte[] map = input.mapData()[0].clone();
		byte[] data = input.mapData()[1].clone();
		
		for (int i = 0; i < w * h / LayerRatio.GROUND.RATIO_FLOWER; i++) {
			int x = random.nextInt(w);
			int y = random.nextInt(h);
			int col = random.nextInt(4);
			for (int j = 0; j < 30; j++) {
				int xx = x + random.nextInt(5) - random.nextInt(5);
				int yy = y + random.nextInt(5) - random.nextInt(5);
				if (xx >= 0 && yy >= 0 && xx < w && yy < h) {
					if (map[xx + yy * w] == TileType.GRASS.getID()) {
						map[xx + yy * w] = TileType.FLOWER.getID();
						data[xx + yy * w] = (byte) (col + random.nextInt(4) * 16);
					}
				}
			}
		}
		input.mapData()[0]=map;
		input.mapData()[1]=data;
		context.advanceStage();
		listener.onProgressUpdate(context.getCurrentStageIndex(), context.getTotalStages(),"GROUND", getName(), "Generated flowers");
	}

	@Override
	public String getName() {
		return "Flower";
	}

}
