
/**
 * Une solution est un parcours des différentes villes
 * dans le probléme type du voyageur de commerce
 * @author Razormarx
 *
 */
public class Solution {

	int nbVille;
	int fitness;
	int[] villes;
	boolean recommence;
	int alea;
	
	public Solution(int nbVille){
		this.nbVille = nbVille;
		this.villes = new int[nbVille];
		this.recommence = true;
		
		villes[0] = 0;
		//Init solution initiale
		//avec des ville présente une unique fois
		for(int i=1;i<nbVille;i++){
			recommence = true;			
			while(recommence){
				recommence = false;			
				alea = (int) Random.aleatoire(nbVille);
				for(int j=0;j<i;j++)
					if(alea==villes[j])
						recommence=true;						
			}
			villes[i]=alea;
		}
		ordonner();
	}
	
	/**
	 * evalue la distance totale du parcours
	 * @param distance
	 */
	public void evaluer(int[][] distance){
		fitness = 0;
		for(int i=0;i<nbVille-1;i++)
			fitness+= distance[villes[i]][villes[i+1]];
		fitness+= distance[villes[0]][villes[nbVille-1]];	
	}
	
	/**
	 * on impose arbitrairement que la 2ième ville visitée (ville[1])
	 * ait un n° plus petit que la dernière ville visitée (ville[taille-1])
     * i.e. : ville[1] > ville[taille-1]
	 */
	public void ordonner(){
		int inter, k;
		
		//On replace la ville de départ (0) au début du tableau
		if(villes[0] != 0){
			int position_ville0 = 0;
			int[] villesBuffer = new int[nbVille];
			//On cherche l'indice de la ville 0 et on remplie le buffer
			for(int i=0;i<nbVille;i++){
				villesBuffer[i] = villes[i];
				if(villes[0]==0)
					position_ville0 = i;
			}
			
			k=0;
			
			for(int i=position_ville0;i<nbVille;i++){
				villes[k] = villesBuffer[i];
				k++;
			}
			for(int i=0;i<position_ville0;i++){
				villes[k] = villesBuffer[i];
				k++;
			}
		}
		
		if(villes[1] > villes[nbVille-1])
			for(int g=1 ; g<=1+(nbVille-2)/2;g++){
				inter = villes[g];
				villes[g]=villes[nbVille-g];
				villes[nbVille-g]=inter;
			}
	}
	
	/**
	 * Affiche la solution dans la console
	 */
	public void afficher(){
		
		System.out.print(villes[0]);
		for(int i=1;i<nbVille;i++)
			System.out.print("->"+villes[i]);
		System.out.println();
		System.out.println("Parcours des "+nbVille+" villes en "+fitness+" km");
	}
	
	/**
	 * Echange deux villes dans le tableau Villes
	 * @param ville1 indice première ville
	 * @param ville2 indice seconde ville
	 */
	public void echanger_2_villes(int ville1, int ville2){
		int buffer = villes[ville1];
		villes[ville1] = villes[ville2];
		villes[ville2] = buffer;
	}
	
	/**
	 * Transforme la solution courante vers le voisin
	 * grâce au mouvement définit par le couple de ville
	 * @param ville1
	 * @param ville2
	 */
	public void deplacement_1_ville(int ville1, int ville2){
		int buffer = villes[ville1];
		if(ville1 < ville2)
			for(int k=ville1; k<ville2;k++)
				villes[k]= villes[k+1];
		else
			for(int k=ville1; k>ville2;k--)
				villes[k]= villes[k-1];
		villes[ville2] = buffer;
	}
	
	/**
	 * Inversion toute une sequence de villes dans la solution
	 * @param ville1
	 * @param ville2
	 */
	public void inversion_sequence_villes(int ville1, int ville2){
		for(int i=ville1; i<=ville1+(ville2-ville1)/2; i++)
			echanger_2_villes(i, ville1+ville2-i);
	}
	
	/**
	 * Fonction pour tester si deux solutions sont identiques
	 * @param s solution à tester avec la solution d'appel
	 * @return
	 */
	public boolean identique(Solution s){
		if(s.nbVille == this.nbVille){
			for(int i=0;i<nbVille-1;i++)
				if(s.villes[i] != this.villes[i])
					return false;
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Copie une solution dans la solution d'apelle
	 * @param source
	 */
	public void copier(Solution source){
		for(int i=0;i<nbVille;i++)
			villes[i] = source.villes[i];
	}
}
