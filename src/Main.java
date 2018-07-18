/*
 * MIT License
 * Copyright (c) 2018 Luke Davison
 * See LICENSE for details.
 */

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application
{
	private Canvas canvas;
	
	private TextField txt_map_height, txt_map_width;
	private TextField txt_island_density, txt_island_size;
	
	private CheckBox chk_use_snow;

	private int MapWidth = 500;
	private int MapHeight = 500;

	private float IslandDensity = 1;
	private float IslandSize = 1;

	public static void main ( String args [] ) 
	{
		launch ( args );
	}

	@Override
	public void start ( Stage primaryStage ) throws Exception 
	{	
		primaryStage.setTitle ( "2D Terrain Generator" );

		canvas = new Canvas ( 500, 500 );				//	Draw Map To This

		Button btn_generate_map = new Button ( "Generate Map" );	//	Generate Random Map
		btn_generate_map.setOnAction ( new EventHandler <ActionEvent> () 
		{
			@Override
			public void handle ( ActionEvent event ) 
			{
				CreateMap ( canvas.getGraphicsContext2D() );
			}
		});
		
		Button btn_save_terrain = new Button ( "Save Terrain" );
		btn_save_terrain.setOnAction ( new EventHandler <ActionEvent> () {
			@Override
			public void handle ( ActionEvent event ) 
			{
				SaveToFile ( canvas, primaryStage );
			}
		});
		
		Label lbl_map_height = new Label ( "Map Height :" );
		txt_map_height = new TextField (  );
		
		Label lbl_map_width = new Label ( "Map Width :" );
		txt_map_width = new TextField (  );

		Label lbl_island_density = new Label ( "Island Density :" );
		txt_island_density = new TextField (  );
		
		Label lbl_island_size = new Label ( "Island Size :" );
		txt_island_size = new TextField (  );

		GridPane layout = new GridPane (  );
		layout.setHgap ( 10 );
		layout.setVgap ( 10 );
		layout.add ( canvas, 0, 0 );
		
		layout.add ( lbl_map_height, 2, 1 );
		layout.add ( txt_map_height, 3, 1 );
		
		layout.add ( lbl_map_width, 2, 2 );
		layout.add ( txt_map_width, 3, 2 );
		
		layout.add ( lbl_island_density, 2, 3 );
		layout.add ( txt_island_density, 3, 3 );
		
		layout.add ( lbl_island_size, 2, 4 );
		layout.add ( txt_island_size, 3, 4 );
		
		layout.add ( btn_generate_map, 3, 5 );
		layout.add ( btn_save_terrain, 3, 6 );

		//layout.setGridLinesVisible ( true );

		primaryStage.setScene ( new Scene ( layout, 1280, 720 ) );
		primaryStage.show ( );
	}

	private void CreateMap ( GraphicsContext gc ) 
	{
		gc.clearRect(0, 0, 500, 500);
		
		RetrieveProperties (  );

		Noise.Shuffle();

		double w = 500;
		double h = 500;

		for ( int x = 0; x < w; x++ )
			for ( int y = 0; y < h; y++ )
			{
				double dx = ( double ) x / w;
				double dy = ( double ) y / h;

				double val = IslandSize * Noise.Perlin( IslandDensity * dx, IslandDensity * dy ) 
						+ ( IslandSize ) * Noise.Perlin ( IslandDensity * 0.25, IslandDensity * 0.5 ) 
						+ ( IslandSize ) * Noise.Perlin( IslandDensity * 0.25, IslandDensity * 0.25 );
				
				val = ( val + 0.25 ) / 2;

				if ( val < 0.1 )
					gc.getPixelWriter().setColor(x, y, Color.AQUA);			//	Aqua Color = Water
				else if ( val < 0.2 )
					gc.getPixelWriter().setColor(x, y, Color.YELLOW);		//  Yellow Color = Sand
				else if ( val < 0.3 )
					gc.getPixelWriter().setColor(x, y, Color.GREENYELLOW);	//	Green Color = Grass
				else if ( val < 0.5 )
					gc.getPixelWriter().setColor(x, y, Color.DARKGREEN);
				else if ( val < 0.7 )
					gc.getPixelWriter().setColor(x, y, Color.SANDYBROWN);
				else if ( val < 0.8 )
					gc.getPixelWriter().setColor(x, y, Color.DARKGOLDENROD);
				else if ( val < 0.9 )
					gc.getPixelWriter().setColor( x, y, Color.SLATEGRAY); 	//	Slate Color = Rock
				else if ( val > 1.0 )
					gc.getPixelWriter().setColor(x, y, Color.GAINSBORO);
			}
	}
	
	private void RetrieveProperties (  ) 
	{
		if ( !txt_map_height.getText (  ).isEmpty (  ) )
			MapHeight = Integer.parseInt ( txt_map_height.getText (  ) );
		
		if ( !txt_map_width.getText (  ).isEmpty (  ) )
			MapWidth = Integer.parseInt ( txt_map_width.getText (  ) );
		
		if ( !txt_island_density.getText (  ).isEmpty (  ) )
			IslandDensity = Float.parseFloat( txt_island_density.getText (  ) );
		
		if ( !txt_island_size.getText (  ).isEmpty (  ) )
			IslandSize = Float.parseFloat ( txt_island_size.getText (  ) );
	}
	
	private void SaveToFile ( Canvas terrain_canvas, Stage primary_stage ) 
	{
		FileChooser file_chooser = new FileChooser (  );
		
		FileChooser.ExtensionFilter png_filter = new FileChooser.ExtensionFilter ( "PNG Files (*.png)", "*.png" );
		file_chooser.getExtensionFilters (  ).add ( png_filter );
		
		File file = file_chooser.showSaveDialog ( primary_stage );
		
		if ( file != null )
		{
			try 
			{
				WritableImage wi = new WritableImage ( MapWidth, MapHeight );
				terrain_canvas.snapshot ( null, wi );
				
				RenderedImage ri = SwingFXUtils.fromFXImage ( wi, null );
				ImageIO.write ( ri, "png", file );
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
