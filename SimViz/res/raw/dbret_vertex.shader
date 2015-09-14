attribute vec4 vPosition;
attribute vec2 vTexCoord;

varying vec2 texCoord[5];
varying vec2 txCoord;

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

	
    // figure out what this means. 
    vec2 singleStepOffset = vec2(0.0002,0.0002);
    txCoord=vTexCoord;
	gl_Position = vPosition;
	texCoord[0] = vTexCoord.xy + singleStepOffset * 1.407333;
	texCoord[1] = vTexCoord.xy - singleStepOffset * 1.407333;
	texCoord[2] = vTexCoord.xy + singleStepOffset * 3.294215;
	texCoord[3] = vTexCoord.xy - singleStepOffset * 3.294215;
}