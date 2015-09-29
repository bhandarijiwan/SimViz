attribute vec2 dbRet_position;
attribute vec2 dbRet_in_texCoord;

varying vec2 dbRet_texCoord;

void main(){
	gl_Position= vec4(dbRet_position,0.0,1.0);
	dbRet_texCoord=dbRet_in_texCoord;
}