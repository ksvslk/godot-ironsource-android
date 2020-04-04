package org.godotengine.godot;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import javax.microedition.khronos.opengles.GL10;
import com.ironsource.adapters.supersonicads.SupersonicConfig;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.BannerListener;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.ironsource.mediationsdk.sdk.OfferwallListener;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;
import com.ironsource.mediationsdk.utils.IronSourceUtils;
import com.godot.game.R;

public class IronSourceSDK extends Godot.SingletonBase {

    protected Activity appActivity;
    protected Context appContext;
    private Godot activity = null;
    private int instanceId = 0;
    private FrameLayout layout = null; // Store the layout
    IronSourceBannerLayout banner = null;
    private FrameLayout.LayoutParams adParams = null; // Store the layout params
    private const String API_KEY = "<YOUR_API_KEY>"
	    
    public String myFunction(String p_str) {
        // A function to bind.
        IntegrationHelper.validateIntegration(activity);
        return "Hello " + p_str;
    }

    public void getInstanceId(int pInstanceId) {
        // You will need to call this method from Godot and pass in the
        // get_instance_id().
        instanceId = pInstanceId;
    }

    static public Godot.SingletonBase initialize(Activity p_activity) {
        return new IronSourceSDK(p_activity);
    }

    public IronSourceSDK(Activity p_activity) {
        // Register class name and functions to bind.
        registerClass("IronSourceSDK", new String[] { "myFunction", "getInstanceId" });
        this.appActivity = p_activity;
        this.appContext = appActivity.getApplicationContext();
        // You might want to try initializing your singleton here, but android
        // threads are weird and this runs in another thread, so to interact with Godot
        // you usually have to do.
        this.activity = (Godot) p_activity;
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                // Useful way to get config info from "project.godot".
                // String key = GodotLib.getGlobal("plugin/api_key");
                // SDK.initializeHere();
                // Init Banner
                IronSource.init(activity, API_KEY, IronSource.AD_UNIT.REWARDED_VIDEO);
                IronSource.init(activity, API_KEY, IronSource.AD_UNIT.BANNER);

                banner = IronSource.createBanner(activity, ISBannerSize.SMART);
                adParams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.MATCH_PARENT,
					FrameLayout.LayoutParams.WRAP_CONTENT
                );
                FrameLayout.LayoutParams mainParams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.MATCH_PARENT,
					FrameLayout.LayoutParams.MATCH_PARENT
				);
				adParams.gravity = Gravity.BOTTOM;
                layout = new FrameLayout(activity);
                activity.addContentView(layout, mainParams);

                if (banner != null)
				{
					layout.removeView(banner); // Remove the old view
				}
                layout.addView(banner, 0, adParams);
                banner.setBannerListener(new BannerListener() {
                    
                    @Override
                    public void onBannerAdLoaded() {
                        System.out.println("onBannerAdLoaded");

                        // Called after a banner ad has been successfully loaded
                    }

                    @Override
                    public void onBannerAdLoadFailed(IronSourceError error) {
                        // Called after a banner has attempted to load an ad but failed.
                        // TODO:
                    }

                    @Override
                    public void onBannerAdClicked() {
                        // Called after a banner has been clicked.
                    }

                    @Override
                    public void onBannerAdScreenPresented() {
                        // Called when a banner is about to present a full screen content.
                    }

                    @Override
                    public void onBannerAdScreenDismissed() {
                        // Called after a full screen content has been dismissed
                    }

                    @Override
                    public void onBannerAdLeftApplication() {
                        // Called when a user would be taken out of the application context.
                    }
                });
     
            IronSource.loadBanner(banner);
            // YOUR OTHER CODE //
            // YOUR OTHER CODE //
            // YOUR OTHER CODE //


                IronSource.setRewardedVideoListener(new RewardedVideoListener() {
                    /**
                     * Invoked when the RewardedVideo ad view has opened. Your Activity will lose
                     * focus. Please avoid performing heavy tasks till the video ad will be closed.
                     */
                    @Override
                    public void onRewardedVideoAdOpened() {
                    }

                    /*
                     * Invoked when the RewardedVideo ad view is about to be closed. Your activity
                     * will now regain its focus.
                     */
                    @Override
                    public void onRewardedVideoAdClosed() {
                    }

                    /**
                     * Invoked when there is a change in the ad availability status.
                     *
                     * @param - available - value will change to true when rewarded videos are
                     *          *available. You can then show the video by calling
                     *          showRewardedVideo(). Value will change to false when no videos are
                     *          available.
                     */
                    @Override
                    public void onRewardedVideoAvailabilityChanged(boolean available) {
                        // Change the in-app 'Traffic Driver' state according to availability.
                        if (available) {
                            // IronSource.showRewardedVideo(null);
                        }
                    }

                    /**
                     * /** Invoked when the user completed the video and should be rewarded. If
                     * using server-to-server callbacks you may ignore this events and wait *for the
                     * callback from the ironSource server.
                     *
                     * @param - placement - the Placement the user completed a video from.
                     */
                    @Override
                    public void onRewardedVideoAdRewarded(Placement placement) {
                        /**
                         * here you can reward the user according to the given amount. String rewardName
                         * = placement.getRewardName(); int rewardAmount = placement.getRewardAmount();
                         */
                    }

                    /*
                     * Invoked when RewardedVideo call to show a rewarded video has failed
                     * IronSourceError contains the reason for the failure.
                     */
                    @Override
                    public void onRewardedVideoAdShowFailed(IronSourceError error) {
                    }

                    /*
                     * Invoked when the end user clicked on the RewardedVideo ad
                     */
                    @Override
                    public void onRewardedVideoAdClicked(Placement placement) {
                    }

                    /*
                     * Note: the events AdStarted and AdEnded below are not available for all
                     * supported rewarded video ad networks. Check which events are available per ad
                     * network you choose to include in your build. We recommend only using events
                     * which register to ALL ad networks you include in your build. Invoked when the
                     * video ad starts playing.
                     */
                    @Override
                    public void onRewardedVideoAdStarted() {
                    }

                    /* Invoked when the video ad finishes plating. */
                    @Override
                    public void onRewardedVideoAdEnded() {
                    }
                });

            }
        });

    }

    // Forwarded callbacks you can reimplement, as SDKs often need them.

    protected void onMainActivityResult(int requestCode, int resultCode, Intent data) {
    }

    protected void onMainRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }

    protected void onMainPause() {
    }

    protected void onMainResume() {
    }

    protected void onMainDestroy() {
    }

    protected void onGLDrawFrame(GL10 gl) {
    }

    protected void onGLSurfaceChanged(GL10 gl, int width, int height) {
    } // Singletons will always miss first 'onGLSurfaceChanged' call.

}
