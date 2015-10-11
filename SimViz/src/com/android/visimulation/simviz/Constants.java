package com.android.visimulation.simviz;

public class Constants {

	protected static final int VIBRATION_TIME_TILT_MS = 200;
	
	protected static final int VIBRATION_TIME_MAGNET_MS= 75;

	protected static final int POSITION_COMPONENT_COUNT_CAMERA_TEXTURE=2; 

	protected static final int TEXTURE_COORD_COMPONENT_COUNT_CAMERA_TEXTURE=2;
	
	protected static final int POSITION_COMPONENT_COUNT_DBRETINA=2;
	
	protected static final int TEXTURE_COORD_COMPONENT_COUNT_DBRETINA=2;
	
	protected static final int DBRETINA_POSITION_OFFSET=0;
	
	protected static final int DBRETINA_TEXTURE_COORDS_OFFSET=POSITION_COMPONENT_COUNT_DBRETINA;

	protected static final int STRIDE_CAMERA_TEXTURE = 0;
	
	protected static final int DBRETINA_STRIDE = 4*(POSITION_COMPONENT_COUNT_DBRETINA +TEXTURE_COORD_COMPONENT_COUNT_DBRETINA);

	protected static final int TOTAL_SIMULATION_NUMBERS=9;

	protected static final float TRANSLATIONRATE=0.005f;
	protected static final int BYTES_PER_FLOAT=4;
}
