package com.appota.sample.gamesdk;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.appota.gamesdk.commons.AppotaAction;
import com.appota.gamesdk.core.AppotaGameSDK;
import com.appota.gamesdk.core.AppotaReceiver;
import com.appota.gamesdk.model.AppotaSession;
import com.appota.gamesdk.model.TransactionResult;
import com.appota.sample.gamesdk1.R;
import com.onclan.android.core.OnClanInitCallback;
import com.onclan.android.core.OnClanSDK;
import com.onclan.android.core.OnClanSubType;
import com.onclan.android.core.OnClanUserInfoCallback;

public class MainActivity extends Activity {

	//private String apiKey = "c87d39adff487a2b89cb9719a10f93050534cff36";
	//private String apiKey = "0e08c327e1ba11e9ac5d23fc86b4c96e053bf65b8";
	//private String apiKey = "1817a27ba6e5b282dd6920f35ed84d940541fd226";
	private String apiKey = "K-A163561-U00000-CEBZXQ-0062D6E59CBB113A";
	private String sandboxApiKey = "13f89ae205bf254e62f3dde371e253b9053d70638";
	private AppotaGameSDK sdk;
	private OnClanSDK onClanSDK;
	private LoginReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// register receiver for receive payment and login event
		receiver = new LoginReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppotaAction.LOGIN_SUCCESS_ACTION);
		filter.addAction(AppotaAction.PAYMENT_SUCCESS_ACTION);
		filter.addAction(AppotaAction.SWITCH_SUCCESS_ACTION);
		filter.addAction(AppotaAction.LOGIN_FAIL_ACTION);
		filter.addAction(AppotaAction.LOGOUT_SUCCESS_ACTION);
		filter.addAction(AppotaAction.UPDATE_USER_INFO_ACTION);
		registerReceiver(receiver, filter);
		// init sdk
		onClanSDK = OnClanSDK.getInstance();
		sdk = AppotaGameSDK.getInstance();
		sdk.setAutoLogin(true);
		sdk.init(this, "http://123.30.173.181:8080/appota.config.json", false, "http://filestore9.com/test.php", apiKey, sandboxApiKey);
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
//	}

	public void manualLogin(View v) {
		sdk.showLogin();
	}

	public void loginFacebook(View v) {
		sdk.loginFacebook(this, null);
		//onClanSDK.submitScore("1", 500);
	}

	public void loginGoogle(View v) {
		sdk.loginGoogle(this);
	}

	public void register(View v) {
		sdk.showRegister();
	}

	// if not use AppotaSDKButton, call makePayment() in an event. for example,
	// on button click
	public void makePayment(View v) {
		sdk.setState("state1");
		sdk.makePayment();
	}

	public void showUserInfo(View v) {
		sdk.showUserInfo();
		sdk.setUseSDKButton(true);
	}

	public void switchAccount(View v) {
		sdk.changeAccount();
		sdk.setUseSDKButton(false);
	}

	public void logout(View v) {
		sdk.logout();
	}

	public void showSMSPayment(View v) {
		//sdk.showSMSPayment(this);
		sdk.showUnipinPayment(this);
	}

	public void showCardPayment(View v) {
		sdk.showCardPayment(this);
	}

	public void showBankPayment(View v) {
		sdk.showBankPayment(this);
	}

	public void showGPPayment(View v) {
		sdk.showGooglePayment(this);
	}

	// implement login recveive to start verify user on your server
	private class LoginReceiver extends AppotaReceiver {

		@Override
		public void onLoginSuccess(AppotaSession user) {
			// do verify login with your server now
			Toast.makeText(MainActivity.this, "Just for login testing. Username = " + user.username + ", AccessToken= " + user.accessToken, Toast.LENGTH_SHORT).show();
			String[] onClanFeatures = new String[] {OnClanSubType.TYPE_CHAT, OnClanSubType.TYPE_LEADERBOARD};
			onClanSDK.initOnClanSDK(MainActivity.this, onClanFeatures, user.getOnClanUser(), new OnClanInitCallback() {
				@Override
				public void onInited() {
					Toast.makeText(MainActivity.this, "onClan inited", Toast.LENGTH_SHORT).show();
					onClanSDK.getUserInfo(new OnClanUserInfoCallback() {
						
						@Override
						public void onGetUserInfoSuccess(String json) {
							// TODO Auto-generated method stub
							Toast.makeText(MainActivity.this, json, Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void onGetUserInfoError(String message) {
							// TODO Auto-generated method stub
							Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
			onClanSDK.enableLogout(false);
		}

		@Override
		public void onLogoutSuccess() {
			onClanSDK.logoutOnClan();
		}

		@Override
		public void onSwitchAccountSuccess(AppotaSession user) {
			onClanSDK.switchAccount(user.getOnClanUser());
		}

		// payment success callback
		@Override
		public void onPaymentSuccess(TransactionResult paymentResult) {
			
		}

		@Override
		public void onLoginFail() {
			// TODO Auto-generated method stub
		}

		@Override
		public void onUpdateUserInfo(String userId, String displayName) {
			// TODO Auto-generated method stub
			android.util.Log.d("OAOAOAOAOAOAOa", userId + ", " + displayName);
		}
	}

	@Override
	protected void onDestroy() {
		sdk.finish();
		onClanSDK.destroy();
		unregisterReceiver(receiver);
		super.onDestroy();
	}

}
