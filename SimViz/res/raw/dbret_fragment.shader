#extension GL_OES_EGL_image_external : require
precision highp float;
// texture from program
uniform samplerExternalOES sTexture;

// had to harcode the dots as could get sampler2D to work with samplerExternalOES

varying vec2 txCoord;
varying highp vec2 texCoord[5];


float radius = 0.1;

float square(float a) {
	return a*a;
}

float distance(float x, float y, float tx, float ty) {
    return sqrt(square(x-tx)+square(y-ty));
} 

float add(vec4 a) {
   return a[0]+a[1]+a[2];
}

void main() {

	vec4 black = vec4(0.0);
	float x = txCoord.x-0.5;
	float y = txCoord.y-0.5;
	
	vec4 cur = texture2D(sTexture, txCoord);
	vec4 sum = vec4(0.0);
	float rad[8] = float[8](0.019,0.045,0.035,0.032,0.058,0.053,0.051,0.04);
	float ax[8] = float[8](0.217,0.073,-0.009,0.130,0.010,0.145,0.245,0.293);
	float by[8] = float[8](0.281,0.118,-0.007,-0.023,-0.156,0.008,0.148,0.012);
	
	for (int i = 0; i < 8; i++) {
	   float r=distance(x,y,ax[i],by[i]);
	   if (r<rad[i]) { 
	     sum = vec4(20.0*r-0.65);
	    }
	}
	sum += texture2D(sTexture, texCoord[0]) * 0.204164;
	sum += texture2D(sTexture, texCoord[1]) * 0.304005;
	sum += texture2D(sTexture, texCoord[2]) * 0.304005;
	sum += texture2D(sTexture, texCoord[3]) * 0.093913;
	sum += texture2D(sTexture, texCoord[4]) * 0.093913;
	if (add(sum)+0.25 > add(cur)) gl_FragColor = cur;	 
	else gl_FragColor = sum;
}
