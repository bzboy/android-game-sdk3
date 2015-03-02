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
import com.appota.gamesdk.model.AppotaUser;
import com.appota.gamesdk.model.TransactionResult;
import com.appota.sample.gamesdk1.R;

public class MainActivity extends Activity {

	private String apiKey = "YOUR API KEY";
	private AppotaGameSDK sdk;
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
		filter.addAction(AppotaAction.LOGIN_FAIL_ACTION);
		filter.addAction(AppotaAction.LOGOUT_SUCCESS_ACTION);
		registerReceiver(receiver, filter);
		// init sdk
		sdk = AppotaGameSDK.getInstance();
		//if keep login session set to true, user will not have to re-login next time. Default is true
		sdk.setKeepLoginSession(false);
		sdk.init(this, apiKey, "YOUR NOTICE URL", "YOUR CONFIG URL");
	}

	public void manualLogin(View v) {
		sdk.showLogin();
	}

	public void makePayment(View v) {
		sdk.setState("state1");
		sdk.makePayment();
	}

	public void showUserInfo(View v) {
		sdk.showUserInfo();
	}

	public void switchAccount(View v) {
		sdk.switchAccount();
	}

	public void logout(View v) {
		sdk.logout();
	}

	// implement login recveive to start verify user on your server
	private class LoginReceiver extends AppotaReceiver {

		@Override
		public void onLoginSuccess(AppotaUser user) {
			// do verify login with your server now
			Toast.makeText(MainActivity.this, "Just for login testing. Username = " + user.username + ", AccessToken= " + user.accessToken, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onLogoutSuccess() {
			
		}

		// payment success callback
		@Override
		public void onPaymentSuccess(TransactionResult paymentResult) {
			
		}

		@Override
		public void onLoginFail() {
			// TODO Auto-generated method stub
		}
	}

	@Override
	protected void onDestroy() {
		sdk.finish();
		unregisterReceiver(receiver);
		super.onDestroy();
	}

}
