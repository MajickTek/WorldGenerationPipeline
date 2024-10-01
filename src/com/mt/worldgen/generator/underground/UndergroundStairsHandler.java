package com.mt.worldgen.generator.underground;

import java.util.Random;

import com.mt.worldgen.generator.LayerMap;
import com.mt.worldgen.generator.LayerRatio;
import com.mt.worldgen.generator.TileType;
import com.mt.worldgen.pipeline.Handler;

public class UndergroundStairsHandler implements Handler<LayerMap, LayerMap> {

	@Override
	public LayerMap process(LayerMap input) {
		final Random random = input.setting().random();
		final int w = input.setting().width();
		final int h = input.setting().height();
		byte[] map = input.mapData()[0].clone();
		if (input.setting().depth() < 3) {
			int count = 0;
			stairsLoop: for (int i = 0; i < w * h / 100; i++) {
				int x = random.nextInt(w - 20) + 10;
				int y = random.nextInt(h - 20) + 10;

				for (int yy = y - 1; yy <= y + 1; yy++)
					for (int xx = x - 1; xx <= x + 1; xx++) {
						if (map[xx + yy * w] != TileType.ROCK.getID())
							continue stairsLoop;
					}

				map[x + y * w] = TileType.STAIRSDOWN.getID();
				count++;
				if (count == LayerRatio.UNDERGROUND.MAX_STAIRS_COUNT)
					break;
			}
		}
		return new LayerMap(input.setting(), new byte[][] {map,input.mapData()[1].clone()});
	}

}
