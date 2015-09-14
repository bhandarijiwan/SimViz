#extension GL_OES_EGL_image_external : require

precision highp float;

// texture from program

uniform samplerExternalOES sTexture;


varying highp vec2 texCoord[5];
varying highp vec2 txCoord;

float add(vec4 a){
	return a[0]+a[1]+a[2]+a[3];
}

void main(){
	
	float x = txCoord.x - 0.5;
	float y = txCoord.y - 0.5;
	float r = sqrt(x*x +y*y);
	float light = 0.05;
	float dark = -0.9;
	float bluredge = 0.35;
	
	vec4 cur = texture2D(sTexture, txCoord);
	
	if(r<bluredge){
		float v = 19.0/5.0*r-1.0;
		vec4 sum = vec4(v);
		sum += texture2D(sTexture, texCoord[0]) * 0.204164;
		sum += texture2D(sTexture, texCoord[1]) * 0.304005;
		sum += texture2D(sTexture, texCoord[2]) * 0.304005;
		sum += texture2D(sTexture, texCoord[3]) * 0.093913;
		sum += texture2D(sTexture, texCoord[4]) * 0.093913;
		if(add(sum)<add(cur))
			gl_FragColor = sum;
		else
			gl_FragColor = cur;
			
	}
	else
		gl_FragColor = cur; 
}