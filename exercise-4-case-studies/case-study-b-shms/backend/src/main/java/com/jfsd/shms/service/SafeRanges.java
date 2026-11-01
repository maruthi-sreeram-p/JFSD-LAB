package com.jfsd.shms.service;

import com.jfsd.shms.model.Severity;
import com.jfsd.shms.model.VitalReading;

import java.util.ArrayList;
import java.util.List;

/**
 * The rules that decide when a reading is outside the safe range.
 *
 * <pre>
 *                      safe range      critical when
 *   Heart rate         60 - 100 bpm    below 40 or above 130
 *   Systolic pressure  90 - 140 mmHg   below 80 or above 180
 *   Diastolic pressure 60 - 90 mmHg    below 50 or above 120
 *   Oxygen level       95 - 100 %      below 90
 *   Temperature        36.1 - 37.5 C   below 35.0 or above 39.0
 * </pre>
 *
 * A value outside the safe range but not critical is a WARNING.
 * These are general adult values, a doctor may need different limits for a patient.
 */
public final class SafeRanges {

    /** One value that is out of range. */
    public record Finding(String key, String parameter, String value, Severity severity, String message) {
    }

    private SafeRanges() {
    }

    public static List<Finding> check(VitalReading r) {
        List<Finding> findings = new ArrayList<>();
        test(findings, "heartRate", "Heart rate", "bpm", r.getHeartRate(), 60, 100, 40, 130);
        test(findings, "systolic", "Systolic pressure", "mmHg", r.getSystolic(), 90, 140, 80, 180);
        test(findings, "diastolic", "Diastolic pressure", "mmHg", r.getDiastolic(), 60, 90, 50, 120);
        test(findings, "oxygenLevel", "Oxygen level", "%", r.getOxygenLevel(), 95, 100, 90, 101);
        test(findings, "temperature", "Temperature", "°C", r.getTemperature(), 36.1, 37.5, 35.0, 39.0);
        return findings;
    }

    /** The worst severity among the findings, or null when everything is in range. */
    public static Severity worst(List<Finding> findings) {
        Severity worst = null;
        for (Finding f : findings) {
            if (f.severity() == Severity.CRITICAL) {
                return Severity.CRITICAL;
            }
            worst = Severity.WARNING;
        }
        return worst;
    }

    private static void test(List<Finding> findings, String key, String name, String unit, double value,
                             double min, double max, double criticalLow, double criticalHigh) {
        boolean low = value < min;
        boolean high = value > max;
        if (!low && !high) {
            return;
        }
        Severity severity = (value < criticalLow || value > criticalHigh) ? Severity.CRITICAL : Severity.WARNING;
        String shown = format(value) + " " + unit;
        String message = name + " " + shown + " is " + (low ? "below" : "above")
                + " the safe range (" + format(min) + " - " + format(max) + " " + unit + ")";
        findings.add(new Finding(key, name, shown, severity, message));
    }

    private static String format(double v) {
        return v == Math.floor(v) ? String.valueOf((long) v) : String.valueOf(v);
    }
}
