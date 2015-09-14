attribute vec4 vPosition;
attribute vec2 vTexCoord;


varying highp vec2 texCoord[5];

varying highp vec2 txCoord;

void main(){

	// figure out what this means
	vec2 singleStepOffset = vec2(0.000002,0.000002);
	
	txCoord = vTexCoord;
	
	gl_Position = vPosition;
	
	texCoord[0] = vTexCoord.xy + singleStepOffset * 1.407333;
	texCoord[1] = vTexCoord.xy + singleStepOffset * 1.407333;
	texCoord[2] = vTexCoord.xy + singleStepOffset * 3.294215;
	texCoord[3] = vTexCoord.xy + singleStepOffset * 3.294215;
	texCoord[4] = vTexCoord.xy + singleStepOffset * 3.294215;
}