



import java.text.DecimalFormat;

/**
 * @author pasadores
 * 
 */
public class Graph<T> {
	private T[] nodes; // Vector de nodos
	private boolean[][] edges; // matriz de aristas
	private double[][] weights; // matriz de pesos
	private int numNodes; // número de elementos en un momento dado
	
	private double[][] aFloyd;
	private int[][] pFloyd;

	/**
	 * 
	 * @param tam
	 *            Número máximo de nodos del grafo
	 */
	public Graph(int tam) {
		nodes = (T[]) new Object[tam];
		edges = new boolean[tam][tam];
		weights = new double[tam][tam];
		
	}

	/**
	 * Obtiene el índice de un nodo en el vector de nodos
	 *
	 * ¡¡¡ OJO que es privado porque depende de la implementación !!!
	 * 
	 * @param node
	 *            el nodo que se busca
	 * @return la posición del nodo en el vector ó -1 si no lo encuentra
	 */
	private int getNode(T node) {
		if (node==null) {
			return -1;
		} else {
			for (int i = 0; i < numNodes; i++) {
				if (nodes[i] != null) {
					if (nodes[i].equals(node)) {
						return i;
					}
				}
			}
			return -1;
		}
	}

	/**
	 * Inserta un nuevo nodo que se le pasa como parámetro, en el vector de
	 * nodos, si existe no lo inserta
	 * 
	 * @param node
	 *            el nodo que se quiere insertar
	 * @return 0 si lo inserta, -1 si no puede insertarlo
	 */
	public int addNode(T node) {
		if (numNodes >= nodes.length || existNode(node) || node==null) {
			return -1;
		} else {
			for (int i = 0; i < numNodes; i++) {
				edges[i][numNodes] = false;
				edges[numNodes][i] = false;
				weights[i][numNodes] = 0.0;
				weights[numNodes][i] = 0.0;
			}
			nodes[numNodes] = node;
			numNodes++;
			return 0;
		}
	}

	/**
	 * Borra el nodo deseado del vector de nodos así como las aristas de las que
	 * forma parte
	 * 
	 * @param node
	 *            que se quiere borrar
	 * @return 0 si lo borra o -1 si no lo hace
	 */
	public int removeNode(T node) {

		if (!existNode(node)) {
			return -1;
		} else {
			int i = getNode(node);
			numNodes--;
			if (i != numNodes + 1) {
				nodes[i] = nodes[numNodes];
				nodes[numNodes] = null;
				for (int j = 0; j < numNodes; j++) {
					edges[j][i] = edges[j][numNodes];
					edges[i][j] = edges[numNodes][j];
					weights[j][i] = weights[j][numNodes];
					weights[i][j] = weights[numNodes][j];

					edges[j][numNodes] = false;
					edges[numNodes][j] = false;
					weights[j][numNodes] = 0;
					weights[numNodes][j] = 0;
				}
				edges[i][i] = edges[numNodes][numNodes];
				weights[i][i] = weights[numNodes][numNodes];
				weights[numNodes][numNodes] = 0;
				edges[numNodes][numNodes] = false;
			}
			return 0;
		}
	}

	/**
	 * @param node
	 *            Nodo que se quiere consultar
	 * @return si existe o no el nodo
	 */
	public boolean existNode(T node) {
		return getNode(node) != -1;
	}

	/**
	 * Comprueba si existe una arista entre dos nodos que se pasan como
	 * parámetro
	 * 
	 * @param source
	 *            nodo origen
	 * @param target
	 *            nodo destino
	 * @return verdadero o falso si existe o no, si alguno de los nodos no
	 *         existe también falso
	 */
	public boolean existEdge(T source, T target) {
		return getEdge(source, target) != -1;
	}

	/**
	 * Inserta una arista con el peso indicado (> 0) entre dos nodos, uno origen
	 * y otro destino. Si la arista existe, le cambia el peso. Devuelve 0 si la
	 * inserta (o cambia el peso) y -1 si no lo hace
	 * 
	 * @param source
	 *            nodo origen
	 * @param target
	 *            nodo destino
	 * @param edgeWeight
	 *            peso de la arista, debe ser > 0
	 * @return 0 si lo hace y -1 si no lo hace (también si el peso es < 0)
	 */
	public int addEdge(T source, T target, double edgeWeight) {
		int i = getNode(source);
		int j = getNode(target);
		if (i == -1 || j == -1 || edgeWeight < 0) {
			return -1;
		} else {
			edges[i][j] = true;
			weights[i][j] = edgeWeight;
			return 0;
		}
	}

	/**
	 * 
	 * @return Retorna la matriz "edges".
	 */
	public boolean[][] getEdges() {
		return edges;
	}
	/**
	 * 
	 * @return Retorna la matriz "weights".
	 */
	public double[][] getWeights() {
		return weights;
	}

	/**
	 * Borra una arista del grafo que conecta dos nodos
	 * 
	 * @param source
	 *            nodo origen
	 * @param target
	 *            nodo destino
	 * @return 0 si la borra o -1 si no lo hace (también si no existe alguno de
	 *         sus nodos)
	 */
	public int removeEdge(T source, T target) {
		int i = getNode(source);
		int j = getNode(target);
		if (i == -1 || j == -1 || !existEdge(source, target)) {
			return -1;
		} else {
			edges[i][j] = false;
			weights[i][j] = 0.0;
			return 0;
		}
	}

	/**
	 * Devuelve el peso de la arista que conecta dos nodos, si no existe la
	 * arista, devuelve -1 (también si no existe alguno de los nodos)
	 * 
	 * @param source
	 * @param target
	 * @return El peso de la arista o -1 si no existe
	 */
	public double getEdge(T source, T target) {
		int i = getNode(source);
		int j = getNode(target);
		if (i == -1 || j == -1) {
			return -1;
		} else {
			if (edges[i][j] == true) {
				return weights[getNode(source)][getNode(target)];
			}
		}
		return -1;
	}


	/**
	 * Aplica el algoritmo de dijkstra al grafo dado
	 * @param nodoOrigen Dado del cual va a partir el algoritmo.
	 * @return	Retorna un vector que contiene las distancias minimas aplicando el algoritmo
	 */
	public double[] dijkstra(T nodoOrigen) 
    {
    	if(nodoOrigen == null || getNode(nodoOrigen)== -1) return null;
    	   	
    	double[] d = new double[nodes.length];
    	int[] p = new int[nodes.length];
    	boolean[] s= new boolean[nodes.length];
    	
    	s[getNode(nodoOrigen)] = true;
    	for (int i = 0; i < nodes.length; i++)
		{
			if(i==0)
			{
				d[i]= 0;
			}
			else{
				if (edges[getNode(nodoOrigen)][i] && i!= 0)
				{  
					d[i] = weights[getNode(nodoOrigen)][i];
					p[i] = getNode(nodoOrigen);}
			
				else {
					d[i] =Double.POSITIVE_INFINITY;
					p[i] = -1;				
				}
			}  
		}   	
    	for(int w= minCost(d,s); w != -1; w = minCost(d,s)){
    		s[w] = true;   	   		
    		for (int i = 1 ; i < nodes.length;i++)
    		{
    			if (edges[w][i]){   				
    				double pesoOld = d[i];
    				double pesoNew = d[w] + weights[w][i] ;    				
    				if (pesoOld > pesoNew ){
    					d[i] = pesoNew;
    					p[i] = w;    					
    				}
    			}   			
    		} 
    	}  	
    	return d;   
    }




  
   
	/**
     * Busca el nodo con distancia mínima en D al resto de nodos
     * @param dist  vector d
     * @param v      vector con visitados (conjunto S)
     * @return índice del nodo buscado o -1 si el grafo es no conexo o no quedan nodos sin visitar
     */
    private int minCost(double[] dist, boolean[] v) 
    {
    		double min = Double.POSITIVE_INFINITY ;
    		double posMin = -1;
    		for (int i = 0; i < dist.length; i++){
    			if (!(dist[i] == Double.POSITIVE_INFINITY)){
    				if (!v[i]){    				
    					if(min > dist[i] ){
    						min = dist[i];
    						posMin = i;
    					}
    				}
    			}
    		}   		
    		return (int)posMin;
    }
    
    	
    
    
  
    /**
     * Aplica el algoritmo de Floyd al grafo actual
     * 
     * @return la matriz A de Floyd
     */
    public double[][] floyd() { 
    	
    	double[][] a = new double[numNodes][numNodes];
    	int[][] p = new int[numNodes][numNodes];
    	
    	for (int ai = 0; ai < numNodes; ai++){
    		for (int j = 0 ; j < numNodes; j++){
    			if (ai == j) {
    				a[ai][j] = 0; //Valores de la diagonal a 0. 
    				p[ai][j]= -1;	
    			}
    			
    			else{
    				if (edges[ai][j]){ //Si existe la arista.
    					a[ai][j]= weights[ai][j];
    					p[ai][j]= ai;
    				}
    				else { //Si no existe la arista.
    					a[ai][j] = Double.POSITIVE_INFINITY;
    					p[ai][j]= -1;
    				}
    			}   			
    		}
    	}
    	
    		for (int k=0; k<numNodes; k++){
    			for (int i=0; i<numNodes; i++){
    				for (int j=0; j<numNodes; j++){ 
    					if (a[i][k] + a[k][j] < a[i][j])
    					{
    						a[i][j] = a[i][k] + a[k][j]; 
    						p[i][j] = k;
    					}   			
    				}  		
    			}    
    		}
    		
    		
    		setA(a);
    		setP(p);
    		
    		return a;
    		
    	}

    
    /**
     * Metodo auxiliar para modificar la matriz pFloyd
     * @param P matriz p generada por floyd
     */
	private void setP(int[][] P) {
		this.pFloyd = P;	
	}
	/**
	 * Metodo auxiliar para modificar la matrix aFloyd
	 * @param A matriz a generada por floyd
	 */
	private void setA(double[][] A) {
		this.aFloyd = A;
		
	}
	
	
	
	
    
    /**
     * Indica el camino entre los nodos que se le pasan como parámetros de esta forma:
     * Origen<tab>(coste0)<tab>Intermedio1<tab>(coste1)….IntermedioN<tab>(costeN) Destino
     *     Si no existen ambos nodos mostrar "No Procede"
     * 
     * @param origen
     * @param destino
     * @return Distancia entre los nodos origen y destino según Floyd si hay camino
     *     Infinito si no hay camino y -1 si no existen ambos nodos
     */
    public double path(T origen, T destino) {
    	int i = getNode(origen);
    	int j = getNode(destino);
    	
    	floyd(); //Hacemos floyd para tener los caminos minimos.
    	
    	if( i == -1 && j == -1){
    		System.out.println("No procede");
    		return -1;
    	}
    	if (!existeCamino(i,j)){
    		tratarNodo(i);
    		System.out.print("("+ Double.POSITIVE_INFINITY +")" +"\t");
    		tratarNodo(j);
    		System.out.println("");
    		return Double.POSITIVE_INFINITY;
    	}
    	
    	if ( i == j ){
    		tratarNodo(i);
    		System.out.print("(0)" + "\t");
    		return 0.0;
    	}
    	
    	
    	
    	if(i != -1 && j != -1 && i != j){
    		tratarNodo(i);
    		double res = printPath(i,j);
    		System.out.println();
    		return res;
        }
    	
    		
    		
    	
    	
    	return -1;
    	
		
    }

    	
    /**
     * Calcula si existe o no el camino entro los nodos i y j
     * @param i Nodo origen 
     * @param j Nodo destino
     * @return true si hay camino, false si no lo hay.
     */
    private boolean existeCamino(int i, int j) {
		int k = pFloyd[i][j];
		
		if (i == j)return true;
		
		if (k == i) return true;
		
    	if(k != -1 && k != i){
    		if(existeCamino(i,k) && existeCamino(k,j)){
    			return true;
    		}
    		
    	}
    	if(k == -1 && i != j && k != i){
    		return false;
    	}
    	return true;
	}
    /**
     * Calcula el camino entre dos nodos
     * @param i Nodo origen
     * @param j Nodo destino
     * @return	Camino entre los dos nodos
     */
	private double printPath(int i, int j){
    	int k = pFloyd[i][j];
    	double res = -1.0;
    	
    	if(k == i){
    		System.out.print("(" + aFloyd[i][j]+ ")" +"\t");
    		tratarNodo(j);
    		return aFloyd[i][j];
    	}
    	  
    	if(k != -1 && k != i ){
    		res = printPath(i, k) + printPath(k, j);   		
    		
    		
    	}
    	return res;
    }
    
    
    
	/**
	 * Devuelve el nodo por consola
	 * @param k nodo a tratar
	 */
	private void tratarNodo(int k) {
        System.out.print(nodes[k].toString()+"\t"); 
    }


	/**
	 * Devuelve un String con la información del grafo
	 */
	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("#.##");
		String cadena = "";

		cadena += "VECTOR NODOS\n";
		for (int i = 0; i < numNodes; i++) {
			cadena += nodes[i].toString() + "\t";
		}
		cadena += "\n\nMATRIZ ARISTAS\n";
		for (int i = 0; i < numNodes; i++) {
			for (int j = 0; j < numNodes; j++) {
				if (edges[i][j])
					cadena += "T\t";
				else
					cadena += "F\t";
			}
			cadena += "\n";
		}
		cadena += "\nMATRIZ PESOS\n";
		for (int i = 0; i < numNodes; i++) {
			for (int j = 0; j < numNodes; j++) {
				cadena += df.format(weights[i][j]) + "\t";
			}
			cadena += "\n";
		}
		return cadena;
	}

}
