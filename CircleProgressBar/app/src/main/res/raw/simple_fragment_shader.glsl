//片段着色器（Fragment Shader）
precision mediump float;//设置精度为中等
uniform vec4 u_Color;
void main(){
    gl_FragColor = u_Color;//OpenGL在gl_FragColor变量中获取颜色值
}
