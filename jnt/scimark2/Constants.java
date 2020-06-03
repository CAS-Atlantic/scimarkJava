package jnt.scimark2;

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

