package ee.larseckart.fortumodemo.receivers;

import com.fortumo.android.Fortumo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import ee.larseckart.fortumodemo.PaymentConstants;
import ee.larseckart.fortumodemo.data.BonusLevel;
import ee.larseckart.fortumodemo.data.PotionStack;
import ee.larseckart.fortumodemo.data.Wallet;

/**
 * Created by lars on 06.06.13.
 */
public class PaymentStatusReceiver extends BroadcastReceiver {

    static final String TAG = PaymentStatusReceiver.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        int billingStatus = extras.getInt("billing_status");

        if (billingStatus == Fortumo.MESSAGE_STATUS_BILLED) {

            String serviceId = extras.getString("service_id");
            String productName = extras.getString("product_name");

            if (serviceId.equals(PaymentConstants.GOLD_SERVICE_ID)) {
                int gold = Integer.parseInt(extras.getString("credit_amount"));
                Wallet.addGold(context, gold);
            }
            if (serviceId.equals(PaymentConstants.BONUS_LEVEL_SERVICE_ID) && productName.equals(PaymentConstants.PRODUCT_BONUS_LEVEL)) {
                BonusLevel.unlockBonusLevel(context);
            }
            if (serviceId.equals(PaymentConstants.POTION_SERVICE_ID) && productName.equals(PaymentConstants.PRODUCT_HEALTH_POTION)) {
                PotionStack.addPotion(context, PaymentConstants.PRODUCT_HEALTH_POTION);
            }
            if (serviceId.equals(PaymentConstants.POTION_SERVICE_ID) && productName.equals(PaymentConstants.PRODUCT_MANA_POTION)) {
                PotionStack.addPotion(context, PaymentConstants.PRODUCT_MANA_POTION);
            }
            Intent result = new Intent(PaymentConstants.SUCCESSFUL_PAYMENT);
            context.sendBroadcast(result);
        }
    }
}
