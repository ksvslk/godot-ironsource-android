package org.godotengine.godot;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.util.Log;
import java.util.*;
import android.view.ViewGroup;
import android.widget.Toast;
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
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.ironsource.mediationsdk.utils.IronSourceUtils;
import com.godot.game.R;

public class IronSourcePlugin extends Godot.SingletonBase {

	private String TAG = "godot";
	private String APP_KEY = "";

	private Activity activity = null; // The main activity of the game
	private int instance_id = 0;

	private Placement mPlacement;

	private FrameLayout mBannerParentLayout = null;
	private IronSourceBannerLayout mIronSourceBannerLayout = null;
	private FrameLayout.LayoutParams mAdParams = null;

	static public Godot.SingletonBase initialize(Activity activity) {
		return new IronSourcePlugin(activity);
	}

	public IronSourcePlugin(Activity p_activity) {
		registerClass("IronSourcePlugin", new String[] {
				"init",
				// Banner
				"createAndloadBanner", "destroyAndDetachBanner",
				// Rewarded video
				"showRewardedVideo",
				// Interstitial
				"loadInterstitial", "showInterstitial" });
		activity = p_activity;
	}

	@Override
	public View onMainCreateView(Activity activity) {
		mBannerParentLayout = new FrameLayout(activity);
		return mBannerParentLayout;
	}

	protected void onMainResume() {
		IronSource.onResume(activity);
	}

	protected void onMainPause() {
		IronSource.onPause(activity);
	}

	public void init(int instance_id, final String[] chosen_ad_units, String appKey) {

		IntegrationHelper.validateIntegration(activity); // TODO: REMOVE BEFORE GOING LIVE
		IronSource.shouldTrackNetworkState(activity, true);
		APP_KEY = appKey;
		this.instance_id = instance_id;

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				IronSource.init(
					activity,
					APP_KEY,
					IronSource.AD_UNIT.REWARDED_VIDEO,
					IronSource.AD_UNIT.BANNER,
					IronSource.AD_UNIT.INTERSTITIAL
				);
				if (Arrays.asList(chosen_ad_units).contains("REWARDED_VIDEO")) {
					setRewardedVideoListeners();
					IronSource.init(activity, APP_KEY, IronSource.AD_UNIT.REWARDED_VIDEO);
				}
				if (Arrays.asList(chosen_ad_units).contains("BANNER")) {
					IronSource.init(activity, APP_KEY, IronSource.AD_UNIT.BANNER);
					createAndloadBanner();
				}
				if (Arrays.asList(chosen_ad_units).contains("INTERSTITIAL")) {
					setInterstitialListeners();
					IronSource.init(activity, APP_KEY, IronSource.AD_UNIT.INTERSTITIAL);
				}
			}
		});
	}

	// --------- REWARDED VIDEO ---------

	private void setRewardedVideoListeners() {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				IronSource.setRewardedVideoListener(new RewardedVideoListener() {
				
					@Override
					public void onRewardedVideoAvailabilityChanged(boolean available) {
						GodotLib.calldeferred(
							instance_id,
							"on_rewarded_video_availability_changed",
							new Object[] { available }
						);
					}
			
					@Override
					public void onRewardedVideoAdClosed() {
						if (mPlacement != null) {
							// if the user was rewarded
							GodotLib.calldeferred(
								instance_id,
								"on_rewarded_video_ad_closed",
								new Object[] { mPlacement.getRewardName(), mPlacement.getRewardAmount() }
							);
							mPlacement = null;
						}
					}

					@Override
					public void onRewardedVideoAdShowFailed(IronSourceError error) {
						GodotLib.calldeferred(
							instance_id,
							"on_rewarded_video_ad_show_failed",
							new Object[] { error.toString() }
						);
					}

					@Override
					public void onRewardedVideoAdRewarded(Placement placement) {
						mPlacement = placement;
					}

					@Override
					public void onRewardedVideoAdOpened() {
					}
				
					@Override
					public void onRewardedVideoAdClicked(Placement placement) {
					}

					@Override
					public void onRewardedVideoAdStarted() {
					}

					@Override
					public void onRewardedVideoAdEnded() {
					}
				});
			}
		});
	}

	public void showRewardedVideo(final String placementName) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (IronSource.isRewardedVideoAvailable()) {
					IronSource.showRewardedVideo(placementName);
				}
			}
		});
	}

	// --------- INTERSTITIAL ---------

	private void setInterstitialListeners() {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				IronSource.setInterstitialListener(new InterstitialListener() {

					@Override
					public void onInterstitialAdReady() {
						GodotLib.calldeferred(
							instance_id,
							"on_interstitial_ad_ready",
							new Object[] {}
						);
					}

					@Override
					public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
						GodotLib.calldeferred(
							instance_id,
							"on_interstitial_ad_show_failed",
							new Object[] { ironSourceError.toString() }
						);
					}
					@Override
					public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
						GodotLib.calldeferred(
							instance_id,
							"on_interstitial_ad_load_failed",
							new Object[] { ironSourceError.toString() }
						);
					}

					@Override
					public void onInterstitialAdOpened() {
					}

					@Override
					public void onInterstitialAdClosed() {
					}

					@Override
					public void onInterstitialAdClicked() {
					}

					@Override
					public void onInterstitialAdShowSucceeded() {
					}

				});
			}
		});
	}

	public void loadInterstitial() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				IronSource.loadInterstitial();
			}
		});
	}

	private void showInterstitial() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (IronSource.isInterstitialReady()) {
					IronSource.showInterstitial();
				}
			}
		});
	}

	// --------- BANNER ---------

	private void createAndloadBanner() {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {

				mIronSourceBannerLayout = IronSource.createBanner(activity, ISBannerSize.SMART);
				mAdParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
						FrameLayout.LayoutParams.WRAP_CONTENT);

				mAdParams.gravity = Gravity.BOTTOM;
				activity.addContentView(mIronSourceBannerLayout, mAdParams);

				if (mIronSourceBannerLayout != null) {

					ViewGroup parent = (ViewGroup) mIronSourceBannerLayout.getParent();
					if (parent != null) {
						parent.removeView(mIronSourceBannerLayout);
					}
					mBannerParentLayout.removeView(mIronSourceBannerLayout); // Remove the old view
				}

				mBannerParentLayout.addView(mIronSourceBannerLayout, 0, mAdParams);

				if (mIronSourceBannerLayout != null) {
					mIronSourceBannerLayout.setBannerListener(new BannerListener() {

						@Override
						public void onBannerAdLoaded() {
							mBannerParentLayout.setVisibility(View.VISIBLE);
							GodotLib.calldeferred(
								instance_id,
								"on_banner_ad_loaded",
								new Object[] {}
							);
						}

						@Override
						public void onBannerAdLoadFailed(IronSourceError error) {
							GodotLib.calldeferred(
								instance_id,
								"on_banner_ad_load_failed",
								new Object[] { error.toString() }
							);
						}

						@Override
						public void onBannerAdScreenPresented() {
						}

						@Override
						public void onBannerAdScreenDismissed() {
						}

						@Override
						public void onBannerAdClicked() {

						}

						@Override
						public void onBannerAdLeftApplication() {

						}
						
					});
					IronSource.loadBanner(mIronSourceBannerLayout);
				} 
			}
		});
	}

	private void destroyAndDetachBanner() {
		IronSource.destroyBanner(mIronSourceBannerLayout);
		if (mBannerParentLayout != null) {
			mBannerParentLayout.removeView(mIronSourceBannerLayout);
		}
	}
}
