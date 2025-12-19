package com.mt.worldgen.generator;

import java.util.ArrayList;
import java.util.List;

// ====================================================
// TileMapGenerator Copyright(C) 2018 Furkan Türkal
// This program comes with ABSOLUTELY NO WARRANTY; This is free software,
// and you are welcome to redistribute it under certain conditions; See
// file LICENSE, which is part of this source code package, for details.
// ====================================================

import java.util.Random;
import java.util.function.Predicate;

import com.mt.worldgen.generator.ground.CactusStage;
import com.mt.worldgen.generator.ground.FlowerStage;
import com.mt.worldgen.generator.ground.GroundNoiseStage;
import com.mt.worldgen.generator.ground.SandStage;
import com.mt.worldgen.generator.ground.StairsStage;
import com.mt.worldgen.generator.ground.TreeStage;
import com.mt.worldgen.generator.sky.CloudCactusStage;
import com.mt.worldgen.generator.sky.SkyNoiseStage;
import com.mt.worldgen.generator.sky.SkyStairsStage;
import com.mt.worldgen.generator.underground.IronOreStage;
import com.mt.worldgen.generator.underground.UndergroundNoiseStage;
import com.mt.worldgen.generator.underground.UndergroundStairsStage;
import com.mt.worldgen.pipeline.Pipeline;

public final class TileMapGenerator {

    private static final Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {

        LayerSetting skyLayerSetting = new LayerSetting(128, 128, 16, 0, random);
        LayerSetting groundLayerSetting = new LayerSetting(128, 128, 16, 0, random);
        LayerSetting undergroundLayerSetting = new LayerSetting(128, 128, 16, 0, random);

        LayerGenerator skyGenerator = new LayerGenerator(skyLayerSetting);
        LayerGenerator groundGenerator = new LayerGenerator(groundLayerSetting);
        LayerGenerator undergroundGenerator = new LayerGenerator(undergroundLayerSetting);

        int widthScaleFactor = 4;
        int heightScaleFactor = 4;

        int attempt = 1;

        while (true){
            LayerMap skyMap = skyGenerator.create(skyLayerSetting,
            		List.of(count -> count[TileType.CLOUD.getID() & 0xff] < 2000,
            				count -> count[TileType.STAIRSDOWN.getID()] < 2),
            		Pipeline.create(new SkyNoiseStage())
            		.addStage(new CloudCactusStage())
            		.addStage(new SkyStairsStage()));
            
            LayerMap groundMap = groundGenerator.create(groundLayerSetting,
            		List.of(count -> count[TileType.ROCK.getID() & 0xff] < 100,
            				count -> count[TileType.SAND.getID() & 0xff] < 100,
            				count -> count[TileType.GRASS.getID() & 0xff] < 100,
            				count -> count[TileType.TREE.getID() & 0xff] < 100,
            				count -> count[TileType.STAIRSDOWN.getID() & 0xff] < 2),
            		Pipeline.create(new GroundNoiseStage())
            		.addStage(new SandStage())
            		//.addStage(new DirtStage())
            		.addStage(new TreeStage())
            		.addStage(new FlowerStage())
            		.addStage(new CactusStage())
            		.addStage(new StairsStage()));
            
            List<Predicate<int[]>> underGroundFilters = new ArrayList<Predicate<int[]>>();

            underGroundFilters.addAll(List.of(count -> count[TileType.ROCK.getID() & 0xff] < 100,
					count -> count[TileType.DIRT.getID() & 0xff] < 100));
			if (undergroundLayerSetting.depth() < 3) {
				underGroundFilters.add(count -> count[TileType.STAIRSDOWN.getID() & 0xff] < 2);
			}

            LayerMap undergroundMap = undergroundGenerator.create(undergroundLayerSetting, underGroundFilters, Pipeline.create(new UndergroundNoiseStage()).addStage(new IronOreStage()).addStage(new UndergroundStairsStage()));
            
            TileMapViewer.viewMap("SKY - Attempt: " + attempt, heightScaleFactor, widthScaleFactor, skyMap);
            TileMapViewer.viewMap("GROUND - Attempt: " + attempt, heightScaleFactor, widthScaleFactor, groundMap);
            TileMapViewer.viewMap("UNDERGROUND - Attempt: " + attempt, heightScaleFactor, widthScaleFactor, undergroundMap);

            attempt++;
        }
    }

}
