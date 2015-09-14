#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES sTexture;

vec4 blindVisionR = vec4(0.43,  0.72, -0.15, 0.0 );
vec4 blindVisionG = vec4(0.34,  0.57,  0.09, 0.0 );
vec4 blindVisionB = vec4(-0.02,  0.03,  1.00, 0.0 );

vec4 sample;
vec4 test;

varying vec2 texCoord;
void main() {
  sample=texture2D(sTexture,texCoord);
  test.r=dot(sample,blindVisionR);
  test.g=dot(sample,blindVisionG);
  test.b=dot(sample,blindVisionB);
  gl_FragColor=vec4(test.r,test.g,test.b,sample.a);
}