
package javadaw;

import java.nio.ByteBuffer;

import java.util.HashMap;
import org.jouvieje.FmodEx.Enumerations.FMOD_RESULT;
import org.jouvieje.FmodEx.Misc.BufferUtils;
import org.jouvieje.FmodEx.Structures.FMOD_GUID;


/**
 *
 * @author pioras
 */
public class RecordDeviceInfo {

    private FMOD_RESULT result;

    private JavaDAWView javaDAWView;
    private JDAWEngine jDAWEngine;

    public HashMap<Integer, String> devices;

    private int choosenDriver;

    public RecordDeviceInfo(JavaDAWView javaDAWView, JDAWEngine jDAWEngine) {
        this.javaDAWView = javaDAWView;
        this.jDAWEngine = jDAWEngine;
        devices = new HashMap<Integer, String>();
        this.choosenDriver = 0;
    }

    public void setDevice(int no) {
        this.choosenDriver = no;
    }

    public void getDevices() {
        ByteBuffer buffer = BufferUtils.newByteBuffer(256);
        this.jDAWEngine.getSystem().getRecordNumDrivers(buffer.asIntBuffer());
        int numberOfDrivers = buffer.getInt(0);
        FMOD_GUID guid = FMOD_GUID.create();

        for (int i = 0; i < numberOfDrivers; i++) {
            ByteBuffer byteBuffer = BufferUtils.newByteBuffer(256);
            this.result = this.jDAWEngine.getSystem().getRecordDriverInfo(i, byteBuffer, byteBuffer.capacity(), guid);
            SoundUtils.ErrorCheck(this.result);
            String driverName = BufferUtils.toString(byteBuffer);
            this.javaDAWView.getOutputTextArea().append(i + ": " + driverName + "\r\n");
            devices.put(i, driverName);
        }
    }

    /**
     * @return the choosenDriver
     */
    public int getChoosenDriver() {
        return choosenDriver;
    }

}
