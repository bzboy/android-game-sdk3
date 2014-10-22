
package com.appota.sample.gamesdk;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.appota.facebook.Session;
import com.appota.gamesdk.commons.AppotaAction;
import com.appota.gamesdk.core.AppotaGameSDK;
import com.appota.gamesdk.core.AppotaReceiver;
import com.appota.gamesdk.model.AppotaSession;
import com.appota.gamesdk.model.TransactionResult;
import com.appota.sample.gamesdk1.R;
import com.appota.support.v4.app.AppotaActivity;
import com.onclan.android.OnClanSDK;

public class MainActivity extends AppotaActivity {

    //private String apiKey = "123593a5f93eac19e26baee408f9928f0525e6a18";
    private String apiKey = "0e08c327e1ba11e9ac5d23fc86b4c96e053bf65b8";
    private String sandboxApiKey = "";
    private AppotaGameSDK sdk;
    private OnClanSDK onClanSDK;
    private LoginReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //register receiver for receive payment and login event
        receiver = new LoginReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppotaAction.LOGIN_SUCCESS_ACTION);
        filter.addAction(AppotaAction.PAYMENT_SUCCESS_ACTION);
        filter.addAction(AppotaAction.SWITCH_SUCCESS_ACTION);
        filter.addAction(AppotaAction.LOGIN_FAIL_ACTION);
        filter.addAction(AppotaAction.LOGOUT_SUCCESS_ACTION);
        registerReceiver(receiver, filter);
        //init sdk
        AppotaGameSDK.getInstance().setAutoLogin(true);
        onClanSDK = OnClanSDK.getInstance();
        sdk = AppotaGameSDK.getInstance();
        sdk.init(this, "https://developer.appota.com/config.php", false, "http://filestore9.com/test.php", apiKey, sandboxApiKey);
        //optional
        //sdk.setShowButtonType(AppotaGameSDK.SHOW_ACCOUNT_BUTTON);

        //show or hide switch, logout button
//        sdk.setShowUserFunctionButtons(false);
//        sdk.prepareFacebookLogin(this, savedInstanceState);
        //we use 2 way to login facebook: native and web
        //sdk.setLoginFacebookType(AppotaGameSDK.LOGIN_FACEBOOK_NATIVE);
        //sdk.setLoginFacebookType(AppotaGameSDK.LOGIN_FACEBOOK_WEB);
        //if login type is web, you have to set clientId and clientSecret (clientId, clientSecret get from http://developer.appota.com)
        //sdk.setClientKey("8d638e1421080c68d9dfb9bc89c56adf0525e6957");
        //sdk.setClientSecret("90b830d7b5fe5b771baf4e2e41fb0b9d0525e6957");
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }
    
    public void manualLogin(View v){
    	sdk.manualLogin();
    	//sdk.showLogin();
    	//sdk.isUserLogin();
//    	List<PaymentMethod> list = sdk.getListPaymentMethod();
//    	for(PaymentMethod method : list){
//    		Log.d("AADASDASDASD", method.name);
//    	}
    }
    
    public void loginFacebook(View v){
    	sdk.loginFacebook(this, null);
    }
    
    public void loginGoogle(View v){
    	sdk.loginGoogle(this);
    }
    
    public void register(View v){
    	sdk.showRegister();
    }

    //if not use AppotaSDKButton, call makePayment() in an event. for example, on button click
    public void makePayment(View v){
        sdk.setState("state1");
        sdk.makePayment();
    }

    public void showUserInfo(View v){
        sdk.showUserInfo();
        sdk.setUseSDKButton(true);
    }

    public void switchAccount(View v){
        sdk.switchAccount();
        sdk.setUseSDKButton(false);
    }

    public void logout(View v){
        sdk.logout();
    }
    
    public void showSMSPayment(View v){
    	sdk.showSMSPayment(this);
    }
    
    public void showCardPayment(View v){
    	sdk.showCardPayment(this);
    }
    
    public void showBankPayment(View v){
    	sdk.showBankPayment(this);
    }
    
    public void showGPPayment(View v){
    	sdk.showGooglePayment(this);
    }

    //implement login recveive to start verify user on your server
    private class LoginReceiver extends AppotaReceiver {

        @Override
        public void onLoginSuccess(AppotaSession user) {
            //do verify login with your server now
            Toast.makeText(MainActivity.this, "Just for login testing. Username = " + user.username + ", AccessToken= " + user.accessToken, Toast.LENGTH_SHORT).show();
            onClanSDK.initOnClanSDK(MainActivity.this, user.getOnClanUser());
        }

        @Override
        public void onLogoutSuccess() {
        	onClanSDK.logoutOnClan();
        }

        @Override
        public void onSwitchAccountSuccess(AppotaSession user) {
            //Toast.makeText(MainActivity.this, "Just for switch testing. Username = " + user.getUsername(), Toast.LENGTH_SHORT).show();
        }

        //payment success callback
        @Override
        public void onPaymentSuccess(TransactionResult paymentResult) {

        }

		@Override
		public void onLoginFail() {
			// TODO Auto-generated method stub
			Log.d("ASDASDASDAS", "ZXCZXCVZXCZX");
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
