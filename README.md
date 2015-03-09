Other languages: [Vietnamese](README.md) | [Chinese](README_CN.md)

**Get Started**

Appota Game SDK is the simplest way to integrate user and payment for
your game in Appota system. This SDK provides solutions for payment
methods such as: SMS, Card. Internet Banking, Paypal and Google Play
Payment.

**Steps to integrate SDK:**

​1. Import SDK into your project

​2. Configure SDK

​3. Integrate SDK

​4. Run SDK samples

<hr/>

# I. Install SDK

** Download Appota Game SDK for Android here: 
https://github.com/appota/android-game-sdk/archive/master.zip

Or if you use any git tool, clone this url: https://github.com/appota/android-game-sdk.git

** Import downloaded SDK project to your game project:

import AppotaGameSDK project to your IDE:
<br/>
*** On Eclipse IDE, click File -> Import
<br/>
![add](https://github.com/appota/android-game-sdk/blob/master/docs/images/1.png)
<br/>
<br/>
*** Select Existing Android project
<br/>
![add](https://github.com/appota/android-game-sdk/blob/master/docs/images/2.png)
<br/>
<br/>
*** Click Browse and point to downloaded SDK
<br/>
![add](https://github.com/appota/android-game-sdk/blob/master/docs/images/3.png)
<br/>
<br/>
*** Select SDK project and Sample project to import:
<br/>
![add](https://github.com/appota/android-game-sdk/blob/master/docs/images/5.png)
<br/>
<br/>
*** Reference your game project to AppotaGameSDK project
<br/>
![add](https://github.com/appota/android-game-sdk/blob/master/docs/images/6.png)
<br/>
<br/>
You can see the video guide here
<br/>
https://www.youtube.com/watch?v=lup6Ni9Memk
<br/><br/>
[![AppotaGameSDK](http://img.youtube.com/vi/lup6Ni9Memk/0.jpg)](https://www.youtube.com/watch?v=lup6Ni9Memk)
<br/>
<br/>
# II. Configure SDK**

**Configuration \<AndroidMainfest.xml\>**

- Open file \<AndroidMainfest.xml\> in your project Android.

- Add following lines to configure permission:

``` xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WRITE_SETTINGS" />
```

- Add this permission if use Google Play Payment

``` xml
    <uses-permission android:name="com.android.vending.BILLING" />
```

- To use payment interface, add following activity configuration:

``` xml
    <activity
            android:name="com.appota.gamesdk.UserActivity"
            android:configChanges="orientation|keyboardHidden|screenSize"
            android:theme="@style/Theme.Appota.GameSDK"
            android:windowSoftInputMode="adjustPan" />
        <activity
            android:name="com.appota.gamesdk.UserInfoActivity"
            android:configChanges="orientation|keyboardHidden|screenSize"
            android:theme="@style/Theme.Appota.GameSDK"
            android:windowSoftInputMode="adjustPan" />
        <activity
            android:name="com.appota.gamesdk.PaymentActivity"
            android:configChanges="orientation|keyboardHidden|screenSize"
            android:theme="@style/Theme.Appota.GameSDK" />
```

- To turn off or on Sandbox mode, add following configuration:

``` xml
    <meta-data android:name="sandbox" android:value="false" />
```

- To login with Google Account, add following configuration:

``` xml
    <uses-permission android:name="android.permission.GET_ACCOUNTS" />
    <uses-permission android:name="android.permission.USE_CREDENTIALS" />
```

- To login with Facebook Account, add following configuration:
 
``` xml
<activity android:name="com.appota.facebook.LoginActivity" android:theme="@android:style/Theme.Translucent.NoTitleBar"
    android:label="@string/app_name" />
    <meta-data android:name="com.facebook.sdk.ApplicationId" android:value="YOUR_FACEBOOK_APP_ID" />
```

- To login with Twitter Account, add following configuration:

``` xml
    <meta-data android:name="com.appota.gamesdk.twitter.consumer.key" android:value="YOUR_CONSUMER_KEY" />
    <meta-data android:name="com.appota.gamesdk.twitter.consumer.secret" android:value="YOUR_SECRET_KEY" />
```

# III. Integrate SDK**

Appota Game SDK provides class AppotaConfiguration for all needed configuration to integrate Game SDK.

**Required configurations:**

 - apiKey
 - sandboxKey
 - payment methods
 - login methods
 - a class inherits from AppotaReceiver to get login/logout/payment event.

``` java
    private class MyReceiver extends AppotaReceiver {

        @Override
        public void onLoginSuccess(AppotaUser user) {
            //Called when user login successfully, AppotaUser parameter contains basic 
            //user info (userId, userName, accessToken, ...)
            //See sample code for more details
        }

        @Override
        public void onLogoutSuccess() {
		//Called when user logout successfully
        }

        //payment success callback
        @Override
        public void onPaymentSuccess(TransactionResult paymentResult) {
		//Called when user did a transaction successfully, TransactionResult parameter contains basic 
		//information of transaction (transactionId, time, amount, ...)
		//See sample code for more details
        }

	@Override
	public void onLoginFail() {
	    // Called when user login fail
	}
    } 
``` 

**JSON configurations:**

Appota Game SDK provides a flexible method to configure various options. You need flow these steps to use this method:

 - Generate a JSON config file by using JSON Generator (https://developer.appota.com/sdktool.php).
 - Upload your config file to an accessible host.
 - Pass it as a param when init Appota Game SDK.


To init SDK, place this code block in onCreate() method of activity:


``` java
    // Register receiver to receive callback when login/logout/payment success
    MyReceiver receiver = new MyReceiver();
    IntentFilter filter = new IntentFilter();
    filter.addAction(AppotaAction.LOGIN_SUCCESS_ACTION);
	filter.addAction(AppotaAction.PAYMENT_SUCCESS_ACTION);
	filter.addAction(AppotaAction.LOGIN_FAIL_ACTION);
	filter.addAction(AppotaAction.LOGOUT_SUCCESS_ACTION);
    registerReceiver(receiver, filter);
    
    // Init SDK
    AppotaGameSDK sdk = AppotaGameSDK.getInstance().init(context, apiKey, noticeUrl, configUrl);
```

- Context context: Application's context.
- String configUrl: Link to JSON config file.
- String noticeUrl: Called when a transaction is finished, if you have already config IPN on developer site, just pass ""
- String apiKey: Provided by Appota for your application.
 
Place this code block in onDestroy() method of activity:
```java
    @Override
    protected void onDestroy() {
        sdk.finish();
        unregisterReceiver(receiver);
        super.onDestroy();
    }
```

You can create your custom buttons to call separate UI:

``` java
    sdk.makePayment(); // Show payment UI
```
``` java
    sdk.showUserInfo(); // Show user info UI
```
``` java
    sdk.switchAccount(); // Switch between accounts
```
``` java
    sdk.logout(); // Logout
```

*** Other sdk method:
```java
	//if keep login session set to true, user will not have to re-login next time. Default is true
	sdk.setKeepLoginSession(boolean keep);
```
<br/>
```java
	//set auto show login form when start. Default is true
	sdk.setAutoShowLoginDialog(boolean autoShow);
```
<br/>
```java
	//show login dialog manually
	sdk.showLogin();
```
<br/>
```java
	//optional parameter use to include in payment transaction return
	sdk.setState(String state);
```

# IV. Run SDK samples**

You can see the more detail in the attached sample code.
