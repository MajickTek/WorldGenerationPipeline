package com.mt.worldgen.generator;

// ====================================================
// TileMapGenerator Copyright(C) 2018 Furkan Türkal
// This program comes with ABSOLUTELY NO WARRANTY; This is free software,
// and you are welcome to redistribute it under certain conditions; See
// file LICENSE, which is part of this source code package, for details.
// ====================================================

import javax.swing.*;

import com.mt.worldgen.pipeline.ConsoleProgressMonitor;
import com.mt.worldgen.pipeline.Pipeline;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;

public final class LayerGenerator {

	private LayerSetting m_layerSetting;

	public LayerGenerator(LayerSetting layerSetting) {
		this.m_layerSetting = layerSetting;
	}

	private boolean isPowerOfTwo(int number) {
		return number > 0 && ((number & (number - 1)) == 0);
	}


	public LayerMap create(LayerSetting setting, List<Predicate<int[]>> filters,
			Pipeline pipeline) {

		if (this.m_layerSetting.height() < 128) {
			JOptionPane.showMessageDialog(null, "Height must be least 128", "Warning", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}

		if (!this.isPowerOfTwo(this.m_layerSetting.height())) {
			JOptionPane.showMessageDialog(null, "Height must be power-of-two", "Warning",
					JOptionPane.INFORMATION_MESSAGE);
			return null;
		}

		if (this.m_layerSetting.width() < 128) {
			JOptionPane.showMessageDialog(null, "Width must be least 128", "Warning", JOptionPane.INFORMATION_MESSAGE);
			return null;
		}

		if (!this.isPowerOfTwo(this.m_layerSetting.width())) {
			JOptionPane.showMessageDialog(null, "Width must be power-of-two", "Warning",
					JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		List<LayerMap> failedMaps = new ArrayList<>();
		do {
			LayerMap map = new LayerMap(setting, new byte[2][2]);
			pipeline.execute(map, new ConsoleProgressMonitor());
			//LayerMap map = pipeline.execute(setting);
			if(failedMaps.contains(map)) continue;
			
			byte[][] result = map.mapData();
			
			//parallelize?
//			int[] count = IntStream.range(0, setting.width() * setting.height())
//				    .parallel()
//				    .map(i -> result[0][i] & 0xff)
//				    .collect(() -> new int[256], (arr, val) -> arr[val]++, (arr1, arr2) -> {
//				        for (int i = 0; i < arr1.length; i++) {
//				            arr1[i] += arr2[i];
//				        }
//				    });
			
			int[] count = new int[setting.width() * setting.height()];
			for (int i = 0; i < setting.width() * setting.height(); i++) {
				count[result[0][i] & 0xff]++;
			}
			Predicate<int[]> filter = filters.stream().reduce(_->true, Predicate::and);
			if (!filter.test(count))
				return map;
			failedMaps.add(map);
		} while (true);

	}
}
