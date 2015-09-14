#extension GL_OES_EGL_image_external : require
precision mediump float;
// texture from program
uniform samplerExternalOES sTexture;

varying highp vec2 texCoord[5];
varying vec2 txCoord;

float bluredge=0.04;
float blackedge=0.02;

void main() {

	float x = txCoord.x-0.5;
	float y = txCoord.y-0.5;

    if (x*x+y*y<bluredge) {
    vec4 sum = vec4(0.0);
	sum += texture2D(sTexture, texCoord[0]) * 0.204164;
	sum += texture2D(sTexture, texCoord[1]) * 0.304005;
	sum += texture2D(sTexture, texCoord[2]) * 0.304005;
	sum += texture2D(sTexture, texCoord[3]) * 0.093913;
	sum += texture2D(sTexture, texCoord[4]) * 0.093913;
	gl_FragColor = sum;
	}
	else gl_FragColor=texture2D(sTexture, txCoord);
}

  //   if (x*x+y*y<blackedge) gl_FragColor=vec4(0.0,0.0,0.0,0.0);