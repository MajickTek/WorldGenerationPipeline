package com.mt.worldgen.generator.sky;

import java.util.Random;

import com.mt.worldgen.generator.LayerMap;
import com.mt.worldgen.generator.LayerRatio;
import com.mt.worldgen.generator.TileType;
import com.mt.worldgen.pipeline.Handler;

public class SkyStairsHandler implements Handler<LayerMap, LayerMap> {

	@Override
	public LayerMap process(LayerMap input) {
		final Random random = input.setting().random();
		final int w = input.getWidth();
		final int h = input.getHeight();
		byte[] map = input.mapData()[0].clone();
		
		int count = 0;
		stairsLoop: for (int i = 0; i < w * h; i++) {
			int x = random.nextInt(w - 2) + 1;
			int y = random.nextInt(h - 2) + 1;

			for (int yy = y - 1; yy <= y + 1; yy++)
				for (int xx = x - 1; xx <= x + 1; xx++) {
					if (map[xx + yy * w] != TileType.CLOUD.getID())
						continue stairsLoop;
				}

			map[x + y * w] = TileType.STAIRSDOWN.getID();
			count++;
			if (count == LayerRatio.SKY.MAX_STAIRS_COUNT)
				break;
		}
		return new LayerMap(input.setting(), new byte[][] {map,input.mapData()[1].clone()});
	}

}
