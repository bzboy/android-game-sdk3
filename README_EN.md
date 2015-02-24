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

**1. Import SDK into project**

Download Appota Game SDK for Android and import into IDE.

**2. Configure SDK**

**Configuration \<AndroidMainfest.xml\>**

- Open file \<AndroidMainfest.xml\> in your project Android.

- Add following lines to configure permission:

``` xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
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

**3. Integrate SDK**

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
        public void onLoginSuccess(AppotaSession user) {
            //do verify login with your server now
            Toast.makeText(MainActivity.this, user.getAccessToken(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLogoutSuccess() {

        }

        //payment success callback
        @Override
        public void onPaymentSuccess(TransactionResult paymentResult) {

        }

	@Override
	public void onLoginFail() {
	    // TODO Auto-generated method stub
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

You can create your custom buttons to call separate UI:

Place this code block in onDestroy() method of activity:
```java
    @Override
    protected void onDestroy() {
        sdk.finish();
        unregisterReceiver(receiver);
        super.onDestroy();
    }
```

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
    sdk.logout(boolean isShowLoginWhenLoggedOut); // Logout with option show/hide login popup after logged out
```

**4 - Run SDK samples**

You can see the more detail in the attached sample code.
