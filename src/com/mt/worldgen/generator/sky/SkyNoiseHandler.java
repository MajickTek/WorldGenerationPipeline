package com.mt.worldgen.generator.sky;

import com.mt.worldgen.generator.LayerMap;
import com.mt.worldgen.generator.LayerRatio;
import com.mt.worldgen.generator.LayerSetting;
import com.mt.worldgen.generator.SampleGenerator;
import com.mt.worldgen.generator.TileType;
import com.mt.worldgen.pipeline.Handler;

public class SkyNoiseHandler implements Handler<LayerSetting, LayerMap> {

	@Override
	public LayerMap process(LayerSetting input) {
		SampleGenerator noise1 = new SampleGenerator(input, 8);
		SampleGenerator noise2 = new SampleGenerator(input, 8);
		final int w = input.width();
		final int h = input.height();
		
		byte[] map = new byte[w * h];
		byte[] data = new byte[w * h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int i = x + y * w;

				double val = Math.abs(noise1.values[i] - noise2.values[i]) * 3 - 2;

				double xd = x / (w - 1.0) * 2 - 1;
				double yd = y / (h - 1.0) * 2 - 1;
				if (xd < 0)
					xd = -xd;
				if (yd < 0)
					yd = -yd;
				double dist = xd >= yd ? xd : yd;
				dist = dist * dist * dist * dist;
				dist = dist * dist * dist * dist;
				val = -val * 1 - 2.2;
				val = val + 1 - dist * 20;

				if (val < LayerRatio.SKY.NOISE_INFINITEFALL_LOWER) {
					map[i] = TileType.INFINITEFALL.getID();
				} else {
					map[i] = TileType.CLOUD.getID();
				}
			}
		}
		return new LayerMap(input, new byte[][] {map,data});
	}

}
