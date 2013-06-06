package ee.larseckart.fortumodemo.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import ee.larseckart.fortumodemo.PaymentConstants;

/**
 * Created by lars on 06.06.13.
 */
public class PotionStack {

    static final String TAG = PotionStack.class.getSimpleName();


    public static int addPotion(Context context, String potion) {

        int currentPotionsAmount = 0;
        SharedPreferences prefs = context.getSharedPreferences(PaymentConstants.PREFS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (potion.equals(PaymentConstants.PRODUCT_HEALTH_POTION)) {
            currentPotionsAmount = prefs.getInt(PaymentConstants.SP_KEY_HEALTH_POTIONS, 0);
            currentPotionsAmount++;
            editor.putInt(PaymentConstants.SP_KEY_HEALTH_POTIONS, currentPotionsAmount);
        }
        if (potion.equals(PaymentConstants.PRODUCT_MANA_POTION)) {
            currentPotionsAmount = prefs.getInt(PaymentConstants.SP_KEY_MANA_POTIONS, 0);
            currentPotionsAmount++;
            editor.putInt(PaymentConstants.SP_KEY_MANA_POTIONS, currentPotionsAmount);
        }

        editor.commit();
        Log.i(TAG, "added potion to the inventory");
        return currentPotionsAmount;
    }

    public static int getPotionAmount(Context context, String potion) {
        int result = 0;
        SharedPreferences prefs = context.getSharedPreferences(PaymentConstants.PREFS,
                Context.MODE_PRIVATE);
        if (potion.equals(PaymentConstants.PRODUCT_HEALTH_POTION)) {
            result = prefs.getInt(PaymentConstants.SP_KEY_HEALTH_POTIONS, 0);
        }
        if (potion.equals(PaymentConstants.PRODUCT_MANA_POTION)) {
            result = prefs.getInt(PaymentConstants.SP_KEY_MANA_POTIONS, 0);
        }

        return result;
    }
}
