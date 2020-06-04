package scimark;

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
	public int itter;
	public Random R;

	public commandline(String args[])
	{
		// default to the (small) cache-contained version
		this.itter = Constants.RESOLUTION_DEFAULT;
		this.R = new Random(Constants.RANDOM_SEED);

		// look for runtime options

        if (args.length > 0)
        {

			if (args[0].equalsIgnoreCase("-h") || 
						args[0].equalsIgnoreCase("-help"))
			{
				System.out.println("Usage: [itteration]");
				return;
			} else {
        		this.itter = Integer.valueOf(args[0]);
			}
        }
		System.out.printf("**                                                              **\n");
		System.out.printf("** SciMark2 Numeric Benchmark, see http://math.nist.gov/scimark **\n");
		System.out.printf("** for details. (Results can be submitted to pozo@nist.gov)     **\n");
		System.out.printf("**                                                              **\n");
	}

	public void print_result(String name, double result)
	{
		System.out.printf("benchmark: %s\nitteration: %d\ntime: %8.2fms\n\n", name, this.itter,result);
	}
  
}
