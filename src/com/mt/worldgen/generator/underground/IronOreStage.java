package com.mt.worldgen.generator.underground;

import java.util.Random;

import com.mt.worldgen.generator.LayerMap;
import com.mt.worldgen.generator.LayerRatio;
import com.mt.worldgen.generator.TileType;
import com.mt.worldgen.pipeline.ProcessingContext;
import com.mt.worldgen.pipeline.ProgressListener;
import com.mt.worldgen.pipeline.Stage;

public class IronOreStage implements Stage {

	@Override
	public void execute(ProcessingContext context, ProgressListener listener) {
		LayerMap input = context.getMap();
		final Random random = input.setting().random();
		final int w = input.getWidth();
		final int h = input.getHeight();
		byte[] map = input.mapData()[0].clone();
		int r = 2;
		for (int i = 0; i < w * h / LayerRatio.UNDERGROUND.RATIO_IRONORE; i++) {
			int x = random.nextInt(w);
			int y = random.nextInt(h);
			for (int j = 0; j < 30; j++) {
				int xx = x + random.nextInt(5) - random.nextInt(5);
				int yy = y + random.nextInt(5) - random.nextInt(5);
				if (xx >= r && yy >= r && xx < w - r && yy < h - r) {
					if (map[xx + yy * w] == TileType.ROCK.getID()) {
						map[xx + yy * w] = (byte) ((TileType.IRONORE.getID() & 0xff) + input.setting().depth() - 1);
					}
				}
			}
		}
		input.mapData()[0]=map;
	}

	@Override
	public String getName() {
		return "Iron Ore";
	}

}
