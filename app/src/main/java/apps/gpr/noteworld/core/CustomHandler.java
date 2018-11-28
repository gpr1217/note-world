package apps.gpr.noteworld.core;

import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;

public class CustomHandler extends ResultReceiver {
    public CustomHandler(Handler handler) {
        super(handler);
    }

    /*private ResultReceiver resultReceiver;
    public CustomHandler(ResultReceiver receiver) {
        resultReceiver = receiver;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        resultReceiver.onReceiveResult(msg);
    }

    public interface ResultReceiver{
        void onReceiveResult(Message message);
    }*/
}
