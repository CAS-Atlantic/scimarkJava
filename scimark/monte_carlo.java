package scimark;

/**
 Estimate Pi by approximating the area of a circle.

 How: generate N Random numbers in the unit square, (0,0) to (1,1)
 and see how are within a radius of 1 or less, i.e.
 <pre>  

 sqrt(x^2 + y^2) < r

 </pre>
  since the radius is 1.0, we can square both sides
  and avoid a sqrt() computation:
  <pre>

    x^2 + y^2 <= 1.0

  </pre>
  this area under the curve is (Pi * r^2)/ 4.0,
  and the area of the unit of square is 1.0,
  so Pi can be approximated by 
  <pre>
		        # points with x^2+y^2 < 1
     Pi =~ 		--------------------------  * 4.0
		             total # points

  </pre>

*/

public class monte_carlo
{	

	public static final double integrate(long Num_samples, Random R)
	{

		int under_curve = 0;
		for (int count=0; count<Num_samples; count++)
		{
			double x= R.nextDouble();
			double y= R.nextDouble();

			if ( x*x + y*y <= 1.0)
				 under_curve ++;
			
		}

		return ((double) under_curve / Num_samples) * 4.0;
	}

	public static double measure(int N, int itter, Random R)
	{
		Stopwatch Q = new Stopwatch();

		Q.start();
		monte_carlo.integrate(itter*N, R);
		Q.stop();

		return Q.read();
	}

    public static void main(String args[])
	{
		commandline cmd = new commandline(args);
		cmd.print_result("monte_carlo", monte_carlo.measure(Constants.MONTECARLO_ITTER, cmd.itter, cmd.R));
	}
}
