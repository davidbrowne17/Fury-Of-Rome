package net.davidbrowne.furyofrome;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdView;
import net.davidbrowne.furyofrome.Tools.AdHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;

public class AndroidLauncher extends AndroidApplication implements AdHandler {
	private static final String TAG = "AndroidLauncher";
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;
	protected AdView adView;


	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case SHOW_ADS:
					adView.setVisibility(View.VISIBLE);
					break;
				case HIDE_ADS:
					adView.setVisibility(View.GONE);
					break;
			}
		}
	};

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RelativeLayout layout = new RelativeLayout(this);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new Game(this), config);
		layout.addView(gameView);

		adView = new AdView(this);
		adView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				int visiblity = adView.getVisibility();
				adView.setVisibility(AdView.GONE);
				adView.setVisibility(visiblity);
				Log.i(TAG, "Ad Loaded...");
			}
		});
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId("ca-app-pub-5896490118144449/9124952802");
		AdRequest.Builder builder = new AdRequest.Builder();
		//run once before uncommenting the following line. Get TEST device ID from the logcat logs.
		//builder.addTestDevice("33BE2250B43518CCDA7DE426D04EE231");
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT
		);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);


		layout.addView(adView, adParams);
		adView.loadAd(builder.build());
		setContentView(layout);
	}

	@Override
	public void showAds(boolean show) {
		handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}
}
