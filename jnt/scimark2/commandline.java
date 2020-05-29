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
		double min_time = Constants.RESOLUTION_DEFAULT;

		int FFT_size = Constants.FFT_SIZE;
		int SOR_size =  Constants.SOR_SIZE;
		int Sparse_size_M = Constants.SPARSE_SIZE_M;
		int Sparse_size_nz = Constants.SPARSE_SIZE_nz;
		int LU_size = Constants.LU_SIZE;

		// look for runtime options

        if (args.length > 0)
        {

			if (args[0].equalsIgnoreCase("-h") || 
						args[0].equalsIgnoreCase("-help"))
			{
				System.out.println("Usage: [-large] [minimum_time]");
				return;
			}

			int current_arg = 0;
			if (args[current_arg].equalsIgnoreCase("-large"))
			{
				FFT_size = Constants.LG_FFT_SIZE;
				SOR_size =  Constants.LG_SOR_SIZE;
				Sparse_size_M = Constants.LG_SPARSE_SIZE_M;
				Sparse_size_nz = Constants.LG_SPARSE_SIZE_nz;
				LU_size = Constants.LG_LU_SIZE;

				current_arg++;
			}

			if (args.length > current_arg)
        		min_time = Double.valueOf(args[current_arg]).doubleValue();
        }
        

		// run the benchmark
		commandline.print_banner();
		System.out.printf("Using %10.2f seconds min time per kenel.", min_time);
		System.out.printf("\n\n"); 

		Results res[] = new Results[6];
		res[0] = new Results();
		Random R = new Random(Constants.RANDOM_SEED);

		res[1] = kernel.measureFFT( FFT_size, min_time, R);
 		System.out.printf("FFT             Mflops: %8.2f    (N=%d) \n", 
			res[1].res, FFT_size);

		res[2] = kernel.measureSOR( SOR_size, min_time, R);
		System.out.printf("SOR             Mflops: %8.2f    (%d x %d) \n", 
			res[2].res, SOR_size, SOR_size);

		res[3] = kernel.measureMonteCarlo(min_time, R);
		System.out.printf("MonteCarlo:     Mflops: %8.2f  \n", 
			res[3].res );

		res[4] = kernel.measureSparseMatmult( Sparse_size_M, Sparse_size_nz, min_time, R);
		System.out.printf("Sparse matmult  Mflops: %8.2f    (N=%d, nz=%d)  \n", 
			res[4].res, Sparse_size_M, Sparse_size_nz);

		res[5] = kernel.measureLU( LU_size, min_time, R);
		System.out.printf("LU              Mflops: %8.2f    (M=%d, N=%d) \n", 
			res[5].res, LU_size, LU_size);

		res[0].res = (res[1].res + res[2].res + res[3].res + res[4].res + res[5].res) / 5;
		res[0].sum = (res[1].sum + res[2].sum + res[3].sum + res[4].sum + res[5].sum) / 5;


		// print out results
		System.out.printf("\n");
		System.out.printf("************************************\n");
		System.out.printf("Composite Score:       %8.2f\n" ,res[0].res);
		System.out.printf("************************************\n");
		System.out.printf("\n");

		System.out.printf("FFT reps:              %d\n", (long)res[1].cycles);
		System.out.printf("SOR reps:              %d\n", (long)res[2].cycles);
		System.out.printf("Montel Carlo reps:     %d\n", (long)res[3].cycles);
		System.out.printf("Sparse MatMult repss:  %d\n", (long)res[4].cycles);
		System.out.printf("LU reps:               %d\n", (long)res[5].cycles);
		System.out.printf("\n");
		System.out.printf("checksum:              %20.16e\n" ,res[0].sum);


	}
  
}
