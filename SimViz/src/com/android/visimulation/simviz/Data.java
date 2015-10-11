package com.android.visimulation.simviz;

public class Data {


	// Diabetic Retina vetex and texture coordinates
	protected static final float dbRet_VertexCoords[]={
			0.0f,  0.0f,0.5f,0.5f,
			-1.0f, -1.0f,0f, 0f, 
			1.0f, -1.0f,1f, 0f, 
			1.0f,  1.0f,1f, 1f, 
			-1.0f,  1.0f,0f, 1f,
			-1.0f, -1.0f,0f, 0f
	};
	// camera texture vertex coordinate
	protected static final float[] camera_preview_vertex_coordinates = {
			1.0f,-1.0f,
			-1.0f,-1.0f,
			1.0f, 1.0f,
			-1.0f, 1.0f
	};

	// camera preivew texture coordinates
	protected static final float[] camera_preview_texture_coordinates = {	
			1.0f, 1.0f, 
			0.0f, 1.0f, 
			1.0f, 0.0f, 
			0.0f, 0.0f
	};

	protected static final float[][] quadVertices ={

			{
				0.25f,-0.85f,
				0.05f, -0.90f,
				0.50f, -0.90f, 
				0.50f,-0.70f,
				0.05f,-0.70f,
				0.05f,-0.90f
			},
			{
				.35f,.85f,
				.15f,.65f,
				.55f,.65f,
				.55f,.95f,
				.15f,.95f,
				.15f,.65f
			},
			{
				-0.7f,0.7f,
				-0.9f,0.5f,
				-0.5f,0.5f,
				-0.5f,0.9f,
				-0.9f,0.9f,
				-0.9f,0.5f

			},
			{
				-.5f,.0f,
				-.7f,-.2f,
				-.3f,-.2f,
				-.3f,.2f,
				-.7f,.2f,
				-.7f,-.2f
			},
			{
				-.85f,-.85f,
				-.99f,-.99f,
				-.65f,-.99f,
				-.65f,-.65f,
				-.99f,-.65f,
				-.99f,-.99f
			}


	};

	/*protected static final float[][] quadTextures = // black textures
					/*{
				{
					0.23f ,0.72f ,
					0.19f ,0.79f ,
					0.29f ,0.79f ,
					0.29f ,0.64f ,
					0.19f ,0.64f ,
					0.19f ,0.79f 
				},
				{
					0.30f ,0.39f ,
					0.26f ,0.43f ,
					0.32f ,0.43f ,
					0.33f ,0.34f ,
					0.28f ,0.35f ,
					0.26f ,0.43f 
				},
				{
					0.91f ,0.67f ,
					0.85f ,0.60f ,
					0.95f ,0.60f ,
					0.94f ,0.75f ,
					0.85f ,0.75f ,
					0.85f ,0.60f 
				},
				{
					0.51f ,0.22f ,
					0.48f ,0.17f ,
					0.55f ,0.17f ,
					0.55f ,0.27f ,
					0.47f ,0.27f ,
					0.48f ,0.17f 
				},
				{
					0.49f ,0.69f ,
					0.44f ,0.62f ,
					0.54f ,0.62f ,
					0.53f ,0.89f ,
					0.44f ,0.86f ,
					0.44f ,0.62f ,
				}
		};*/

	protected static float[][] quadTextures={  // white textures	
			{
				0.91f ,0.67f ,
				0.85f ,0.75f , 
				0.94f ,0.75f ,
				0.95f ,0.60f ,
				0.85f ,0.60f ,
				0.85f ,0.75f 
			},
			{
				0.92f ,0.70f ,
				0.85f ,0.81f ,
				0.98f ,0.81f ,
				0.98f ,0.55f ,
				0.82f ,0.56f ,
				0.85f ,0.81f 
			},
			{
				0.21f ,0.10f ,
				0.14f ,0.20f ,
				0.33f ,0.21f ,
				0.34f ,0.00f ,
				0.18f ,0.00f ,
				0.14f ,0.20f 
			},
			{
				0.60f ,0.64f ,
				0.44f ,0.80f ,
				0.66f ,0.80f ,
				0.66f ,0.47f ,
				0.48f ,0.46f ,
				0.44f ,0.80f 
			},
			{
				0.28f ,0.94f ,
				0.23f ,0.99f ,
				0.34f ,0.99f ,
				0.34f ,0.89f ,
				0.22f ,0.89f ,
				0.23f ,0.99f 
			},
	};	


	protected static final int[][] x_translationSeries= {
			{-1,1,-1,1},
			{-1,1,1,-1},
			{1,-1,1,-1},
			{1,0,-1,0},
			{0,1,0,-1}
	};
	protected static final int[][] y_translationSeries= {
			{1,0,-1,0},
			{-1,-1,1,1},
			{-1,0,1,0},
			{1,-1,1,-1},
			{1,0,-1,0}
	};

}
