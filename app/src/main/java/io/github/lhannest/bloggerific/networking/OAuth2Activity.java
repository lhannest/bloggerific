package io.github.lhannest.bloggerific.networking;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

import io.github.lhannest.bloggerific.R;

public class OAuth2Activity extends AppCompatActivity {

    private final static String BLOGGER_SCOPE = "https://www.googleapis.com/auth/blogger";
    private final static String ACCOUNT_TYPE = "com.google";
    public final static String INTENT_KEY = "oauth2token";

    private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> future) {
            try {
                Bundle bundle = future.getResult();

                Intent launch = (Intent) bundle.get(AccountManager.KEY_INTENT);

                if (launch != null) {
                    startActivityForResult(launch, 0);
                    return;
                }

                String oauth2token = bundle.getString(AccountManager.KEY_AUTHTOKEN);

                Intent intent = new Intent();
                intent.putExtra(INTENT_KEY, oauth2token);
                setResult(RESULT_OK, intent);
                finish();

            } catch (OperationCanceledException e) {
                throw new RuntimeException(e.getMessage(), e.getCause());
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e.getCause());
            } catch (AuthenticatorException e) {
                throw new RuntimeException(e.getMessage(), e.getCause());
            }
        }
    }

    private void requestToken() {
        AccountManager accountManager = AccountManager.get(this);
        Bundle options = new Bundle();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        Account googleAccount = accounts[0];

        accountManager.getAuthToken(
                googleAccount,
                "oauth2:" + BLOGGER_SCOPE,
                options,
                this,
                new OnTokenAcquired(),
                new Handler() {
                    //TODO: This is supposed to be an error handler, implement it!
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            requestToken();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth2);

        requestToken();

    }
}
