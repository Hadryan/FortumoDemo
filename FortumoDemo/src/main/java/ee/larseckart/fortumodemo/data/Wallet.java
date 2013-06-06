package ee.larseckart.fortumodemo.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import ee.larseckart.fortumodemo.PaymentConstants;

/**
 * Created by lars on 06.06.13.
 */
public class Wallet {

    static final String TAG = Wallet.class.getSimpleName();


    public static int addGold(Context context, int amount) {
        Log.i(TAG, "adding " + amount + " gold to wallet");

        SharedPreferences prefs = context.getSharedPreferences(PaymentConstants.PREFS,
                Context.MODE_PRIVATE);
        int currentGoldAmount = prefs.getInt(PaymentConstants.SP_KEY_GOLD, 0);

        SharedPreferences.Editor editor = prefs.edit();
        currentGoldAmount += amount;
        editor.putInt(PaymentConstants.SP_KEY_GOLD, currentGoldAmount);
        editor.commit();
        return currentGoldAmount;
    }

    public static int getColdAmount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PaymentConstants.PREFS,
                Context.MODE_PRIVATE);
        return prefs.getInt(PaymentConstants.SP_KEY_GOLD, 0);
    }
}
