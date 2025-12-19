package com.mt.worldgen.generator.ground;

import com.mt.worldgen.generator.LayerMap;
import com.mt.worldgen.generator.LayerRatio;
import com.mt.worldgen.generator.SampleGenerator;
import com.mt.worldgen.generator.TileType;
import com.mt.worldgen.pipeline.ProcessingContext;
import com.mt.worldgen.pipeline.ProgressListener;
import com.mt.worldgen.pipeline.Stage;

public class GroundNoiseStage implements Stage {

	@Override
	public void execute(ProcessingContext context, ProgressListener listener) {
		LayerMap input = context.getMap();
		SampleGenerator mnoise1 = new SampleGenerator(input.setting(), 16);
		SampleGenerator mnoise2 = new SampleGenerator(input.setting(), 16);
		SampleGenerator mnoise3 = new SampleGenerator(input.setting(), 16);

		SampleGenerator noise1 = new SampleGenerator(input.setting(), 32);
		SampleGenerator noise2 = new SampleGenerator(input.setting(), 32);
		
		final int w = input.setting().width();
		final int h = input.setting().height();
		
		byte[] map = new byte[w * h];
		byte[] data = new byte[w * h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int i = x + y * w;

				double val = Math.abs(noise1.values[i] - noise2.values[i]) * 3 - 2;
				double mval = Math.abs(mnoise1.values[i] - mnoise2.values[i]);
				mval = Math.abs(mval - mnoise3.values[i]) * 3 - 2;

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

				if (val < LayerRatio.GROUND.NOISE_WATER_LOWER) {
					map[i] = TileType.WATER.getID();
				} else if (val > LayerRatio.GROUND.NOISE_ROCK_HIGHER && mval < LayerRatio.GROUND.NOISE_ROCK_LOWER) {
					map[i] = TileType.ROCK.getID();
				} else {
					map[i] = TileType.GRASS.getID();
				}
			}
		}
		input.mapData()[0]=map;
		input.mapData()[1]=data;
		context.advanceStage();
		listener.onProgressUpdate(context.getCurrentStageIndex(), context.getTotalStages(), getName(), "Completed Ground base terrain");
	}

	@Override
	public String getName() {
		return "Ground Noise";
	}

}
