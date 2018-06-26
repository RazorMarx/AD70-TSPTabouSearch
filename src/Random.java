
public class Random {

	static java.util.Random random = new java.util.Random();
	
	static void randinit(){
		random.setSeed(System.currentTimeMillis());
	}
	
	static long aleatoire(long borne){
		long alea = random.nextLong()%borne;
		if(alea < 0) alea *= -1;
		return(alea);
	}
	
	
}
