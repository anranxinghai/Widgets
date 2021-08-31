//顶点着色器（Vertex Shader）
uniform mat4 u_Matrix;
attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates;
varying vec2 vs_TEXCOORD0;
void main()
{
    gl_Position = a_Position;
	vs_TEXCOORD0 = a_TextureCoordinates;
}
