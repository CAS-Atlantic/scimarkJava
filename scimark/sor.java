/**
 *  SciMark2: A Java numerical benchmark measuring performance
 *  of computational kernels for FFTs, Monte Carlo simulation,
 *  sparse matrix computations, Jacobi SOR, and dense LU matrix
 *  factorizations.
 *
 *  Authors: Roldan Pozo (pozo@nist.gov) and Bruce Miller (bruce.miller@nist.gov)
 *
 *  Downloaded from: https://math.nist.gov/scimark2/download.html
 *
 *  Modified by: Aaron Graham (aaron.graham@unb.ca, aarongraham9@gmail.com) and
 *                Jean-Philippe Legault (jlegault@unb.ca, jeanphilippe.legault@gmail.com)
 *                 for the Centre for Advanced Studies - Atlantic (CAS-Atlantic) at the
 *                  Univerity of New Brunswick in Fredericton, New Brunswick, Canada
*/

package scimark;

public class sor {
    public static final void execute(double omega, double G[][], long num_iterations) {
        int M = G.length;
        int N = G[0].length;

        double omega_over_four = omega * 0.25;
        double one_minus_omega = 1.0 - omega;

        // update interior points
        //
        int Mm1 = M - 1;
        int Nm1 = N - 1;
        for (int p = 0; p < num_iterations; p++) {
            for (int i = 1; i < Mm1; i++) {
                double[] Gi = G[i];
                double[] Gim1 = G[i - 1];
                double[] Gip1 = G[i + 1];
                for (int j = 1; j < Nm1; j++)
                    Gi[j] = omega_over_four * (Gim1[j] + Gip1[j] + Gi[j - 1] + Gi[j + 1])
                            + one_minus_omega * Gi[j];
            }
        }
    }

    public static double measure(int N, int itter, Random R) {
        double G[][] = R.RandomMatrix(N, N);

        Stopwatch Q = new Stopwatch();

        Q.start();
        execute(1.25, G, itter);
        Q.stop();

        return Q.read();
    }

    public static void main(String args[])
	{
		commandline cmd = new commandline(args);
		cmd.print_result("sor", sor.measure(Constants.SOR_SIZE, cmd.itter, cmd.R));
	}
}
