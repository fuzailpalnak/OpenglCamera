package com.example.palnak.opengl;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by palnak on 2/8/2017.
 */

public class MyGL20Renderer implements GLSurfaceView.Renderer {
    FramePreview mFramePreview;
    int texture;
    private SurfaceTexture surface;
    private int Width;
    private int Height;
    private int count=0;
    MainActivity delegate;
    public MyGL20Renderer(MainActivity _delegate) {
        delegate = _delegate;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {


        texture = createTexture();
        mFramePreview = new FramePreview(texture);
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        delegate.startCamera(texture);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        Width=width;
        Height=height;


    }

    @Override
    public void onDrawFrame(GL10 gl) {
        float[] mtx = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        count++;


        surface.updateTexImage();
        surface.getTransformMatrix(mtx);
        mFramePreview.draw();

         final Bitmap  bmp = createBitmapFromGLSurface(0, 0, Width, Height, gl);

        if(count <10){

            AsyncTask<Bitmap, Void, Boolean> task = new AsyncTask<Bitmap, Void, Boolean>(){

                @Override
                protected Boolean doInBackground(Bitmap... params) {
                    if(count<10){

                        try
                        {
                            FileOutputStream fos=new FileOutputStream("/sdcard/"+count);


                            try
                            {

                                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                                fos.flush();
                                fos.close();
                            }
                            catch (IOException e)
                            {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }



                        }
                        catch (FileNotFoundException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                    return true;

                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    if (true){
                        long start = System.currentTimeMillis();
                        Log.d("Start",start+"-------------------"+count);
                    }
                }


            };
            task.execute(bmp);

        }



    }


    static public int loadShader(int type, String shaderCode)
    {
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    static private int createTexture()
    {
        int[] texture = new int[1];


        GLES20.glGenTextures(1,texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);


        return texture[0];
    }

    public void setSurface(SurfaceTexture _surface)
    {
        surface = _surface;
    }

    public Bitmap createBitmapFromGLSurface(int x, int y, int w, int h, GL10 gl){
        int b[]=new int[(int) w*h];
        int bt[]=new int[(int) w*h];
        IntBuffer ib=IntBuffer.wrap(b);
        ib.position(0);
        gl.glReadPixels(0, 0, w, h, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib);

       for(int i=0; i<h; i++)
        {
            for(int j=0; j<w; j++)
            {
                int pix=b[i*w+j];
                int pb=(pix>>16)&0xff;
                int pr=(pix<<16)&0x00ff0000;
                int pix1=(pix&0xff00ff00) | pr | pb;
                bt[(h-i-1)*w+j]=pix1;
            }
        }



        Bitmap sb=null;

        sb=Bitmap.createBitmap(bt,w,h, Bitmap.Config.RGB_565);
        return sb;
    }


}
