package ee.larseckart.fortumodemo.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import ee.larseckart.fortumodemo.PaymentConstants;

/**
 * Created by lars on 06.06.13.
 */
public class BonusLevel {

    private final static String TAG = BonusLevel.class.getSimpleName();


    public static boolean unlockBonusLevel(Context context) {
        Log.i(TAG, "unlockbonus");
        SharedPreferences prefs = context.getSharedPreferences(PaymentConstants.PREFS,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(PaymentConstants.SP_KEY_BONUS_LEVEL, true);
        editor.commit();
        return true;
    }

    public static boolean isBonusUnlocked(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PaymentConstants.PREFS,
                Context.MODE_PRIVATE);
        return prefs.getBoolean(PaymentConstants.SP_KEY_BONUS_LEVEL, false);
    }
}
