#include <jni.h>
#include <string>
#include "hello.h"
extern "C"
JNIEXPORT jstring JNICALL
Java_pinguo_us_jnidemo_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}