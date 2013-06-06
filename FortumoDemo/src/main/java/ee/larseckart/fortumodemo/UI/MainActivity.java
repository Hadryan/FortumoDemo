package ee.larseckart.fortumodemo.UI;

import java.util.List;

import android.os.AsyncTask;
import com.fortumo.android.Fortumo;
import com.fortumo.android.PaymentActivity;
import com.fortumo.android.PaymentRequestBuilder;
import com.fortumo.android.PaymentResponse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import ee.larseckart.fortumodemo.Manifest;
import ee.larseckart.fortumodemo.PaymentConstants;
import ee.larseckart.fortumodemo.R;
import ee.larseckart.fortumodemo.data.BonusLevel;
import ee.larseckart.fortumodemo.data.PotionStack;
import ee.larseckart.fortumodemo.data.Wallet;


public class MainActivity extends PaymentActivity {

    static final String TAG = MainActivity.class.getSimpleName();

    private TextView goldTextView;
    private TextView bonusLevelUnlockedTextView;
    private TextView healthPotionTextView;
    private TextView manaPotionTextView;

    private Button boyGoldButton;
    private Button buyBonusLevelButton;
    private Button buyHealthPotionButton;
    private Button buyManaPotionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareUI();
        preparePaymentButtons();

        new UpdateDataTask().execute();

        Fortumo.enablePaymentBroadcast(this, Manifest.permission.PAYMENT_BROADCAST_PERMISSION);
    }

    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new UpdateDataTask().execute();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(PaymentConstants.SUCCESSFUL_PAYMENT);
        registerReceiver(updateReceiver, filter);
        Log.i(TAG, "updateReceiver registered");
    }

    @Override
    protected void onStop() {
        unregisterReceiver(updateReceiver);
        Log.i(TAG, "updateReceiver unregistered");
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.restore_purchases:
                restorePurchases();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restorePurchases() {
        new RestoreDataTask().execute();
    }

    private void prepareUI() {
        goldTextView = (TextView) findViewById(R.id.GoldAmountTextView);
        bonusLevelUnlockedTextView = (TextView) findViewById(R.id.bonusUnlockedTextView);
        healthPotionTextView = (TextView) findViewById(R.id.healthPotionsTextView);
        manaPotionTextView = (TextView) findViewById(R.id.manaPotionsTextView);

        boyGoldButton = (Button) findViewById(R.id.buyGoldButton);
        buyBonusLevelButton = (Button) findViewById(R.id.buyBonusLevelButton);
        buyHealthPotionButton = (Button) findViewById(R.id.buyHealthPotionButton);
        buyManaPotionButton = (Button) findViewById(R.id.buyManaPotionButton);
    }

    private void preparePaymentButtons() {

        // Gold, consumable
        boyGoldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentRequestBuilder builder = new PaymentRequestBuilder();
                builder.setService(PaymentConstants.GOLD_SERVICE_ID,
                        PaymentConstants.GOLD_SERVICE_IN_APP_SECRET);
                builder.setProductName(PaymentConstants.PRODUCT_GOLD);
                builder.setConsumable(true);
                builder.setDisplayString(PaymentConstants.DISPLAY_STRING_GOLD);
                builder.setCreditsMultiplier(1.1d);
                builder.setIcon(R.drawable.ic_launcher);
                makePayment(builder.build());

            }
        });

        // Bonus level, non-consumable
        buyBonusLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentRequestBuilder builder = new PaymentRequestBuilder();
                builder.setService(PaymentConstants.BONUS_LEVEL_SERVICE_ID,
                        PaymentConstants.BONUS_LEVEL_IN_APP_SECRET);
                builder.setProductName(PaymentConstants.PRODUCT_BONUS_LEVEL);
                builder.setConsumable(false);
                builder.setDisplayString(PaymentConstants.DISPLAY_STRING_BONUS_LEVEL);
                builder.setIcon(R.drawable.ic_launcher);
                makePayment(builder.build());
            }
        });

        // Health potion, consumable
        buyHealthPotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentRequestBuilder builder = new PaymentRequestBuilder();
                builder.setService(PaymentConstants.POTION_SERVICE_ID, PaymentConstants.POTION_IN_APP_SECRET);
                builder.setProductName(PaymentConstants.PRODUCT_HEALTH_POTION);
                builder.setConsumable(true);
                builder.setDisplayString(PaymentConstants.DISPLAY_STRING_HEALTH_POTION);
                builder.setIcon(R.drawable.ic_launcher);
                makePayment(builder.build());

            }
        });

        // Mana potion, consumable
        buyManaPotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentRequestBuilder builder = new PaymentRequestBuilder();
                builder.setService(PaymentConstants.POTION_SERVICE_ID, PaymentConstants.POTION_IN_APP_SECRET);
                builder.setProductName(PaymentConstants.PRODUCT_MANA_POTION);
                builder.setConsumable(true);
                builder.setDisplayString(PaymentConstants.DISPLAY_STRING_MANA_POTION);
                builder.setIcon(R.drawable.ic_launcher);
                makePayment(builder.build());
            }
        });
    }

    private class UpdateDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... voids) {
            String[] result = new String[4];
            result[0] = String.valueOf(Wallet.getColdAmount(MainActivity.this));
            result[1] = String.valueOf(BonusLevel.isBonusUnlocked(MainActivity.this));
            result[2] = String.valueOf(PotionStack.getPotionAmount(MainActivity.this, PaymentConstants.PRODUCT_HEALTH_POTION));
            result[3] = String.valueOf(PotionStack.getPotionAmount(MainActivity.this, PaymentConstants.PRODUCT_MANA_POTION));
            return result;
        }

        @Override
        protected void onPostExecute(String[] data) {
            goldTextView.setText(data[0]);
            bonusLevelUnlockedTextView.setText(data[1]);
            healthPotionTextView.setText(data[2]);
            manaPotionTextView.setText(data[3]);
        }
    }

    private class RestoreDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            Context context = MainActivity.this;
            List<PaymentResponse> history = Fortumo.getPurchaseHistory(context,
                    PaymentConstants.BONUS_LEVEL_SERVICE_ID,
                    PaymentConstants.BONUS_LEVEL_IN_APP_SECRET, 5000);

            final String resultMessage;
            // since bonus level can only be bought once, the list has either size
            // zero or 1
            if (history != null && history.size() > 0) {
                BonusLevel.unlockBonusLevel(context);
                resultMessage = "Restored bonus level";
            } else {
                resultMessage = "Nothing restored";
            }
            return resultMessage;
        }

        @Override
        protected void onPostExecute(String message) {
            Context context = MainActivity.this;
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            bonusLevelUnlockedTextView.setText(String.valueOf(BonusLevel.isBonusUnlocked(MainActivity.this)));
        }
    }

}
