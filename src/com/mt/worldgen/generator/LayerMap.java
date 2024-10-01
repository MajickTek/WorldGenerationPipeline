package com.mt.worldgen.generator;

// ====================================================
// TileMapGenerator Copyright(C) 2018 Furkan Türkal
// This program comes with ABSOLUTELY NO WARRANTY; This is free software,
// and you are welcome to redistribute it under certain conditions; See
// file LICENSE, which is part of this source code package, for details.
// ====================================================


public record LayerMap(LayerSetting setting, byte[][] mapData) {
	public int getHeight() {
		return setting.height();
	}
	
	public int getWidth() {
		return setting.width();
	}
}
