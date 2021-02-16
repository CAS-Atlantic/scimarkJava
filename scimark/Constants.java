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

public class Constants
{
	public static final int RESOLUTION_DEFAULT = 30;  /*itterm*/
	public static final int RANDOM_SEED = 101010;

	// large (out-of-cache) problem sizes
	//
	public static final int FFT_SIZE = 1048576;  // must be a power of two
	public static final int SOR_SIZE = 1000; // NxN grid
	public static final int SPARSE_SIZE_M = 100000;
	public static final int SPARSE_SIZE_nz = 1000000;
	public static final int LU_SIZE = 1000;
	public static final int MONTECARLO_ITTER = 1000000;
}

