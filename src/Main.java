
public class Main {
	
	public static void main(String[] args) {
		
		int nb_iteration = 50;
		int duree_tabou  = 5;
		int nb_villes    = 10;
		String fichierVille = "distances_entre_villes_10.txt";

		
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
		
		Algo algo = new Algo(nb_iteration, duree_tabou, nb_villes, fichierVille);
		Solution best = algo.optimiser();
		
		long timer_start = System.currentTimeMillis();
		best.afficher();
		long timer_stop = System.currentTimeMillis();
		System.out.println("Solution trouvé en : "+(timer_stop-timer_start)*1000000+" s");
	}

}
