import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.math.BigInteger;

public class PrimeSplit
{
	BigInteger factorial(int n)
	{
		if(n < 2) return BigInteger.ONE;
		int log2n = 31- Integer.numberOfLeadingZeros(n);
		int proc = Runtime.getRuntime().availableProcessors();
		ExecutorService exe = Executors.newFixedThreadPool(proc);
		ArrayList<Callable<BigInteger>> tasks = new ArrayList<>();
		int high = n, low = n>>>1, shift = low, taskCounter = 0;
		while((low+1) < high)
		{
			tasks.add(new Product(low+1,high));
			high = low;
			low >>= 1;
			shift += low;
			taskCounter++;
		}
		BigInteger p = BigInteger.ONE, r = BigInteger.ZERO;
		try
		{
			List<Future<BigInteger>> products = exe.invokeAll(tasks);
			Future<BigInteger> R = exe.submit(() -> BigInteger.ONE);
			while(--taskCounter >= 0)
			{
				p = p.multiply(products.get(taskCounter).get());
				R = exe.submit(new Multiply(R.get(),p));
			}
			r = R.get();
		} catch(Throwable ignored){}
		exe.shutdown();
		return r.shiftLeft(shift);
	}
	static class Multiply implements Callable<BigInteger>
	{
		private final BigInteger a,b;
		public Multiply(BigInteger a, BigInteger b)
		{
			this.a = a;
			this.b = b;
		}
		@Override
		public BigInteger call(){return a.multiply(b);}
	}
}
final class Product implements Callable<BigInteger>
{
	private final int n,m;
	public Product(int n, int m)
	{
		this.n = n;
		this.m = m;
	}
	public BigInteger call(){return product(n,m);}
	private static BigInteger product(int n, int m)
	{
		n |= 1;
		m = (m-1) | 1;
		if(m == n) return BigInteger.valueOf(m);
		if(m == (n+2)) return BigInteger.valueOf((long) n *m);
		int k = (n+m) >>> 1;
		return product(n,k).multiply(product(k+1,m));
	}
}