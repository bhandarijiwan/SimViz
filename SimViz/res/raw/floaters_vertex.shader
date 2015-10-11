attribute vec2 af_Position;

attribute vec2 f_intexCoord;

uniform mat4 translationMatrix;
uniform vec2 transCoord;
varying vec2 f_texCoord;


void main()
{
	//gl_Position = translationMatrix * vec4(af_Position,0.0,1.0);
	
	gl_Position = vec4(af_Position.x+transCoord.x,af_Position.y+transCoord.y,0.0,1.0);
	
	/*vec4 transCoord = vec4(af_Position.x+transCoord.x,af_Position.y+transCoord.y,0.0,1.0);
	
	if(transCoord.x>0.9){
		transCoord.x= -transCoord.x;
	}
	if(transCoord.y>0.9){
		transCoord.y= -transCoord.y;
	}
	gl_Position=transCoord;*/
	
	//gl_Position = vec4((af_Position+0.0026),0.0,1.0);
	
	f_texCoord = f_intexCoord;
}