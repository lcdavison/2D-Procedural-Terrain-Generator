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
		
		layout.setGridLinesVisible ( true );
		
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
				double val = 3 * Noise.Perlin ( 3 * dx, 3 * dy );

				double r = val * 0xFF,
				g =  val * 0x0,
				b =  val * 0x0,
				a = r + g + b * 0xFF;
				
				byte red = (byte) ( (int) r & 0xFF ), green = (byte) ( (int) g & 0xFF ), blue = (byte) ( (int) b & 0xFF ), alpha = (byte) ( (int) a & 0xFF );
				int final_val = ( red << 24 ) | ( green << 16 ) | ( blue << 8 ) | ( alpha );
				
				gc.getPixelWriter().setArgb(x, y, final_val);
				
				//bi.setRGB(x, y, final_val);
			}
		
		//Image image = SwingFXUtils.toFXImage(bi, null);
		//view.setImage(image);
	}
}
