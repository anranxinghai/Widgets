
uniform mat4 u_Matrix;
attribute vec4 a_Position;
attribute vec4 a_Color;
varying vec4 v_Color;
void main (){
    v_Color = a_Color;
    gl_Position = u_Matrix * a_Position;//OpenGL在gl_Position变量中获取顶点位置属性 x、y、z、w
    gl_PointSize = 10.0;
}