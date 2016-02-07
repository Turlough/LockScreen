package com.example;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.utils.LockscreenService;
import com.example.utils.LockscreenUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class LockScreenActivity extends Activity implements
		LockscreenUtils.OnLockStatusChangedListener {

	// User-interface
	private Button btnUnlock;

	// Member variables
	private LockscreenUtils mLockscreenUtils;



	void setWindowType() {
//		this.getWindow().setType(
//				LayoutParams.TYPE_KEYGUARD_DIALOG);
		this.getWindow().addFlags(
				LayoutParams.FLAG_FULLSCREEN
						| LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| LayoutParams.FLAG_KEEP_SCREEN_ON
//						| LayoutParams.FLAG_DISMISS_KEYGUARD
		);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setWindowType();
		setContentView(R.layout.activity_lockscreen);

		init();

		// unlock screen in case of app get killed by system
		if (getIntent() != null && getIntent().hasExtra("kill")
				&& getIntent().getExtras().getInt("kill") == 1) {
			//enableKeyguard();
			unlockHomeButton();
		} else {

			try {
				// disable keyguard
				disableKeyguard();

				// lock home button
				lockHomeButton();

				// start service for observing intents
				startService(new Intent(this, LockscreenService.class));

				addPinListener();


			} catch (Exception e) {
			}

		}

	}

	private void init() {
		mLockscreenUtils = new LockscreenUtils();
		btnUnlock = (Button) findViewById(R.id.btnUnlock);
		btnUnlock.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(LockScreenActivity.this, Login_Activity.class);
				startActivity(i);
				unlockHomeButton();
				unlockDevice();
			}
		});
	}


	// Don't finish Activity on Back press
	@Override
	public void onBackPressed() {
		return;
	}

	// Handle button clicks
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//		if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
//				|| (keyCode == KeyEvent.KEYCODE_POWER)
//				|| (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
//				|| (keyCode == KeyEvent.KEYCODE_CAMERA)) {
//			return true;
//		}
//		if ((keyCode == KeyEvent.KEYCODE_HOME)) {
//
//			return true;
//		}
//
//		return false;
//
//	}
//
//	// handle the key press events here itself
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP
//				|| (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
//				|| (event.getKeyCode() == KeyEvent.KEYCODE_POWER)) {
//			return false;
//		}
//		if ((event.getKeyCode() == KeyEvent.KEYCODE_HOME)) {
//
//			return true;
//		}
//		return false;
//	}

	// Lock home button
	public void lockHomeButton() {
		mLockscreenUtils.lock(LockScreenActivity.this);
	}

	// Unlock home button and wait for its callback
	public void unlockHomeButton() {
		mLockscreenUtils.unlock();
	}

	// Simply unlock device when home button is successfully unlocked
	@Override
	public void onLockStatusChanged(boolean isLocked) {
		if (!isLocked) {
			unlockDevice();
		}
	}


	@SuppressWarnings("deprecation")
	private void disableKeyguard() {
		KeyguardManager mKM = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		KeyguardManager.KeyguardLock mKL = mKM.newKeyguardLock("IN");
		mKL.disableKeyguard();
	}

	@SuppressWarnings("deprecation")
	private void enableKeyguard() {
		KeyguardManager mKM = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		KeyguardManager.KeyguardLock mKL = mKM.newKeyguardLock("IN");
		mKL.reenableKeyguard();
	}

	//Simply unlock device by finishing the activity
	private void unlockDevice() {
		finish();
	}

	private void addPinListener() {

		final EditText pin = (EditText) findViewById(R.id.enterPin);
		pin.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (s.toString().equals("7945")) {
					unlockHomeButton();
					unlockDevice();
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});

		pin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//enableKeyguard();
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
						.showSoftInput(pin, InputMethodManager.SHOW_FORCED);
			}
		});
	}

}