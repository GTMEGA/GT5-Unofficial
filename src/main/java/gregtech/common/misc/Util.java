package gregtech.common.misc;

public final class Util {

    public static double map(double value, double srcMax, double dstMax) {
        if (value < 0.0 || Double.isNaN(value)) {
            value = 0.0;
        }

        if (value > srcMax) {
            value = srcMax;
        }

        return value / srcMax * dstMax;
    }

    public static boolean isSimilar(double a, double b) {
        return Math.abs(a - b) < 1.0E-6;
    }

    public static String toSiString(double value, int digits) {
        if (value == 0.0) {
            return "0 ";
        } else if (Double.isNaN(value)) {
            return "NaN ";
        } else {
            String ret = "";
            if (value < 0.0) {
                ret = "-";
                value = -value;
            }

            if (Double.isInfinite(value)) {
                return ret + "∞ ";
            } else {
                double log = Math.log10(value);
                double mul;
                String si;
                if (log >= 0.0) {
                    int reduce = (int)Math.floor(log / 3.0);
                    mul = 1.0 / Math.pow(10.0, (double)(reduce * 3));
                    switch (reduce) {
                        case 0:
                            si = "";
                            break;
                        case 1:
                            si = "k";
                            break;
                        case 2:
                            si = "M";
                            break;
                        case 3:
                            si = "G";
                            break;
                        case 4:
                            si = "T";
                            break;
                        case 5:
                            si = "P";
                            break;
                        case 6:
                            si = "E";
                            break;
                        case 7:
                            si = "Z";
                            break;
                        case 8:
                            si = "Y";
                            break;
                        default:
                            si = "E" + reduce * 3;
                    }
                } else {
                    int expand = (int)Math.ceil(-log / 3.0);
                    mul = Math.pow(10.0, (double)(expand * 3));
                    switch (expand) {
                        case 0:
                            si = "";
                            break;
                        case 1:
                            si = "m";
                            break;
                        case 2:
                            si = "µ";
                            break;
                        case 3:
                            si = "n";
                            break;
                        case 4:
                            si = "p";
                            break;
                        case 5:
                            si = "f";
                            break;
                        case 6:
                            si = "a";
                            break;
                        case 7:
                            si = "z";
                            break;
                        case 8:
                            si = "y";
                            break;
                        default:
                            si = "E-" + expand * 3;
                    }
                }

                value *= mul;
                int iVal = (int)Math.floor(value);
                value -= (double)iVal;
                int iDigits = 1;
                if (iVal > 0) {
                    iDigits = (int)((double)iDigits + Math.floor(Math.log10((double)iVal)));
                }

                mul = Math.pow(10.0, (double)(digits - iDigits));
                int dVal = (int)Math.round(value * mul);
                if ((double)dVal >= mul) {
                    iVal++;
                    dVal = (int)((double)dVal - mul);
                    iDigits = 1;
                    if (iVal > 0) {
                        iDigits = (int)((double)iDigits + Math.floor(Math.log10((double)iVal)));
                    }
                }

                ret = ret + Integer.toString(iVal);
                if (digits > iDigits && dVal != 0) {
                    ret = ret + String.format(".%0" + (digits - iDigits) + "d", dVal);
                }

                ret = ret.replaceFirst("(\\.\\d*?)0+$", "$1");
                return ret + " " + si;
            }
        }
    }

}
