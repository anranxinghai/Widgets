# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

MESSAGE(STATUS "Cmake:${CMAKE_CURRENT_SOURCE_DIR}")
cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
add_library( # Sets the name of the library.
             hello

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/hello.c )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

target_include_directories( hello PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}/src/main/include)

#file(COPY ${relative_dir} DESTINATION ${distribution_copy_DIR})
add_library(hello_world SHARED IMPORTED)
#set_target_properties(hello_world PROPERTIES IMPORTED_LOCATION /home/anranxinghai/Downloads/CircleProgressBar/app/build/intermediates/cmake/debug/obj/armeabi-v7a/libhello.so)
find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries(hello
#        hello_world
        log)
