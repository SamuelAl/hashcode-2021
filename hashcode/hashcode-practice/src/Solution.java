import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Solution {

    static final String FILENAME = "./e_many_teams.in";
    
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        try {
            solution.solve();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
            
    }

    private void solve() throws Exception {
        // get pizzas

        In input = new In(FILENAME);
        int numberOfPizzas = input.readInt();
        int team_2 = input.readInt();
        int team_3 = input.readInt();
        int team_4 = input.readInt();
        input.readLine();

        // DATA PREPARATION
        List<Integer> pizzaList = new ArrayList<Integer>();
        Map<Integer, Long> pizzaIngredientsMap = new HashMap<Integer, Long>();
        Map<String, Integer> ingredientMap = mapIngredientsToIdx();
        Set<Long> uniqueIngredientPatterns = new HashSet<Long>(); 
        
        for (int i = 0; i < numberOfPizzas; i++) {
            String line = input.readLine();
            String[] ingredientArray = line.split(" ");
            int pizzaId = i;
            pizzaList.add(pizzaId);
            long encodedIngredients = encodeIngredients(ingredientArray, ingredientMap);
            pizzaIngredientsMap.put(pizzaId, encodedIngredients);
        }
        
        Map<Long, List<Integer>> ingredientsToPizzaMap = mapListPizzas(pizzaIngredientsMap);
        
        // SOLUTION
        StringBuilder solution = new StringBuilder();
        int teamCounter = 0;
        
        // teams of 4
        long currIngredients = 0;
        int[] nextPizza = {0};
        for (int team = 0; team < team_4; team++) {
            if (pizzaList.size() < 4) break;
            solution.append("4 ");
            currIngredients = 0;
            nextPizza[0] = 0;      
            for (int person = 0; person < 4; person++) {
                long nextPizzaIngredients = getBestCombination(nextPizza, ingredientsToPizzaMap, currIngredients);
                solution.append(nextPizza[0] + " ");
                pizzaList.remove((Integer)nextPizza[0]);
                currIngredients |= nextPizzaIngredients;
            }
            teamCounter++;
            solution.append("\n");
           
        }
        
        
        // team of 3
        for (int team = 0; team < team_3; team++) {
            if (pizzaList.size() < 3) break;
            solution.append("3 ");
            currIngredients = 0;
            nextPizza[0] = 0;        
            for (int person = 0; person < 3; person++) {
                long nextPizzaIngredients = getBestCombination(nextPizza, ingredientsToPizzaMap, currIngredients);
                solution.append(nextPizza[0] + " ");
                pizzaList.remove((Integer)nextPizza[0]);
                currIngredients |= nextPizzaIngredients;
            }
            teamCounter++;
            solution.append("\n");
           
        }
        // team of 2
        for (int team = 0; team < team_2; team++) {
            if (pizzaList.size() < 2) break;
            solution.append("2 ");
            currIngredients = 0;
            nextPizza[0] = 0;
            for (int person = 0; person < 2; person++) {
                long nextPizzaIngredients = getBestCombination(nextPizza, ingredientsToPizzaMap, currIngredients);
                solution.append(nextPizza[0] + " ");
                pizzaList.remove((Integer)nextPizza[0]);
                currIngredients |= nextPizzaIngredients;
            }
            teamCounter++;
            solution.append("\n");
        }

        solution.insert(0, teamCounter + "\n");
        System.out.println(solution);
        WriteToFile(solution.toString(), "e_output");
    }

    private void WriteToFile(String str, String fileName) 
        throws IOException {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(str);
            writer.close();
}
    
    private int calculateDifference (long pizzaOne, long pizzaTwo)
    {
        long bitwiseDiff = pizzaOne ^ pizzaTwo;
        return Long.bitCount(bitwiseDiff);
    }

    private String[] getIngredientsArray() {
        In input = new In(FILENAME);
        int numberOfPizzas = input.readInt();
        input.readLine();
        Set<String> ingredients = new HashSet<String>();
        for (int i = 0; i < numberOfPizzas; i++) {
            String line = input.readLine();
            String[] ingredientArray = line.split(" ");
            for (int j = 1; j < ingredientArray.length; j++) {
                ingredients.add(ingredientArray[j]);
            }
        }
        return ingredients.toArray(new String[ingredients.size()]);
    }

    private Map<String, Integer> mapIngredientsToIdx() {
        String[] ingredientArray = getIngredientsArray();
        Map<String, Integer> ingredientMap = new HashMap<String, Integer>();
        for (int idx = 0; idx < ingredientArray.length; idx++) {
            ingredientMap.put(ingredientArray[idx], idx);
        }
        return ingredientMap;
    }

    private static void printArray(Object[] array) {
        for (Object obj : array) {
            System.out.print(obj.toString() + " ");
        }
    }

    private long encodeIngredients(String[] pizzaIngredients, Map<String, Integer> ingredientMap) {
        long encodedPizza = 0;
        for(int i = 1; i < pizzaIngredients.length; i++) {
            encodedPizza += (1 << ingredientMap.get(pizzaIngredients[i]));
        }
        return encodedPizza;
    }

    private Map<Long, List<Integer>> mapListPizzas(Map<Integer, Long> ingredientMap) {
        
        Map<Long, List<Integer>> map =  new HashMap<Long, List<Integer>>();
        
        for (Integer pizza : ingredientMap.keySet()) {
            Long code = ingredientMap.get(pizza);
            if (map.containsKey(code)) {
                map.get(code).add(pizza);
            }
            else {
                List<Integer> pizzaList = new LinkedList<Integer>();
                pizzaList.add(pizza);
                map.put(code, pizzaList);
            }
        }

        return map;
    }
    
    private long getBestCombination(int[] pizzaCode, Map<Long, List<Integer>> listMapPizza, long pizzaIngredients){
        long maxDiffIngredients= pizzaIngredients;
        int maxDiff = 0;
        for (Long ingre : listMapPizza.keySet()) {
            int diff = calculateDifference(pizzaIngredients, ingre);
            if (diff > maxDiff){
                
                maxDiff = diff;
                maxDiffIngredients = ingre;
                
            }
        }
        List<Integer> pizzaList;
        if (maxDiff == 0) {
            Long key = listMapPizza.keySet().iterator().next();
            pizzaList = listMapPizza.get(listMapPizza.keySet().iterator().next());
        }
        else {
            pizzaList = listMapPizza.get(maxDiffIngredients);
        }
          
        pizzaCode[0] = pizzaList.remove(0);
        if (pizzaList.isEmpty()) {
            listMapPizza.remove(maxDiffIngredients);
        }
        return maxDiffIngredients;
    }
}





  /* private int[][] buildDiffMatrix(long[] uniquePizzas)
    {
        int[][] diffMatrix = new int[uniquePizzas.length][uniquePizzas.length];
        for(int i = 0; i < uniquePizzas.length; i++)
        {
            for (int j = 0; j < i; j++)
            {
                diffMatrix[i][j] = calculateDifference(uniquePizzas[i], uniquePizzas[j]);
            }
        }
        return diffMatrix;
    } */