package jnt.scimark2;

public class kernel
{
	// each measurement returns approx Mflops


	public static Results measureFFT(int N, int itter, Random R)
	{
		Results output = new Results();

		// initialize FFT data as complex (N real/img pairs)
		double x[] = RandomVector(2*N, R);
		double oldx[] = NewVectorCopy(x);
		Stopwatch Q = new Stopwatch();


		Q.start();
		for (int i=0; i<itter; i++)
		{
			FFT.transform(x);	// forward transform
			FFT.inverse(x);		// backward transform
		}
		Q.stop();
		output.estimated_time = Q.read();

		// approx Mflops

		final double EPS = 1.0e-10;
		if ( FFT.test(x) / N > EPS )
			output.res = 0.0;
		else
			output.res = FFT.num_flops(N)*itter/ output.estimated_time * 1.0e-6;

        for (int i=0; i<2*N; i++)
        {
            output.sum += x[i];
        }
        output.sum /= 2*N;

		return output;
	}


	public static Results measureSOR(int N, int itter, Random R)
	{
		Results output = new Results();
		double G[][] = RandomMatrix(N, N, R);

		Stopwatch Q = new Stopwatch();

		Q.start();
		SOR.execute(1.25, G, itter);
		Q.stop();

		// approx Mflops
		output.estimated_time =  Q.read();
		output.res = SOR.num_flops(N, N, itter) / output.estimated_time * 1.0e-6;
        for (int i=0; i<N; i++)
          for (int j=0; j<N; j++)
              output.sum += G[i][j];
        output.sum /= (N*N);

		return output;
	}

	public static Results measureMonteCarlo(int itter, Random R)
	{
		Results output = new Results();
		Stopwatch Q = new Stopwatch();

		Q.start();
		output.sum += MonteCarlo.integrate(itter);
		Q.stop();

		// approx Mflops
		output.estimated_time =  Q.read();
		output.res = MonteCarlo.num_flops(itter) / output.estimated_time * 1.0e-6;
		return output;
	}


	public static Results measureSparseMatmult(int N, int nz, 
			int itter, Random R)
	{
		Results output = new Results();
		// initialize vector multipliers and storage for result
		// y = A*y;

		double x[] = RandomVector(N, R);
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

		int nr = nz/N; 		// average number of nonzeros per row
		int anz = nr *N;   // _actual_ number of nonzeros

			
		double val[] = RandomVector(anz, R);
		int col[] = new int[anz];
		int row[] = new int[N+1];

		row[0] = 0;	
		for (int r=0; r<N; r++)
		{
			// initialize elements for row r

			int rowr = row[r];
			row[r+1] = rowr + nr;
			int step = r/ nr;
			if (step < 1) step = 1;   // take at least unit steps


			for (int i=0; i<nr; i++)
				col[rowr+i] = i*step;
				
		}

		Stopwatch Q = new Stopwatch();

		Q.start();
		SparseCompRow.matmult(y, val, row, col, x, itter);
		Q.stop();

		// approx Mflops
		output.estimated_time =  Q.read();
		output.res = SparseCompRow.num_flops(N, nz, itter) / output.estimated_time * 1.0e-6;

        for (int i=0; i<N; i++)
            output.sum += y[i];
        output.sum /= N;

		return output;
	}


	public static Results measureLU(int N, int itter, Random R)
	{
		Results output = new Results();
		// compute approx Mlfops, or O if LU yields large errors

		double A[][] = RandomMatrix(N, N,  R);
		double lu[][] = new double[N][N];
		int pivot[] = new int[N];

		Stopwatch Q = new Stopwatch();

		Q.start();
		for (int i=0; i<itter; i++)
		{
			CopyMatrix(lu, A);
			LU.factor(lu, pivot);
		}
		Q.stop();

		output.estimated_time =  Q.read();


		// verify that LU is correct
		double b[] = RandomVector(N, R);
		double x[] = NewVectorCopy(b);

		LU.solve(lu, pivot, x);

		final double EPS = 1.0e-12;
		if ( normabs(b, matvec(A,x)) / N > EPS )
			output.res = 0.0;
		else //return approx Mflops
			output.res = LU.num_flops(N) * itter / output.estimated_time * 1.0e-6;

        for (int i=0; i<N; i++)
          for (int j=0; j<N; j++)
            output.sum += lu[i][j];
        output.sum /= (N*N);

		return output;
	}


  private static double[] NewVectorCopy(double x[])
  {
		int N = x.length;

		double y[] = new double[N];
		for (int i=0; i<N; i++)
			y[i] = x[i];

		return y;
  }
	
  private static void CopyVector(double B[], double A[])
  {
		int N = A.length;

		for (int i=0; i<N; i++)
			B[i] = A[i];
  }


  private static double normabs(double x[], double y[])
  {
		int N = x.length;
		double sum = 0.0;

		for (int i=0; i<N; i++)
			sum += Math.abs(x[i]-y[i]);

		return sum;
  }

  private static void CopyMatrix(double B[][], double A[][])
  {
        int M = A.length;
        int N = A[0].length;

		int remainder = N & 3;		 // N mod 4;

        for (int i=0; i<M; i++)
        {
            double Bi[] = B[i];
            double Ai[] = A[i];
			for (int j=0; j<remainder; j++)
                Bi[j] = Ai[j];
            for (int j=remainder; j<N; j+=4)
			{
				Bi[j] = Ai[j];
				Bi[j+1] = Ai[j+1];
				Bi[j+2] = Ai[j+2];
				Bi[j+3] = Ai[j+3];
			}
		}
  }

  private static double[][] RandomMatrix(int M, int N, Random R)
  {
  		double A[][] = new double[M][N];

        for (int i=0; i<N; i++)
			for (int j=0; j<N; j++)
            	A[i][j] = R.nextDouble();
		return A;
	}

	private static double[] RandomVector(int N, Random R)
	{
		double A[] = new double[N];

		for (int i=0; i<N; i++)
			A[i] = R.nextDouble();
		return A;
	}

	private static double[] matvec(double A[][], double x[])
	{
		int N = x.length;
		double y[] = new double[N];

		matvec(A, x, y);

		return y;
	}

	private static void matvec(double A[][], double x[], double y[])
	{
		int M = A.length;
		int N = A[0].length;

		for (int i=0; i<M; i++)
		{
			double sum = 0.0;
			double Ai[] = A[i];
			for (int j=0; j<N; j++)
				sum += Ai[j] * x[j];

			y[i] = sum;
		}
	}

}
