package com.mt.worldgen.generator.ground;

import java.util.Random;

import com.mt.worldgen.generator.LayerMap;
import com.mt.worldgen.generator.LayerRatio;
import com.mt.worldgen.generator.TileType;
import com.mt.worldgen.pipeline.Handler;

public class TreeHandler implements Handler<LayerMap, LayerMap> {

	@Override
	public LayerMap process(LayerMap input) {
		final Random random = input.setting().random();
		final int w = input.getWidth();
		final int h = input.getHeight();
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
		return new LayerMap(input.setting(), new byte[][] {map,input.mapData()[1].clone()});
	}

}
