//fast up to 600000
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class ParallelPrimeSwing
{
	private int[] smallSwing = {1, 1, 1, 3, 3, 15, 5, 35, 35, 315, 63, 693, 231, 3003, 429, 6435, 6435, 109395, 12155, 230945, 46189, 969969, 88179, 2028117, 676039, 16900975, 1300075, 35102025, 5014575, 145422675, 9694845, 300540195, 300540195};
	BigInteger factorialPS(int n)
	{
		if(n == 0) return BigInteger.ONE;
		if(n < 20)
		{
			ArrayList<Integer> myList = new ArrayList<>();
			for(int i = 2;i<n+1;i++) myList.add(i);
			return product(0,myList.size(),myList);
		}
		int N = n;
		int bits = n;
		while(N != 0)
		{
			bits -= N & 1;
			N >>= 1;
		}
		ArrayList<Integer> primes = advancedSieve(n);
		return oddFactorial(n,primes).shiftLeft(bits);
	}
	int isqrt(int x)
	{
		int n = x,s = Integer.toBinaryString(n).length(), a = s/2,b = s%2;
		x = (int)Math.pow(2,(a+b));
		if(n == 0) return 0;
		while(true)
		{
			int y = (x + n/x) / 2;
			if(y >= x) return x;
			x = y;
		}
	}
	BigInteger swing(int n, ArrayList<Integer> primes)
	{
		if(n < 33) return BigInteger.valueOf(smallSwing[n]);
		int s = bisectLeft(primes, 1 + isqrt(n),0,primes.size());
		int d = bisectLeft(primes, 1 + n/3,0,primes.size());
		int e = bisectLeft(primes, 1 + n/2,0,primes.size());
		int g = bisectLeft(primes, 1+ n,0,primes.size());
		ArrayList<Integer> factors = new ArrayList<>();
		for(int i = e;i<g;i++) factors.add(primes.get(i));
		for(int i = s;i<d;i++) if(((n/primes.get(i)) & 1) == 1) factors.add(primes.get(i));
		for(int i = 1;i<s;i++)
		{
			int p = 1, q = n;
			while(true)
			{
				q /= primes.get(i);
				if(q == 0) break;
				if((q&1) == 1) p *= primes.get(i);
			}
			if(p > 1) factors.add(p);
		}
		return product(0,factors.size()-1,factors);
	}
	public BigInteger product(int a, int b, ArrayList<Integer> factors)
	{
		int n = b-a;
		if(n < 24)
		{
			BigInteger p = BigInteger.ONE;
			for(int i = a;i<b+1;i++)
				p = p.multiply(BigInteger.valueOf(factors.get(i)));
			return p;
		}
		int m = (a+b)/2;
		return product(a,m,factors).multiply(product(m+1,b,factors));
	}
	public BigInteger oddFactorial(int n, ArrayList<Integer> primes)
	{
		if(n < 2) return BigInteger.ONE;
		BigInteger tmp = oddFactorial(n/2,primes);
		return (tmp.multiply(tmp)).multiply(swing(n,primes));
	}
	public ArrayList<Integer> advancedSieve(int n)
	{
		ArrayList<Integer> primes = new ArrayList<>();
		primes.add(2);
		primes.add(3);
		int lim = n/3;
		boolean tog = false;
		boolean[] composite = new boolean[lim];
		int d1 = 8, d2 = 8, p1 = 3, p2 = 7, s = 7, s2 = 3, m = -1;
		while(s < lim)
		{
			m++;
			if(!composite[m])
			{
				for(int i = s;i<lim;i+=(p2+p1)) composite[i] = true;
				for(int i = s+s2;i<lim;i+=(p2+p1)) composite[i] = true;
				tog = !tog;
				if(tog)
				{
					s += d2;
					d1 += 16;
					p1 += 2;
					p2 += 2;
					s2 = p2;
				}
				else
				{
					s += d1;
					d2 += 8;
					p1 += 2;
					p2 += 6;
					s2 = p1;
				}
			}
		}
		int k = 0;
		int p = 5;
		tog = false;
		while(p <= n)
		{
			if(!composite[k]) primes.add(p);
			k += 1;
			tog = !tog;
			p += tog ? 2 : 4;
		}
		return primes;
	}
	public static int bisectLeft(ArrayList<Integer> A, int x, int lo, int hi)
	{
    int N = A.size();
    if (N == 0) return 0;
    if (x < A.get(lo))  return lo;
    if (x > A.get(hi-1)) return hi;
    for (;;) 
		{
      if (lo + 1 == hi) return x == A.get(lo) ? lo : (lo + 1);
      int mi = (hi + lo) / 2;
      if (x <= A.get(mi))  hi = mi;
      else lo = mi;
    }
	}
}