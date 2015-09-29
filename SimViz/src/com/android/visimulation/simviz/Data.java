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

	
}
