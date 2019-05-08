package us.pinguo.androidsdk1;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by taoli on 14/11/4.
 */
public class PGShaderHolder {

    public static byte[] array;

    public synchronized static byte[] getShaderFile(Context context) {

        if (array == null) {
            AssetManager am = context.getAssets();
            BufferedInputStream in = null;
            try {
                in = new BufferedInputStream(am.open("load_background.jpg", AssetManager.ACCESS_BUFFER));

                ByteArrayOutputStream out;

                out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024 * 4];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
                array = out.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return array;
    }
}
