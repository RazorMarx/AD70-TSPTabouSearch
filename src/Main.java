
public class Main {
	
	public static void main(String[] args) {
		
		/** config 10 villes*/
		int nb_iteration = 1000;
		int nb_villes    = 10;
		int duree_tabou  = (int) Math.sqrt(nb_iteration);
		String fichierVille = "distances_entre_villes_10.txt";
		
		
		/** config 50 villes
		int nb_iteration = 10000;
		int nb_villes    = 50;
		int duree_tabou  = (int) Math.sqrt(nb_iteration);
		String fichierVille = "distances_entre_villes_50.txt";
		 */
		
		
		if(args.length == 4){
			nb_iteration = Integer.parseInt(args[0]);
			duree_tabou = Integer.parseInt(args[1]);
			nb_villes = Integer.parseInt(args[2]);
			fichierVille = args[3];
		}else if(args.length == 0){
			System.out.println("Utilisation des paramétres par défaut");
		}else{
			System.out.println("Usage : nb_iteration duree_tabou nb_villes fichierVille");
		}
		
		long timer_start = System.currentTimeMillis();
		Algo algo = new Algo(nb_iteration, duree_tabou, nb_villes, fichierVille);
		Solution best = algo.optimiser();
		best.ordonner();
		best.afficher();
		long timer_stop = System.currentTimeMillis();
		double chrono = (timer_stop-timer_start);
		chrono *= 1000000;
		System.out.println("Solution trouvé en : "+chrono+" s");
	}

}
