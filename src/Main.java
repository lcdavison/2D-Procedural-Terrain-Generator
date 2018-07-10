/*
 * MIT License
 * Copyright (c) 2018 Luke Davison
 * See LICENSE for details.
*/

import java.awt.MouseInfo;
import java.awt.image.BufferedImage;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.input.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application
{
	private Canvas canvas;
	private ImageView view;
	
	private int MapWidth = 500;
	private int MapHeight = 500;
	
	private int IslandDensity = 5;
	private int IslandHeight = 2;
	
	public static void main ( String args [] ) 
	{
		launch ( args );
	}

	@Override
	public void start ( Stage primaryStage ) throws Exception 
	{	
		primaryStage.setTitle ( "2D Terrain Generator" );
		
		canvas = new Canvas ( 500, 500 );				//	Draw Map To This
		view = new ImageView ();
		view.setFitWidth(500);
		view.setFitHeight(500);
		
		Button generate_map = new Button ( "Generate Map" );	//	Generate Random Map
		generate_map.setOnAction ( new EventHandler <ActionEvent> () 
		{
			@Override
			public void handle(ActionEvent event) 
			{
				CreateMap ( canvas.getGraphicsContext2D() );
			}
		});
		
		GridPane layout = new GridPane (  );
		layout.setHgap ( 10 );
		layout.setVgap ( 10 );
		layout.add ( canvas, 1, 1 );
		layout.add ( generate_map, 2, 1);
		
		//layout.setGridLinesVisible ( true );
		
		primaryStage.setScene ( new Scene ( layout, 1280, 720 ) );
		primaryStage.show ( );
	}
	
	private void CreateMap ( GraphicsContext gc ) 
	{
		Noise.Shuffle();
		
		double w = 500;
		double h = 500;
		
		BufferedImage bi = new BufferedImage (500, 500, BufferedImage.TYPE_INT_RGB);
		
		for ( int x = 0; x < w; x++ )
			for ( int y = 0; y < h; y++ )
			{
				double dx = ( double ) x / w;
				double dy = ( double ) y / h;
				double val = IslandHeight * Noise.Perlin ( IslandDensity * dx, IslandDensity * dy );

				val = ( val - 1 ) / 2;
				
				if ( val < 0 )
					gc.getPixelWriter().setColor(x, y, Color.BLUE);		// Blue Color = Water
				else if ( val > 0.5 )
					gc.getPixelWriter().setColor(x, y, Color.GREEN);	//	Green Color = Grass
				else if ( val > 0 )
					gc.getPixelWriter().setColor(x, y, Color.YELLOW);	// Yellow Color = Sand
			}
		
		//Image image = SwingFXUtils.toFXImage(bi, null);
		//view.setImage(image);
	}
}
