Other languages: [Vietnamese](README.md) | [English](README_EN.md)

**Get Started**

Android Appota Game SDK 是给你应用集成Appota 用户和支付系统的最为简洁方式。 本SDK提供： 短信、充值卡、网络银行、支付宝、Google Play Payment等各种支付方式的解决方案。

**添加SDK步骤:**


1.给project import SDK

2. 配置SDK

3. 集成SDK

3. 运行SDK samples

 

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

# II. 配置 SDK**

**配置文件 \<AndroidMainfest.xml\>**

- 打开 文件 \<AndroidMainfest.xml\> trong project Android.

-添加以下内容以配置：

``` xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

-添加以下permission 配置以便使用Google Play付费面板：

``` xml
    <uses-permission android:name="com.android.vending.BILLING" />
```

-添加以下activity配置以便使用短信付费面板：

``` xml
    <activity android:name="com.appota.gamesdk.SMSPaymentActivity" android:theme="@style/Theme.Appota.GameSDK" android:configChanges="orientation|keyboardHidden|screenSize"/>
```

-添加以下activity配置以便使用充值卡付费面板：

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

-添加以下内容以便打开、关闭sandbox 状态：

``` xml
    <meta-data android:name="sandbox" android:value="false" />
```

-添加以下permission 配置以便使用谷歌账户登录：

``` xml
    <uses-permission android:name="android.permission.GET_ACCOUNTS" />
    <uses-permission android:name="android.permission.USE_CREDENTIALS" />
```

-添加以下permission 配置以便使用Facebook账户登录：

``` xml
    <activity android:name="com.appota.facebook.LoginActivity" android:theme="@android:style/Theme.Translucent.NoTitleBar"
    android:label="@string/app_name" />
    <meta-data android:name="com.facebook.sdk.ApplicationId" android:value="YOUR_FACEBOOK_APP_ID" />
```
 

# III. 集成 SDK**

Appota Game SDK给所有需要的配置提供AppotaConfiguration class以便集成Game SDK

**各个必须的配置:**

 - apiKey
 - sandboxKey
 - payment methods
 - login methods
 - a class inherits from AppotaReceiver to get login/logout/payment successfully.


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

** JSON配置:**

Appota Game SDK给开发商提供一个便利的配置方式。 您需要进行一下的步骤以便使用Appota Game SDK

 - 使用JSON Generator来 创造有配置的JSON 文件
(https://developer.appota.com/sdktool.php).
 -把JSON配置文件Upload 到可以访问的host
 -用到配置文件的URL初始Appota Game SDK

**初始 SDK**

把以下的code放在以下activity中onCreate()函数：

``` java
     // Register receiver to receive callback when login/logout/payment success
    MyReceiver receiver = new MyReceiver();
    IntentFilter filter = new IntentFilter();
    filter.addAction(AppotaAction.LOGIN_SUCCESS_ACTION);
	filter.addAction(AppotaAction.PAYMENT_SUCCESS_ACTION);
	filter.addAction(AppotaAction.LOGIN_FAIL_ACTION);
	filter.addAction(AppotaAction.LOGOUT_SUCCESS_ACTION);;
    registerReceiver(receiver, filter);

    // Init SDK
    AppotaGameSDK sdk = AppotaGameSDK.getInstance().init(Context context, String apiKey, String noticeUrl, String configUrl);
```

 - configUrl:到JSON配置文件的链接
 - noticeUrl:叫出当交易结束， 如果你已经在developer.appota.com 配置IPN就可以给""填写价值。
 - apiKey: Appota 将你的应用提供的key 
 
 把以下的code放在以下activity中onDestroy()函数：
```java
    @Override
    protected void onDestroy() {
        sdk.finish();
        unregisterReceiver(receiver);
        super.onDestroy();
    }
```

你也可以任意创造按钮并叫出不同的界面。

``` java
    sdk.makePayment(); // Show payment UI
```

``` java
    sdk.showUserInfo(); // Show user info UI
```

``` java
    sdk.switchAccount(); // Switch between accounts
```

*** Other sdk method:
```java
	//if keep login session set to true, user will not have to re-login next time. Default is true
	sdk.setKeepLoginSession(boolean keep);
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

# IV. 运行 SDK Samples**

参考SDK附件的sample code
