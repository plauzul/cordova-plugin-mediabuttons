package cordova.plugin.mediabuttons;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.session.MediaSession;
import android.view.KeyEvent;

/**
 * This class listens for MEDIA_BUTTON events and sends it to javascript
 */
public class MediaButtons extends CordovaPlugin {

    MediaSession ms;
    AudioTrack at;
    boolean startedActionMediaButtons;

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        ms =  new MediaSession(webView.getContext(), webView.getContext().getPackageName());
        at = new AudioTrack(AudioManager.STREAM_MUSIC, 48000, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                AudioTrack.getMinBufferSize(48000, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT), AudioTrack.MODE_STREAM);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("startActionMedia")) {
            this.startActionMedia(callbackContext);
            return true;
        } else if (action.equals("stopActionMedia")) {
            this.stopActionMedia(callbackContext);
            return true;
        }
        return false;
    }

    private void startActionMedia(CallbackContext callbackContext) {
        ms.setActive(true);

        ms.setCallback(new MediaSession.Callback() {
            @Override
            public boolean onMediaButtonEvent(Intent mediaButtonIntent) {
                KeyEvent keyEvent = (KeyEvent) mediaButtonIntent.getExtras().get(Intent.EXTRA_KEY_EVENT);
                if (startedActionMediaButtons) {
                    startedActionMediaButtons = false;
                    return super.onMediaButtonEvent(mediaButtonIntent);
                }
                cordova.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startedActionMediaButtons = true;
                        webView.loadUrl("javascript:cordova.fireDocumentEvent('actionbutton');");
                    }
                });
                return super.onMediaButtonEvent(mediaButtonIntent);
            }
        });

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        PendingIntent mediaButtonReceiverPendingIntent = PendingIntent.getBroadcast(this.webView.getContext(), 0, mediaButtonIntent, PendingIntent.FLAG_IMMUTABLE);

        ms.setMediaButtonReceiver(mediaButtonReceiverPendingIntent);
        at.play();
    }

    private void stopActionMedia(CallbackContext callbackContext) {
        at.stop();
        at.release();
    }
}
