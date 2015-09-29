precision mediump float;

uniform sampler2D dbRet_Texture;
varying vec2 dbRet_texCoord;


void main(){
	gl_FragColor = texture2D(dbRet_Texture,dbRet_texCoord);
}