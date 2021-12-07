package com.example.pikalti.lib.bikeModeDetection;

import android.content.Context;

import com.example.pikalti.R;

import java.util.Locale;

/**
 * A class that holds static utility methods.
 */
public final class Utilities {

    /**
     * This class should not be instantiated.
     */
    private Utilities() {
        //
    }


    /**
     * Receives a speed value in m/s and converts it to km/h.
     *
     * @param speed The speed value in m/s.
     * @return The speed value in km/h.
     */
    public static float speedToKm(float speed) {
        return (speed * 3600) / 1000;
    }

    /**
     * Receives a speed value in km/h and converts it to displayable text.
     *
     * @param context The current context.
     * @param speed The speed value in km/h.
     * @return Displayable text of the speed.
     */
    public static String formatSpeed(Context context, float speed) {
        return context.getString(
                R.string.speed_label, String.format(Locale.US, "%.2f", speed));
    }
}
