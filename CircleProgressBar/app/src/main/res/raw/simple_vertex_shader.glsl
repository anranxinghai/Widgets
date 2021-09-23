//顶点着色器（Vertex Shader）
uniform mat4 u_Matrix;
attribute vec4 a_Position;
void main (){
    gl_Position = u_Matrix * a_Position;//OpenGL在gl_Position变量中获取顶点位置属性 x,y,z,w
}