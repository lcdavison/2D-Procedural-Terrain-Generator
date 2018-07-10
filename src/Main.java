/*
 * MIT License
 * Copyright (c) 2018 Luke Davison
 * See LICENSE for details.
*/

import java.awt.MouseInfo;

import javafx.application.Application;
import javafx.scene.input.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application
{
	public static void main ( String args [] ) 
	{
		launch ( args );
	}

	@Override
	public void start ( Stage primaryStage ) throws Exception 
	{	
		primaryStage.setTitle ( "2D Terrain Generator" );
		
		Button generate_map = new Button ( "Generate Map" );
		generate_map.setLayoutX ( 150 );
		
		generate_map.setOnAction ( new EventHandler <ActionEvent> () 
		{
			@Override
			public void handle ( ActionEvent event ) 
			{
				System.out.println( Noise.Perlin ( MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY() ) );
			}
		} );
		
		StackPane sp = new StackPane (  );
		sp.getChildren (  ).add ( generate_map );
		
		primaryStage.setScene ( new Scene ( sp, 300, 300 ) );
		primaryStage.show ( );
	}
}
