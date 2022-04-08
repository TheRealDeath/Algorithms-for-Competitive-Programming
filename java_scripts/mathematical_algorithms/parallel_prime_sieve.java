import java.util.ArrayList;
public class ParallelPrimeSieve
{
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
				if(tog) {s += d2; d1 += 16; p1 += 2; p2 += 2; s2 = p2;}
				else {s += d1; d2 += 8; p1 += 2; p2 += 6; s2 = p1;}
			}
		}
		int k = 0;
		int p = 5;
		tog = false;
		while(p <= n)
		{
			if(!composite[k++]) primes.add(p);
			tog = !tog;
			p += tog ? 2 : 4;
		}
		return primes;
	}
}
//Faster than prime sieve by a long shot!