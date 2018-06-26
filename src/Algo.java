import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;


public class Algo {

	int iter_courante;
	int nbIteriation;
	int dureeTabou;
	int nbVille;
	Solution courante;
	int[][] distances;
	int[][] listeTabou1;
	int[][] listeTabou2;
	
	int bi;
	int bj;
	int pos;
	
	public Algo(int nbIteriation, int dureeTabou, int nbVille, String file) {
		this.nbIteriation = nbIteriation;
		this.dureeTabou = dureeTabou;
		this.iter_courante = 0;
		this.nbVille = nbVille;
		this.courante = new Solution(nbVille);
		this.construction_distances(nbVille, file);
		this.courante.evaluer(distances);
		
		listeTabou1 = new int[nbVille][nbVille];
		listeTabou2 = new int[nbVille][nbVille];
		
		//Remplie les liste vide de valeur n�gative
		for(int i=0;i<nbVille;i++){
			Arrays.fill(listeTabou1[i], -1);
			Arrays.fill(listeTabou2[i], -1);			
		}
		
		System.out.println("Solution initiale al�atoire : ");
		courante.afficher();
	}
	
	/**
	 * Construit la matrice des distance avec les donn�es contenu dans le fichier distance
	 * @param nbVilles
	 * @param file_name
	 */
	public void construction_distances(int nbVille, String file_name){
		distances = new int[nbVille][nbVille];
		try{
			InputStream flux=new FileInputStream(file_name); 
			InputStreamReader lecture=new InputStreamReader(flux);
			BufferedReader buff=new BufferedReader(lecture);
			String ligne;
			int j=0;
			while ((ligne=buff.readLine())!=null){
				String[] buffLigne = ligne.split(" ");
				for(int i=0;i<buffLigne.length;i++){
					if(!buffLigne[i].equals("")){	
						int data = Integer.parseInt(buffLigne[i]);
						distances[j][i+j+1] = data;
						distances[i+j+1][j] = data;
					}
				}
				for(int k=0;k<nbVille;k++)
					distances[k][k]=-10;
				j++;
			}
			buff.close(); 
		}	
		catch (Exception e){
			System.out.println(e.toString());
		}
	}
	
	/**
	 * V�rifie si le couple est Tabou
	 * @param i 
	 * @param j
	 * @return
	 */
	public boolean nonTabou(int i, int j){
		if(listeTabou1[i][j]<iter_courante)
			return true;
		return false;
	}
	
	/**
	 * V�rifie si la solution passer en param�tre est Tabou
	 * @param solution
	 * @return
	 */
	public boolean nonTabou2(Solution solution){
		for(int i=0;i<dureeTabou;i++)
			for(int j=1;j<nbVille;j++)
				if(listeTabou2[i][j]!=solution.villes[j])
					j = nbVille;
				else if(j == nbVille-1)
					return false;
		for(int i=0;i<dureeTabou;i++)
			for(int j=1;j<nbVille;j++)
				if(listeTabou2[i][j]!=solution.villes[nbVille-j])
					j = nbVille;
				else if(j == nbVille-1)
					return false;
		return true;
	}
	
	/**
	 * Met � jour la listeTabou2
	 * @param solution
	 * @param position
	 */
	public void majListeTabou2(Solution solution){
		if(dureeTabou != 0){
			for(int j=0; j<nbVille;j++)
				listeTabou2[pos][j] = solution.villes[j];
			pos++;
			if(pos == dureeTabou)
				pos=0;
		}
	}
	
	public void voisinage_2_opt(){
		int best_vois = 10000;
		boolean tous_tabou = true;
		
		// on s�l�ctionne une premi�re ville pour le mouvement
		for(int i=0;i<nbVille;i++)
			for(int j=i+1;j<nbVille;j++){  // on s�l�ctionne une seconde ville pour le mouvement
				if(((i!=0)||(j!=nbVille-1))
				&& ((i!=0)||(j!=nbVille-2))){
					//on transforme la solution courante vers le voisin
				    //gr�ce au mouvement d�finit par le couple de ville
					courante.inversion_sequence_villes(i, j);
					//on estime ce voisin
					courante.evaluer(distances);
					// si ce mouvement est non tabou et
				    // si ce voisin a la meilleure fitness
				    // alors ce voisin devient le meilleur voisin non tabou
					if(nonTabou(i,j) && courante.fitness<best_vois){
						best_vois  = courante.fitness;
						bi      = i;
						bj      = j;
						tous_tabou = false;
					}
					
					// on re-transforme ce voisin en la solution courante
				    courante.inversion_sequence_villes(i,j);
				    // on r�-�value la solution courante
				    courante.evaluer(distances);
				}
			}
	}
	
	public Solution optimiser(){
		
		boolean first            = true; //indique si c'est la premiere fois
										 //que l'on est dans un mimium local
		boolean descente         = false;// indique si la solution courzntz corresponds � une descente
		int ameliore_solution = -1;      // indique l'iteration o� l'on a am�lior� la solution
		int f_avant, f_apres;            // valeurs de la fitness avant et apr�s une it�ration
		// La meilleure solution trouv�e (= plus petit minium trouv�) � conserver
		Solution best_solution = new Solution(nbVille);
		int best_eval = courante.fitness;
		f_avant       = 10000000;

		// Tant que le nombre d'it�rations limite n'est pas atteint
		for(iter_courante=0; iter_courante<nbIteriation; iter_courante++){
			voisinage_2_opt();            // La fonction 'voisinage_2_opt' retourne le meilleur
			//mouvement non tabou; c'est le couple (best_i, best_j)
			courante.inversion_sequence_villes(bi, bj);
			//  On d�place la solution courante gr�ce � ce mouvement

			courante.ordonner();                        // On r�ordonne la solution en commen�ant par 0
			courante.evaluer(distances);            // On �value la nouvelle solution courante

			f_apres = courante.fitness;                 // valeur de la fitness apres le mouvement

			if(courante.fitness < best_eval){          // si on am�liore le plus petit minimum rencontr�
			                                           // alors on l'enregistre dans 'best_solution'
				best_eval = courante.fitness;            // on mets � jour 'best_eval'
				best_solution.copier(courante);          // on enregistre la solution corante comme best_solution
				best_solution.evaluer(distances);        // on �value la best solution
				ameliore_solution = iter_courante;       // on indique que l'am�lioration � eu lieu � cette it�ration
			}else{ 									   // Si on n'est pas dans le plus petit minimum
													   //rencontr� mais dans un minimum local
			
			  // Crit�res de d�tection d'un minimum local. 2 cas:
			  //  1. si la nouvelle solution est + mauvaise que l'ancienne
			  //         et que on est en train d'effectuer une descente
			  //  2. si la nouvelle solution est identique � l'ancienne
			  //         et que c'est la premi�re fois que cela se produit
			  if (    ((f_avant<f_apres)&&(descente==true))
				  || ((f_avant == f_apres)&&(first)) ){
			      
			      System.out.print("On est dans un minimum local a l'iteration "+(iter_courante-1));
			      System.out.print(" -> min = " + f_avant);
			      System.out.print(" km (le + petit min local deja trouve = "+best_eval+" km)");
			      first = false;
		      }

			  if (f_avant<=f_apres)  // la solution courente se d�grade
			    descente = false;
			  else
			    descente = true;   // la solution courante s'am�liore : descente

			  if ((f_avant!=f_apres)&&(!first))
			    first = true;
			  
		      // mise � jour de la liste tabou
		      listeTabou1[bi][bj] = iter_courante+dureeTabou;
		      //mise_a_jour_liste_tabou_2(courant, position);
		      f_avant = f_apres; 

		    }
		  
		}
		return best_solution;
	}
	
}
