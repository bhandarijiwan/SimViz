precision mediump float;

uniform sampler2D f_sampler;

//uniform float texPxWidth;
//uniform float texPxHeight;

varying vec2 f_texCoord;

void main()
{
	//vec2 uvCoords;
	//uvCoords.x =  f_texCoord.x/texPxWidth;
	//uvCoords.y =  f_texCoord.y/texPxHeight;
	gl_FragColor = texture2D(f_sampler,f_texCoord);
	
	
}