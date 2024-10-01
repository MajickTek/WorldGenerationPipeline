package com.mt.worldgen.generator;

// ====================================================
// TileMapGenerator Copyright(C) 2018 Furkan Türkal
// This program comes with ABSOLUTELY NO WARRANTY; This is free software,
// and you are welcome to redistribute it under certain conditions; See
// file LICENSE, which is part of this source code package, for details.
// ====================================================

import java.util.Random;

public record LayerSetting(int width, int height, int stepSize, int depth, Random random) {

}
