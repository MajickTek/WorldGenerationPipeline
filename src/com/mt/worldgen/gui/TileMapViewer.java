package com.mt.worldgen.gui;

import javax.swing.*;

import com.mt.worldgen.generator.LayerGenerator;
import com.mt.worldgen.generator.LayerMap;
import com.mt.worldgen.generator.LayerSetting;
import com.mt.worldgen.generator.TileType;
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

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class TileMapViewer
{
	private static final Random random = new Random(System.currentTimeMillis());
	static BufferedImage image = new BufferedImage(256,256,BufferedImage.TYPE_INT_RGB);
	private static final int WIDTH=800;
	private static final int HEIGHT=600;
    public static void main(String[] args)
    {
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        for(int y = 0; y < 256; y++) {
        	for(int x = 0; x < 256; x++) {
        		int c = x ^ y;
        		image.setRGB(x, y, new Color(c,c,c).getRGB());
        	}
        }
        JFrame app = new JFrame("Tilemap Viewer");
        app.setIgnoreRepaint( true );
        app.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        Canvas canvas = new Canvas();
        canvas.setIgnoreRepaint(true);
        canvas.setSize(WIDTH, HEIGHT);

        JScrollPane jsp = new JScrollPane();
        
        jsp.setViewportView(canvas);
        
        JMenuBar jmb = new JMenuBar();
        
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(_ -> {
        	System.exit(0);
        });
        fileMenu.add(exitMenuItem);
        jmb.add(fileMenu);
        
        JMenu generateMenu = new JMenu("Generate");
        JMenuItem groundMenuItem = new JMenuItem("Ground");
        JMenuItem skyMenuItem = new JMenuItem("Sky");
        JMenuItem undergroundMenuItem = new JMenuItem("Underground");
        
        groundMenuItem.addActionListener(l -> {
        	LayerSetting groundLayerSetting = new LayerSetting(128, 128, 16, 0, random);
        	LayerGenerator groundGenerator = new LayerGenerator(groundLayerSetting);
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
        	image = makeMap(4, 4, groundMap);
        });
        
        skyMenuItem.addActionListener(l -> {
        	LayerSetting skyLayerSetting = new LayerSetting(128, 128, 16, 0, random);
        	LayerGenerator skyGenerator = new LayerGenerator(skyLayerSetting);
        	LayerMap skyMap = skyGenerator.create(skyLayerSetting,
            		List.of(count -> count[TileType.CLOUD.getID() & 0xff] < 2000,
            				count -> count[TileType.STAIRSDOWN.getID()] < 2),
            		Pipeline.create(new SkyNoiseStage())
            		.addStage(new CloudCactusStage())
            		.addStage(new SkyStairsStage()));
        	image = makeMap(4,4,skyMap);
        });
        
        undergroundMenuItem.addActionListener(l -> {
        	LayerSetting undergroundLayerSetting = new LayerSetting(128, 128, 16, 0, random);
        	LayerGenerator undergroundGenerator = new LayerGenerator(undergroundLayerSetting);
        	List<Predicate<int[]>> underGroundFilters = new ArrayList<Predicate<int[]>>();
        	underGroundFilters.addAll(List.of(count -> count[TileType.ROCK.getID() & 0xff] < 100,
					count -> count[TileType.DIRT.getID() & 0xff] < 100));
			if (undergroundLayerSetting.depth() < 3) {
				underGroundFilters.add(count -> count[TileType.STAIRSDOWN.getID() & 0xff] < 2);
			}
			LayerMap undergroundMap = undergroundGenerator.create(undergroundLayerSetting,
					underGroundFilters,
					Pipeline.create(new UndergroundNoiseStage())
					.addStage(new IronOreStage())
					.addStage(new UndergroundStairsStage()));
			image=makeMap(4,4,undergroundMap);
        });
        generateMenu.add(groundMenuItem);
        generateMenu.add(skyMenuItem);
        generateMenu.add(undergroundMenuItem);
        
        jmb.add(generateMenu);
        app.setJMenuBar(jmb);
        app.add(jsp);
        app.pack();
        app.setVisible(true);

        // Create BackBuffer...
        canvas.createBufferStrategy(2);
        BufferStrategy buffer = canvas.getBufferStrategy();

        // Get graphics configuration...
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        // Create off-screen drawing surface
        BufferedImage bi = gc.createCompatibleImage(WIDTH, HEIGHT);

        // Objects needed for rendering...
        Graphics graphics = null;
        Graphics2D g2d = null;
        Color background = Color.BLACK;
        Random rand = new Random();

        // Variables for counting frames per seconds
        int fps = 0;
        int frames = 0;
        long totalTime = 0;
        long curTime = System.currentTimeMillis();
        long lastTime = curTime;

        while(true)
        {
            try
            {
                // count Frames per second...
                lastTime = curTime;
                curTime = System.currentTimeMillis();
                totalTime += curTime - lastTime;
                if(totalTime > 1000)
                {
                    totalTime -= 1000;
                    fps = frames;
                    frames = 0;
                }
                ++frames;

            // clear back buffer...
            g2d = bi.createGraphics();
            g2d.setColor( background );
            g2d.fillRect( 0, 0, 639, 479 );

            g2d.drawImage(image, 0, 0, null);

            // display frames per second...
            g2d.setFont( new Font( "Courier New", Font.PLAIN, 12 ) );
            g2d.setColor( Color.GREEN );
            g2d.drawString( String.format( "FPS: %s", fps ), 20, 20 );

            // Blit image and flip...
            graphics = buffer.getDrawGraphics();
            graphics.drawImage(bi, 0, 0, null);
            if(!buffer.contentsLost())
              buffer.show();

            // Let the OS have a little time...
            Thread.yield();
          }
          finally
          {
            // release resources
            if( graphics != null )
                graphics.dispose();
            if( g2d != null )
                g2d.dispose();
            }
        }
    }
    
    public static BufferedImage makeMap(int heightScaleFactor, int widthScaleFactor, LayerMap layerMap){
        int width = layerMap.width();
        int height = layerMap.height();

        byte[] mapData = layerMap.mapData()[0];

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int i = x + y * width;

                if (mapData[i] == TileType.WATER.getID()) pixels[i] = TileType.WATER.getHexColor();
                if (mapData[i] == TileType.GRASS.getID()) pixels[i] = TileType.GRASS.getHexColor();
                if (mapData[i] == TileType.ROCK.getID()) pixels[i] = TileType.ROCK.getHexColor();
                if (mapData[i] == TileType.DIRT.getID()) pixels[i] = TileType.DIRT.getHexColor();
                if (mapData[i] == TileType.SAND.getID()) pixels[i] = TileType.SAND.getHexColor();
                if (mapData[i] == TileType.TREE.getID()) pixels[i] = TileType.TREE.getHexColor();
                if (mapData[i] == TileType.LAVA.getID()) pixels[i] = TileType.LAVA.getHexColor();
                if (mapData[i] == TileType.CLOUD.getID()) pixels[i] = TileType.CLOUD.getHexColor();
                if (mapData[i] == TileType.STAIRSDOWN.getID()) pixels[i] = TileType.STAIRSDOWN.getHexColor();
                if (mapData[i] == TileType.STAIRSUP.getID()) pixels[i] = TileType.STAIRSUP.getHexColor();
                if (mapData[i] == TileType.CLOUDCACTUS.getID()) pixels[i] = TileType.CLOUDCACTUS.getHexColor();
            }
        }

        img.setRGB(0, 0, width, height, pixels, 0, width);
        
        return toBufferedImage(img.getScaledInstance(width*widthScaleFactor, height*heightScaleFactor, Image.SCALE_AREA_AVERAGING));
    }
    
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bimage = new BufferedImage(
            img.getWidth(null),
            img.getHeight(null),
            BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bimage;
    }
}