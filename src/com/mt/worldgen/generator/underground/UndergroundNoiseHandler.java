package com.mt.worldgen.generator.underground;

import com.mt.worldgen.generator.LayerMap;
import com.mt.worldgen.generator.LayerRatio;
import com.mt.worldgen.generator.LayerSetting;
import com.mt.worldgen.generator.SampleGenerator;
import com.mt.worldgen.generator.TileType;
import com.mt.worldgen.pipeline.Handler;

public class UndergroundNoiseHandler implements Handler<LayerSetting, LayerMap> {

	@Override
	public LayerMap process(LayerSetting input) {
		final int w = input.width();
		final int h = input.height();
		SampleGenerator mnoise1 = new SampleGenerator(input, 16);
		SampleGenerator mnoise2 = new SampleGenerator(input, 16);
		SampleGenerator mnoise3 = new SampleGenerator(input, 16);

		SampleGenerator nnoise1 = new SampleGenerator(input, 16);
		SampleGenerator nnoise2 = new SampleGenerator(input, 16);
		SampleGenerator nnoise3 = new SampleGenerator(input, 16);

		SampleGenerator wnoise1 = new SampleGenerator(input, 16);
		SampleGenerator wnoise2 = new SampleGenerator(input, 16);
		SampleGenerator wnoise3 = new SampleGenerator(input, 16);

		SampleGenerator noise1 = new SampleGenerator(input, 32);
		SampleGenerator noise2 = new SampleGenerator(input, 32);
		
		byte[] map = new byte[w * h];
		byte[] data = new byte[w * h];
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int i = x + y * w;

				double val = Math.abs(noise1.values[i] - noise2.values[i]) * 3 - 2;

				double mval = Math.abs(mnoise1.values[i] - mnoise2.values[i]);
				mval = Math.abs(mval - mnoise3.values[i]) * 3 - 2;

				double nval = Math.abs(nnoise1.values[i] - nnoise2.values[i]);
				nval = Math.abs(nval - nnoise3.values[i]) * 3 - 2;

				double wval = Math.abs(wnoise1.values[i] - wnoise2.values[i]);
				wval = Math.abs(wval - wnoise3.values[i]) * 3 - 2;

				double xd = x / (w - 1.0) * 2 - 1;
				double yd = y / (h - 1.0) * 2 - 1;
				if (xd < 0)
					xd = -xd;
				if (yd < 0)
					yd = -yd;
				double dist = xd >= yd ? xd : yd;
				dist = dist * dist * dist * dist;
				dist = dist * dist * dist * dist;
				val = val + 1 - dist * 20;

				if (val > LayerRatio.UNDERGROUND.NOISE_LIQUID_HIGHER && wval < -2.0 + (input.depth()) / 2 * 3) {
					if (input.depth() > LayerRatio.UNDERGROUND.DEPTH_LAVA)
						map[i] = TileType.LAVA.getID();
					else
						map[i] = TileType.WATER.getID();
				} else if (val > LayerRatio.UNDERGROUND.NOISE_DIRT_HIGHER
						&& (mval < LayerRatio.UNDERGROUND.NOISE_DIRT_MVAL
								|| nval < LayerRatio.UNDERGROUND.NOISE_DIRT_NVAL)) {
					map[i] = TileType.DIRT.getID();
				} else {
					map[i] = TileType.ROCK.getID();
				}
			}
		}
		return new LayerMap(input, new byte[][] {map,data});
	}

}
