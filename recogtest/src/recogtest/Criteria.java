package recogtest;


public class Criteria {
    public static Double criteriafigure(Double m) {
        //Determine the revised value
        Double m1 = m - 10.245;
        if (m1 > 0) {
            //The figure
            Double n = 0.01894 * Math.pow(m1, 3.0) - 1.1 * Math.pow(m1, 2.0) + 30.35 * m1 + 3.097;
            return n;
        } else {
            System.out.println("The value out of Range!");
            System.exit(1);
        }
        return null;
    }
}