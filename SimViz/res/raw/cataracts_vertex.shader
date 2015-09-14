attribute vec4 vPosition;
attribute vec2 vTexCoord;

varying vec2 texCoord[5];

void main() {
    // figure out what this means. 
    vec2 singleStepOffset = vec2(0.002,0.002);
	gl_Position = vPosition;
	texCoord[0] = vTexCoord.xy + singleStepOffset * 1.407333;
	texCoord[1] = vTexCoord.xy - singleStepOffset * 1.407333;
	texCoord[2] = vTexCoord.xy + singleStepOffset * 3.294215;
	texCoord[3] = vTexCoord.xy - singleStepOffset * 3.294215;
}