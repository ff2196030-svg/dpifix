package com.example.dpifix;

import android.util.DisplayMetrics;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage {
    static final String TARGET = "com.gg2.dcruz";
    static final float DPI = 309f;

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpp) {
        if (!lpp.packageName.equals(TARGET)) return;

        XposedHelpers.findAndHookMethod(android.content.res.Resources.class,
            "getDisplayMetrics", new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam p) {
                    DisplayMetrics m = (DisplayMetrics) p.getResult();
                    m.xdpi = DPI; m.ydpi = DPI;
                }
            });

        XC_MethodHook fill = new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam p) {
                DisplayMetrics m = (DisplayMetrics) p.args[0];
                m.xdpi = DPI; m.ydpi = DPI;
            }
        };
        XposedHelpers.findAndHookMethod(android.view.Display.class,
            "getMetrics", DisplayMetrics.class, fill);
        XposedHelpers.findAndHookMethod(android.view.Display.class,
            "getRealMetrics", DisplayMetrics.class, fill);
    }
}
