package com.android.visimulation.simviz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.EyeTransform;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;

public class  SimViz extends CardboardActivity implements CardboardView.StereoRenderer, SurfaceTexture.OnFrameAvailableListener{

	private static final String TAG ="SimViz";
	private Vibrator mVibrator;
	private CardboardOverlayView mOverlayView;
	private CardboardView cardboardView;
	private final boolean description_on= false;
	
	private int[] hTex;
	
	private int dbretTex;
	
	private FloatBuffer pVertex;
	private FloatBuffer pTexCoord;
	
	private FloatBuffer dbret_pVertex;

	
	/*Variables for floaters */
	
		/*FloatBuffer for floater quads and textues*/
	private final FloatBuffer[] quads = new FloatBuffer[5];
	private final FloatBuffer[] quadTexs = new FloatBuffer[5];
		// floaters texture object ID.
	private int floaterTex; 
		// floaters vertex coordinates ref.
	private int f_aPositionLocation;
		// floaters texture coordinates ref.
	private int f_aTextureCoord;
		// floaters sampler2D uniform ref.
	private int f_uTextureLocation;
	
	private float[] x_translationSum = {0f,0f,0f,0f,0f};
	private float[] y_translationSum = {0f,0f,0f,0f,0f};
	
	private float[] translationTotal = {0f,0f,0f,0f,0f};
	private long time; 
	private int translationIndex=0;
	private int translationCoords;
	
	/*End of variables for floaters*/
	
	
	private int active_Program;
	private boolean mUpdateST = false;
	private int pass_Program, prota_Program,trita_Program, deuta_Program,cataracts_Program,glaucoma_Program,
			macdeg_Program,diabretina_texProgram,floaters_program;
	private int active =0;
	int[] progs = new int[Constants.TOTAL_SIMULATION_NUMBERS];
	String[] descriptions = new String[Constants.TOTAL_SIMULATION_NUMBERS];
	private Camera mCamera;
	private SurfaceTexture mSTexture;
	
	@Override
	public void onCreate(android.os.Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.common_ui); 
		
		// Get hold of our GLSurfaceView
		cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
		
		// render will be dealt with by cardboard;
		cardboardView.setRenderer(this);
		
		// Associate the cardboardView with this activity
		setCardboardView(cardboardView);
		
		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		mOverlayView = (CardboardOverlayView) findViewById(R.id.overlay); 
		
		mOverlayView.show3DToast("Visual Impairment Simulator. Pull trigger to cycle through impairments.",8000);
		
		pVertex = ByteBuffer.allocateDirect(Data.camera_preview_vertex_coordinates.length*4)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		pVertex.put(Data.camera_preview_vertex_coordinates);
		pVertex.position(0);
		
		// Allocating bytebuffers for position and textures
		
		pTexCoord = ByteBuffer.allocateDirect(Data.camera_preview_texture_coordinates.length*4)
					.order(ByteOrder.nativeOrder())
					.asFloatBuffer();
		pTexCoord.put(Data.camera_preview_texture_coordinates);
		pTexCoord.position(0);
		
		// Allocating bytebuffers  for diabetic retina;
		
		dbret_pVertex = ByteBuffer.allocateDirect(Data.dbRet_VertexCoords.length*4)
						.order(ByteOrder.nativeOrder())
						.asFloatBuffer();
		dbret_pVertex.put(Data.dbRet_VertexCoords);
		dbret_pVertex.position(0);
		
		
		/*Allocating byte buffers specific to  floaters*/
		for(int i=0;i<Data.quadTextures.length;i++){
			quads[i]=ByteBuffer.allocateDirect(Data.quadVertices[i].length*Constants.BYTES_PER_FLOAT)
					.order(ByteOrder.nativeOrder())
					.asFloatBuffer();
			quads[i].put(Data.quadVertices[i]);
			quadTexs[i]=ByteBuffer.allocateDirect(Data.quadTextures[i].length*Constants.BYTES_PER_FLOAT)
					.order(ByteOrder.nativeOrder())
					.asFloatBuffer();
			quadTexs[i].put(Data.quadTextures[i]);
			
		}
		
		

	}
	
	@Override
	public void onPause(){
		
		super.onPause();
	}
	
	
	@Override
	public void onResume(){
		super.onResume();
	}
	
	@Override
	public void onDestroy(){
		
		super.onDestroy();
		mUpdateST =false;
		mSTexture.release();
		mCamera.stopPreview();
		mCamera= null;
		deleteText();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e){
		
		int eventaction = e.getAction();
		switch(eventaction){
		
		case MotionEvent.ACTION_DOWN:
			// finger touches the screen;
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			onCardboardTrigger();
			break;
			
		}
		return true;
	}
	@Override
	public void onCardboardTrigger(){
		
		mVibrator.vibrate(Constants.VIBRATION_TIME_MAGNET_MS);
		//Log.i(TAG, "active #" + active);
		active++;
		if(active==progs.length)
			active=0;
		active_Program = progs[active];
		
		mOverlayView.show3DToast(descriptions[active],3000); 
		
	}

	public void onDrawEye(EyeTransform arg0) {
		
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		synchronized(this){
			if(mUpdateST){
				mSTexture.updateTexImage();
				mUpdateST=false;
			}
		}
		GLES20.glUseProgram(active_Program);
		
		int ph = GLES20.glGetAttribLocation(active_Program, "vPosition");
		int tch= GLES20.glGetAttribLocation(active_Program, "vTexCoord");
		int th = GLES20.glGetUniformLocation(active_Program, "sTexture");
		
		GLES20.glEnable(GLES11Ext.GL_TEXTURE_EXTERNAL_OES);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, hTex[0]);
		GLES20.glUniform1f(th, 0);
		
		GLES20.glVertexAttribPointer(ph,Constants.POSITION_COMPONENT_COUNT_CAMERA_TEXTURE,
				                     GLES20.GL_FLOAT,false,Constants.STRIDE_CAMERA_TEXTURE,pVertex);
		GLES20.glVertexAttribPointer(tch,Constants.TEXTURE_COORD_COMPONENT_COUNT_CAMERA_TEXTURE,
				                     GLES20.GL_FLOAT, false,Constants.STRIDE_CAMERA_TEXTURE, pTexCoord);
		
		GLES20.glEnableVertexAttribArray(ph);
		GLES20.glEnableVertexAttribArray(tch);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
		
		GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);

		if(active==4){/*if Diabetic Retinopathy */
			
			GLES20.glUseProgram(diabretina_texProgram);
			int dbret_position= GLES20.glGetAttribLocation(diabretina_texProgram, "dbRet_position");
			int dbret_textureCoordinate= GLES20.glGetAttribLocation(diabretina_texProgram, "dbRet_in_texCoord");
			int dbret_uniformLocation = GLES20.glGetUniformLocation(diabretina_texProgram, "dbRet_Texture");
			
			dbret_pVertex.position(0);
			GLES20.glVertexAttribPointer(dbret_position,Constants.POSITION_COMPONENT_COUNT_DBRETINA,
										GLES20.GL_FLOAT, false, Constants.DBRETINA_STRIDE, dbret_pVertex);
			GLES20.glEnableVertexAttribArray(dbret_position);
			
			dbret_pVertex.position(2);
			GLES20.glVertexAttribPointer(dbret_textureCoordinate,Constants.TEXTURE_COORD_COMPONENT_COUNT_DBRETINA,
					                     GLES20.GL_FLOAT, false,Constants.DBRETINA_STRIDE,dbret_pVertex);
			GLES20.glEnableVertexAttribArray(dbret_textureCoordinate);
			
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, dbretTex);
			GLES20.glUniform1i(dbret_uniformLocation, 0);
			
			GLES20.glEnable(GLES20.GL_BLEND);
			GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
			
			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6 );
		}
		if(active==5){ /*if floaters*/
			
			
			GLES20.glUseProgram(floaters_program);

			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, floaterTex);
			
			for(int i=0;i<5;i++){

				quads[i].position(0);
				GLES20.glVertexAttribPointer(f_aPositionLocation, 2, GLES20.GL_FLOAT,
						false, 0, quads[i]);

				quadTexs[i].position(0);
				GLES20.glVertexAttribPointer(f_aTextureCoord, 2, GLES20.GL_FLOAT,
						false, 0, quadTexs[i]);
				if(translationTotal[i]>=0.75) {
					time = SystemClock.uptimeMillis();
					translationIndex=(++translationIndex>3)?(0):(translationIndex);
					translationTotal[i]=0;
				}
				if(SystemClock.uptimeMillis()-time>=300){
					float x=Data.x_translationSeries[i][translationIndex]*Constants.TRANSLATIONRATE;
					float y=Data.y_translationSeries[i][translationIndex]*Constants.TRANSLATIONRATE;
					x_translationSum[i] +=x;
					y_translationSum[i] +=y;
					translationTotal[i] += (Data.x_translationSeries[i][translationIndex]==0) ? (Math.abs(y)):(Math.abs(x));
				}
				GLES20.glUniform2fv(translationCoords, 1, new float[]{x_translationSum[i],y_translationSum[i]},0);
				GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6);
			}

			
			
		}	
		GLES20.glFlush();
	}


	@Override
	public void onFinishFrame(Viewport arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNewFrame(HeadTransform arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRendererShutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSurfaceChanged(int arg0, int arg1) {
		// TODO Auto-generated method stub
		Camera.Parameters param = mCamera.getParameters();
		List<android.hardware.Camera.Size> sizes = param.getSupportedPreviewSizes();
		
		param.set("orientation", "landscape");
		// focus on INF lock white balance for
		param.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
		param.setAutoWhiteBalanceLock(true);
		param.setPreviewSize(sizes.get(0).width,sizes.get(0).height);
		mCamera.setParameters(param);
		mCamera.startPreview();
		
		
	}

	@Override
	public void onSurfaceCreated(EGLConfig arg0) {
		
		initTex();
		
		mSTexture = new SurfaceTexture(hTex[0]);
		
		mSTexture.setOnFrameAvailableListener(this);
		mCamera=Camera.open();
		
		try{
			mCamera.setPreviewTexture(mSTexture);
		}catch(IOException ioe){
			
		}
		// load all our shaders
		
		pass_Program = loadProgram(R.raw.pass_vertex,R.raw.pass_fragment);
		deuta_Program= loadProgram(R.raw.pass_vertex, R.raw.deuteranopia_fragment);
		prota_Program = loadProgram(R.raw.pass_vertex, R.raw.protanopia_fragment);
		trita_Program = loadProgram(R.raw.pass_vertex, R.raw.tritanopia_fragment);
		cataracts_Program = loadProgram(R.raw.cataracts_vertex, R.raw.cataracts_fragment);
		macdeg_Program = loadProgram(R.raw.macdeg_vertex,R.raw.macdeg_fragment);
		glaucoma_Program = loadProgram(R.raw.glaucoma_vertex,R.raw.glaucoma_fragment);
		/* Shader programs for Diabetic Retina and floater*/
		diabretina_texProgram= loadProgram(R.raw.dbretina_vertex,R.raw.dbretina_fragment);
		floaters_program=loadProgram(R.raw.floaters_vertex,R.raw.floaters_fragment);
		
//		progs[0]= pass_Program;
//		progs[1]= deuta_Program;
//		progs[2]= prota_Program;
//		progs[3]= trita_Program;
//		progs[4]= cataracts_Program;
//		progs[5]= macdeg_Program;
//		progs[6]= glaucoma_Program;
//		progs[7]= pass_Program;
//		progs[8]= pass_Program;
		
		progs[0]= pass_Program;
		progs[1]= cataracts_Program;
		progs[2]= macdeg_Program;
		progs[3]= glaucoma_Program;
		progs[4]= pass_Program; // diabetic Retina texture will be superimposed over this.
		progs[5]= pass_Program; // floaters texture will be superimposed over this.
		progs[6]= deuta_Program;
		progs[7]= prota_Program;
		progs[8]= trita_Program;
		
		
		
		active_Program = progs[active=0];
		
		if(description_on)
			descriptions = getResources().getStringArray(R.array.sim_descriptions);
		else
			descriptions = getResources().getStringArray(R.array.sim_names);
	    
		
		// Get reference to the vertex and texture attributes as well as the uniforms here.

		GLES20.glUseProgram(floaters_program);
		
		f_aPositionLocation = GLES20.glGetAttribLocation(floaters_program, "af_Position");
		GLES20.glEnableVertexAttribArray(f_aPositionLocation);

		f_aTextureCoord = GLES20.glGetAttribLocation(floaters_program, "f_intexCoord");
		GLES20.glEnableVertexAttribArray(f_aTextureCoord);

		f_uTextureLocation = GLES20.glGetUniformLocation(floaters_program, "f_sampler");
		GLES20.glUniform1i(f_uTextureLocation,0);

		translationCoords = GLES20.glGetUniformLocation(floaters_program, "transCoord");
	}


	private void initTex(){
		
		hTex = new int[1];
		GLES20.glGenTextures(1, hTex,0);
		GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, hTex[0]);
		GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_NEAREST);
		GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
		
		dbretTex   = initExtraTextures(R.drawable.diabetic_smudged); // Initialize the Diabetic Retina Smudged texture. 
		floaterTex = initExtraTextures(R.drawable.floater_texture);  // Initialize the floaters texture.
	}
	
	
	/*Function to load a texture for DBRetina and Floaters*/
	
	private int initExtraTextures(int TextureResourceID){
		
		int[] texID = new int[1];
		GLES20.glGenTextures(1, texID,0);
		if(texID[0]==0){
			//Log.w(TAG, "Couldn't generate a new OpenGL texture Object.");
			return 0;
		}
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled=false;
		
		final Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), TextureResourceID,options);
		if(bitmap==null){
			//Log.w(TAG, "Resource ID " + TextureResourceID + "could not be decoded.");
			GLES20.glDeleteTextures(1, texID,0);
			return 0;
		}
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texID[0]);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		return texID[0];
	}
	
	
	private int loadProgram(int vshader, int fshader){
		
		/* create a program */
		
		int vertexShader = loadGLShader(GLES20.GL_VERTEX_SHADER,vshader);
		int fragShader = loadGLShader(GLES20.GL_FRAGMENT_SHADER,fshader);
	
		int program = GLES20.glCreateProgram();
		
		if(program==0){
			
			//Log.v(TAG,"Couldn't create a program");
			
			return 0;
		}
		GLES20.glAttachShader(program,vertexShader);
		GLES20.glAttachShader(program, fragShader);
		
		final int[] linkStatus = new int[1];
		
		GLES20.glLinkProgram(program);
		
		
		
		GLES20.glGetProgramiv(program,GLES20.GL_LINK_STATUS,linkStatus,0);
		
		if(linkStatus[0]==0){ // couldn't link the program
			
			//Log.v(TAG, "Faliled to Link Prgram : " +"\n :" + GLES20.glGetProgramInfoLog(program));
			GLES20.glDeleteProgram(program);
			return 0;
		}else{
			//Log.v(TAG, "Program linked successfully.!!!");
		}
		
		return program;
	}
	
	private int loadGLShader(int type, int resId){
		
		String code = readRawTextFile(resId);
		int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, code);
		GLES20.glCompileShader(shader);
		// Get the compilation status.
		final int[] compileStatus = new int[1];
		GLES20.glGetShaderiv(shader,GLES20.GL_COMPILE_STATUS,compileStatus,0);
		// If the compilation failed, delete the shader.
		if(compileStatus[0]==0){
			//Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
			GLES20.glDeleteShader(shader);
			shader=0;
		}
		if(shader==0){
			throw new RuntimeException("Error creating shader.");
		}
		return shader;
		
	}
	
	private String readRawTextFile(int resId){
		InputStream inputStream = getResources().openRawResource(resId);
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder sb = new StringBuilder();
			String line;
			while((line=reader.readLine())!=null){
				sb.append(line).append("\n");
			}
			reader.close();
			return sb.toString();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return "";
	}
	private void deleteText(){
		
		GLES20.glDeleteTextures(1, hTex,0);
	}
	
	@Override
	public void onFrameAvailable(SurfaceTexture surfaceTexture) {
		mUpdateST = true;
		cardboardView.requestRender();
		
	}

	
	

}