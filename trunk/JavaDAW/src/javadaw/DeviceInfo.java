/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javadaw;

import java.nio.ByteBuffer;

import java.util.HashMap;
import org.jouvieje.FmodEx.Defines.INIT_MODES;
import org.jouvieje.FmodEx.Exceptions.InitException;
import org.jouvieje.FmodEx.Enumerations.FMOD_RESULT;
import org.jouvieje.FmodEx.FmodEx;
import org.jouvieje.FmodEx.System;
import org.jouvieje.FmodEx.Init;
import org.jouvieje.FmodEx.Enumerations.FMOD_PLUGINTYPE;
import org.jouvieje.FmodEx.Misc.BufferUtils;
import org.jouvieje.FmodEx.Structures.FMOD_GUID;

import static org.jouvieje.FmodEx.Defines.FMOD_INITFLAGS.FMOD_INIT_NORMAL;
import static org.jouvieje.FmodEx.Defines.FMOD_MODE.FMOD_HARDWARE;
import static org.jouvieje.FmodEx.Defines.FMOD_MODE.FMOD_LOOP_OFF;
import static org.jouvieje.FmodEx.Defines.FMOD_MODE.FMOD_OPENMEMORY;
import static org.jouvieje.FmodEx.Defines.FMOD_TIMEUNIT.FMOD_TIMEUNIT_MS;
import static org.jouvieje.FmodEx.Defines.VERSIONS.FMOD_VERSION;
import static org.jouvieje.FmodEx.Defines.VERSIONS.NATIVEFMODEX_JAR_VERSION;
import static org.jouvieje.FmodEx.Defines.VERSIONS.NATIVEFMODEX_LIBRARY_VERSION;
import static org.jouvieje.FmodEx.Enumerations.FMOD_CHANNELINDEX.FMOD_CHANNEL_FREE;
import static org.jouvieje.FmodEx.Enumerations.FMOD_RESULT.FMOD_ERR_CHANNEL_STOLEN;
import static org.jouvieje.FmodEx.Enumerations.FMOD_RESULT.FMOD_ERR_INVALID_HANDLE;
import static org.jouvieje.FmodEx.Enumerations.FMOD_RESULT.FMOD_OK;
import static org.jouvieje.FmodEx.Enumerations.FMOD_PLUGINTYPE.FMOD_PLUGINTYPE_CODEC;
import static org.jouvieje.FmodEx.Enumerations.FMOD_PLUGINTYPE.FMOD_PLUGINTYPE_DSP;
import static org.jouvieje.FmodEx.Enumerations.FMOD_PLUGINTYPE.FMOD_PLUGINTYPE_OUTPUT;

import static org.jouvieje.FmodEx.Misc.BufferUtils.newByteBuffer;
import static org.jouvieje.FmodEx.Misc.BufferUtils.SIZEOF_INT;

/**
 *
 * @author pioras
 */
public class DeviceInfo {

    private FMOD_RESULT result;
    private System system;
    private JavaDAWView javaDAWView;

    public HashMap<Integer, String> devices;
    public HashMap<Integer, String> drivers;

    private int[] pluginHandles;

    public DeviceInfo(JavaDAWView javaDAWView) {
        this.javaDAWView = javaDAWView;
        this.system = new System();
        devices = new HashMap<Integer, String>();
        drivers = new HashMap<Integer, String>();
    }

    public void setDevice(int no) {
        result = this.getSystem().setDriver(no);
        SoundUtils.ErrorCheck(result);
    }

    public void setDriver(int no) {
        int parsedNo = this.pluginHandles[no];
        result = this.getSystem().setOutputByPlugin(parsedNo);
        SoundUtils.ErrorCheck(result);
        
        // test, logowanie
        ByteBuffer byteBuffer = BufferUtils.newByteBuffer(256);
        ByteBuffer buffer2 = BufferUtils.newByteBuffer(256);
        FMOD_PLUGINTYPE[] plugin_types = new FMOD_PLUGINTYPE[1];
        this.result = this.getSystem().getPluginInfo(parsedNo, plugin_types, byteBuffer, byteBuffer.capacity(), buffer2.asIntBuffer());
        SoundUtils.ErrorCheck(this.result);
        String pluginName = BufferUtils.toString(byteBuffer);
        this.javaDAWView.getOutputTextArea().append("Ustawiony sterownik: " + pluginName + "\r\n");
    }

    public void createSystem() {
        try {
            Init.loadLibraries(INIT_MODES.INIT_FMOD_EX);
        } catch (InitException e) {
            java.lang.System.out.println("NativeFmodEx error: " + e.getMessage());
        }

        if (NATIVEFMODEX_LIBRARY_VERSION != NATIVEFMODEX_JAR_VERSION) {
            java.lang.System.out.println("Error!  NativeFmodEx library versions: " +
                    NATIVEFMODEX_LIBRARY_VERSION +
                    " vs. " + NATIVEFMODEX_JAR_VERSION);
        }

        this.result = FmodEx.System_Create(this.getSystem());
        SoundUtils.ErrorCheck(this.result);
    }

    public void init() {
        this.result = this.getSystem().init(32, FMOD_INIT_NORMAL, null);
        SoundUtils.ErrorCheck(this.result);
    }

    public void getDevices() {
        ByteBuffer buffer = BufferUtils.newByteBuffer(256);
        this.getSystem().getNumDrivers(buffer.asIntBuffer());
        int numberOfDrivers = buffer.getInt(0);
        FMOD_GUID guid = FMOD_GUID.create();

        for (int i = 0; i < numberOfDrivers; i++) {
            ByteBuffer byteBuffer = BufferUtils.newByteBuffer(256);
            this.result = this.getSystem().getDriverInfo(i, byteBuffer, byteBuffer.capacity(), guid);
            SoundUtils.ErrorCheck(this.result);
            String driverName = BufferUtils.toString(byteBuffer);
            this.javaDAWView.getOutputTextArea().append(i + ": " + driverName + "\r\n");
            devices.put(i, driverName);
        }
    }

    public void getDrivers() {
        ByteBuffer buffer = BufferUtils.newByteBuffer(256);
        ByteBuffer buffer2 = BufferUtils.newByteBuffer(256);

        this.result = this.getSystem().getNumPlugins(FMOD_PLUGINTYPE_OUTPUT, buffer.asIntBuffer());
        SoundUtils.ErrorCheck(this.result);
        int count = buffer.getInt(0);
        this.pluginHandles = new int[count];
        for (int i = 0; i < count; i++) {
            this.result = this.getSystem().getPluginHandle(FMOD_PLUGINTYPE_OUTPUT, i, buffer.asIntBuffer());
            SoundUtils.ErrorCheck(this.result);

            ByteBuffer byteBuffer = BufferUtils.newByteBuffer(256);
            FMOD_PLUGINTYPE[] plugin_types = new FMOD_PLUGINTYPE[1];
            int pluginHandle = buffer.getInt(0);
            this.pluginHandles[i] = pluginHandle;
            this.result = this.getSystem().getPluginInfo(pluginHandle, plugin_types, byteBuffer, byteBuffer.capacity(), buffer2.asIntBuffer());
            SoundUtils.ErrorCheck(this.result);

            String pluginName = BufferUtils.toString(byteBuffer);
            this.javaDAWView.getOutputTextArea().append(i + ": " + pluginName + "\r\n");
            this.drivers.put(i, pluginName);
        }
    }

    /**
     * @return the system
     */
    public System getSystem() {
        return system;
    }

}
