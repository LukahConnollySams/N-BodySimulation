package org.lukah.Visualisation.util;

import org.joml.Vector3f;

import java.util.List;
import java.util.stream.Collectors;

public class Conversions {

    public static Vector3f metersToAU(Vector3f vector) {

        float metersToAU = 1.0f / 1.496e11f;
        return new Vector3f(vector).mul(metersToAU);
    }

    public static float metersToAU(float num) {

        return num / 1.496e11f;
    }

    public static List<Vector3f> metersToAU(List<Vector3f> list) {

        return list.stream().map(Conversions::metersToAU).collect(Collectors.toList());
    }

    public static Vector3f hexToNormRGB(String hex) {

        String trimHex = hex.trim();

        if (trimHex.startsWith("#")) {
            trimHex = trimHex.substring(1);
        }

        if (trimHex.length() != 6) {
            throw new IllegalArgumentException("Invalid hex code colour: '" + hex + "'");
        }

        return new Vector3f(
                Integer.parseInt(trimHex.substring(0, 2), 16) / 255f,
                Integer.parseInt(trimHex.substring(2, 4), 16) / 255f,
                Integer.parseInt(trimHex.substring(4, 6), 16) / 255f);
    }

    public static float logScale(float number) {

        return (float) ((Math.log10(number + 1e-6) + 5) * 0.01);
    }
}
