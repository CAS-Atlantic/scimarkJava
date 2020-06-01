package jnt.scimark2;

import java.util.Properties;

/**
	SciMark2: A Java numerical benchmark measuring performance
	of computational kernels for FFTs, Monte Carlo simulation,
	sparse matrix computations, Jacobi SOR, and dense LU matrix
	factorizations.  


*/


public class commandline
{

  /* Benchmark 5 kernels with individual Mflops.
	 "results[0]" has the average Mflop rate.

  */

	public static void print_banner()
	{
		System.out.printf("**                                                              **\n");
		System.out.printf("** SciMark2 Numeric Benchmark, see http://math.nist.gov/scimark **\n");
		System.out.printf("** for details. (Results can be submitted to pozo@nist.gov)     **\n");
		System.out.printf("**                                                              **\n");
	}

	public static void main(String args[])
	{
		// default to the (small) cache-contained version
		int itter = Constants.RESOLUTION_DEFAULT;
		Random R = new Random(Constants.RANDOM_SEED);

		// look for runtime options

        if (args.length > 0)
        {

			if (args[0].equalsIgnoreCase("-h") || 
						args[0].equalsIgnoreCase("-help"))
			{
				System.out.println("Usage: [itteration]");
				return;
			} else {
        		itter = Integer.valueOf(args[0]);
			}
        }
        

		// run the benchmark
		commandline.print_banner();
		System.out.printf("Using %d itter per kenel.", itter);
		System.out.printf("\n\n"); 

		// print out results
		System.out.printf("FFT time:              %8.2f\n", 
			kernel.measureFFT( Constants.FFT_SIZE, itter, R));

		System.out.printf("SOR time:              %8.2f\n",
			kernel.measureSOR( Constants.SOR_SIZE, itter, R));

		System.out.printf("Montel Carlo time:     %8.2f\n", 
			kernel.measureMonteCarlo10k(itter, R));

		System.out.printf("Sparse MatMult time:  %8.2f\n", 
			kernel.measureSparseMatmult( Constants.SPARSE_SIZE_M, Constants.SPARSE_SIZE_nz, itter, R));

		System.out.printf("LU time:               %8.2f\n", 
			kernel.measureLU( Constants.LU_SIZE, itter, R));

		System.out.printf("\n");


	}
  
}
