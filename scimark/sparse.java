package scimark;

public class sparse {
    /* computes  a matrix-vector multiply with a sparse matrix
            held in compress-row format.  If the size of the matrix
            in MxN with nz nonzeros, then the val[] is the nz nonzeros,
            with its ith entry in column col[i].  The integer vector row[]
            is of size M+1 and row[i] points to the begining of the
            ith row in col[].
    */

    public static void matmult(
            double y[], double val[], int row[], int col[], double x[], long NUM_ITERATIONS) {
        int M = row.length - 1;

        for (int reps = 0; reps < NUM_ITERATIONS; reps++) {
            for (int r = 0; r < M; r++) {
                double sum = 0.0;
                int rowR = row[r];
                int rowRp1 = row[r + 1];
                for (int i = rowR; i < rowRp1; i++) sum += x[col[i]] * val[i];
                y[r] = sum;
            }
        }
    }

    public static double measure(int N, int nz, int itter, Random R) {
        // initialize vector multipliers and storage for result
        // y = A*y;

        double x[] = R.RandomVector(N);
        double y[] = new double[N];

        // initialize square sparse matrix
        //
        // for this test, we create a sparse matrix wit M/nz nonzeros
        // per row, with spaced-out evenly between the begining of the
        // row to the main diagonal.  Thus, the resulting pattern looks
        // like
        //             +-----------------+
        //             +*                +
        //             +***              +
        //             +* * *            +
        //             +** *  *          +
        //             +**  *   *        +
        //             +* *   *   *      +
        //             +*  *   *    *    +
        //             +*   *    *    *  +
        //             +-----------------+
        //
        // (as best reproducible with integer artihmetic)
        // Note that the first nr rows will have elements past
        // the diagonal.

        int nr = nz / N; // average number of nonzeros per row
        int anz = nr * N; // _actual_ number of nonzeros

        double val[] = R.RandomVector(anz);
        int col[] = new int[anz];
        int row[] = new int[N + 1];

        row[0] = 0;
        for (int r = 0; r < N; r++) {
            // initialize elements for row r

            int rowr = row[r];
            row[r + 1] = rowr + nr;
            int step = r / nr;
            if (step < 1) step = 1; // take at least unit steps

            for (int i = 0; i < nr; i++) col[rowr + i] = i * step;
        }

        Stopwatch Q = new Stopwatch();

        Q.start();
        matmult(y, val, row, col, x, itter);
        Q.stop();

        // approx Mflops
        return Q.read();
    }
    
    public static void main(String args[])
	{
		commandline cmd = new commandline(args);
		cmd.print_result("sparse", sparse.measure(Constants.SPARSE_SIZE_M, Constants.SPARSE_SIZE_nz, cmd.itter, cmd.R));
	}
}
