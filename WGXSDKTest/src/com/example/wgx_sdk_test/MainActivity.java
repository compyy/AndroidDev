package com.example.wgx_sdk_test;


import com.wellcore.ibeacon.BeaconConfig;
import com.wellcore.ibeacon.BeaconController;
import com.wellcore.ibeacon.BeaconStateListener;
import com.zmz.util.Log;
import com.zmz.util.ToastUtil;

import android.os.Bundle;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		// step0 : Make sure 'AndroidManifest.xml' have add info about wgx 确保Manifest中的都已经添加
		// step1 : Bind this activity
		
		BeaconController.getInstance().bindActiviyPage(this);
		
//		//设置信号过滤值，只有大于此值才可以连接
		//set the rssi min value, the rssi larger than this value will connect device.
		//BeaconController.getInstance().setRssiLimit(-80);
		
//		// set whether or not use sqc as the filter which get from scanrecord
		//设置是否使用扫描回复中的sqc作为连接，默认会使用
		//BeaconController.getInstance().setScanFilter(false);
		
//		//set the timeout duration, default is 60s,设置超时时间，默认是60s
		// BeaconController.getInstance().setmTimeOutDuration(45);
		
		// step2 : Register beacon state listener
		BeaconController.getInstance().setBeaconStateListener(
				new BeaconStateListener() {
					@Override
					public void onWriteSuccess() {
						ToastUtil.showShort(MainActivity.this, "write success");
						closeProgress();
					}
					@Override
					public void onWriteStart() {
						ToastUtil.showShort(MainActivity.this, "write start");
						showProgress();
					}
					@Override
					public void onWriteFail(String arg0) {
						// TODO Auto-generated method stub
						ToastUtil.showShort(MainActivity.this, "write fail:"
								+ arg0);
						closeProgress();
					}
					@Override
					public void onGetDeviceRssiInfo(int arg0) {
						Log.i("onGetDeviceRssiInfo:" + arg0);
					}

					@Override
					public void onGetDevicePowerInfo(int arg0) {
						Log.i("onGetDevicePowerInfo:" + arg0);

					}
				});
	}

	// step3~step5 do your operate
	
	// step3 : Set your uuid,major,minor and other beacon parms here.
	public void doWriteConfigClick(View v) {
		BeaconConfig config = new BeaconConfig();
		
		config.setmCurrentUUID1("e2c56db5dffb48d2b060d0f5a71096e1");
		config.setmDevicePwd("140611"); //beacon default pwd is "140611"
		config.setmMajor1(12);
		config.setmMinor1(34);
		BeaconController.getInstance().doWriteConfig(config);
	}
	
	// step4 : Modify the beacon password.
	public void doResetPwdClick(View v) {
		BeaconConfig config = new BeaconConfig();
		config.setmDevicePwd("140611");
		config.setmDeviceNewPwd("140612");
		BeaconController.getInstance().modifyDevicePwd(config);
	}
	
	// step5 : Reset beacon to default config.
	public void doResetConfigClick(View v) {
		BeaconConfig config = new BeaconConfig();
		config.setmDevicePwd("140611");
		BeaconController.getInstance().resetBeaconConfig(config);
		
	}
	
	// step6 : unbind this activity
	@Override
	protected void onDestroy() {
		super.onDestroy();
		BeaconController.getInstance().unbindActiviyPage();
	}

	private ProgressDialog progressDialog;

	protected void showProgress() {
		if (progressDialog == null || !progressDialog.isShowing()) {
			progressDialog = ProgressDialog.show(this, null, "write start");
		}
	}

	protected void closeProgress() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
