package com.mt.worldgen.generator.ground;

import java.util.Random;

import com.mt.worldgen.generator.LayerMap;
import com.mt.worldgen.generator.LayerRatio;
import com.mt.worldgen.generator.TileType;
import com.mt.worldgen.pipeline.Handler;

public class FlowerHandler implements Handler<LayerMap, LayerMap> {

	@Override
	public LayerMap process(LayerMap input) {
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
		return new LayerMap(input.setting(), new byte[][] {map,data});
	}

}
