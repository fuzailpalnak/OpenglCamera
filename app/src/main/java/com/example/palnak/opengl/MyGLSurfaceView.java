package com.example.palnak.opengl;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.EGL14;
import android.opengl.GLSurfaceView;
import android.view.WindowManager;

import javax.microedition.khronos.egl.EGL;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;

import static android.R.attr.version;
import static android.opengl.EGL14.EGL_DEFAULT_DISPLAY;
import static android.opengl.EGL14.EGL_NO_CONTEXT;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by palnak on 2/8/2017.
 */

public class MyGLSurfaceView extends GLSurfaceView {

    MyGL20Renderer renderer;
    public MyGLSurfaceView(Context context)
    {
        super(context);

        setEGLContextClientVersion(2);
        setEGLConfigChooser(8,8,8,8,16,0);
        setZOrderOnTop(true);






        renderer = new MyGL20Renderer((MainActivity)context);

        setRenderer(renderer);

        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);//means "do not call onDrawFrame() unless something explicitly requests rendering with requestRender()

    }

    public MyGL20Renderer getRenderer()
    {
        return renderer;
    }
}
