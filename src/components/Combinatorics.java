package components;

/**
 * Provides methods to calculate combinatorics formulas.
 *
 * @author 150009974
 * @version 1.1
 */
public final class Combinatorics {

    /** Hides the constructor for this utility class. */
    private Combinatorics() {
    }

    /**
     * Calculates and returns the N-choose-K operation.
     * The combination of K elements from a sample of N.
     *
     * @param n the total amount of elements N
     * @param k the chosen amount of elements K
     * @return number of combinations in which K elements can be chosen from N
     */
    public static long nChooseK(final int n, final int k) {
        long total = 1;
        int above = n;
        int lower = Math.min(k, n - k);
        for (int i = 0; i < lower; i++) {
            total *= above;
            total /= (i + 1);
            above--;
        }
        return total;
    }

    /**
     * Calculates and returns the Bernstein Coefficient
     * of the given degree for the given index and value.
     *
     * @param n the degree of the Bernstein Coefficient
     * @param i the index in the Bernstein Polynomial
     * @param u the value for the Bernstein Coefficient
     * @return the nth degree Bernstein Coefficient for index i and value u
     */
    public static double bernsteinCoefficient(final int n,
                                              final int i,
                                              final double u) {
        if (i < 0 || i > n) {
            return 0;
        }
        long binomialCoefficient = nChooseK(n, i);
        double ui = Math.pow(u, i);
        double u1ni = Math.pow(1 - u, n - i);
        return binomialCoefficient * ui * u1ni;
    }

    /**
     * Calculates and returns the derivative of the Bernstein Coefficient
     * of the given degree for the given index and value.
     *
     * @param n the degree of the Bernstein Coefficient
     * @param i the index in the Bernstein Polynomial
     * @param u the value for the Bernstein Coefficient
     * @return the derivative of the
     * nth degree Bernstein Coefficient for index i and value u
     */
    public static double bernsteinDerivative(final int n,
                                             final int i,
                                             final double u) {
        double bv1n1 = bernsteinCoefficient(n - 1, i - 1, u);
        double bvn1 = bernsteinCoefficient(n - 1, i, u);
        return n * (bv1n1 - bvn1);
    }

    /**
     * Calculates and returns the second derivative of the Bernstein Coefficient
     * of the given degree for the given index and value.
     *
     * @param n the degree of the Bernstein Coefficient
     * @param i the index in the Bernstein Polynomial
     * @param u the value for the Bernstein Coefficient
     * @return the second derivative of the
     * nth degree Bernstein Coefficient for index i and value u
     */
    public static double bernsteinSecondDerivative(final int n,
                                                   final int i,
                                                   final double u) {
        double bv1n1 = bernsteinDerivative(n - 1, i - 1, u);
        double bvn1 = bernsteinDerivative(n - 1, i, u);
        return n * (bv1n1 - bvn1);
    }
}
