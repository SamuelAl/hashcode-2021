import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Solution {

    // Constants
    private static final String FILENAME = "./e.txt";
    private static final String FILENAME_SOL = "./e_solution.txt";

    public static void main(String[] args) throws Exception {
        Solution solution = new Solution();
        solution.solve();
    }

    private void solve() {
        // solution of the problem
        // variables
        int simDuration, nIntersections, nStreets, nCars, bonusPointCar = 0;
        
        Map<Integer, List<String>> intersectionMap = new HashMap<Integer, List<String>>();
        Map<String, Integer> roadLoadsMap = new HashMap<String, Integer>();
        // Scan in variables on first line
        Scanner scan = null;
        try {
            File myFile = new File(FILENAME);
            scan = new Scanner(myFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        simDuration = scan.nextInt();
        nIntersections = scan.nextInt();
        nStreets = scan.nextInt();
        nCars = scan.nextInt();
        bonusPointCar = scan.nextInt();

        // SAMUEL
        for (int i = 0; i < nStreets; i++) {
            int start = scan.nextInt();
            int finish = scan.nextInt();
            String street = scan.next();
            int time = scan.nextInt();

            if (intersectionMap.containsKey(finish)) {
                intersectionMap.get(finish).add(street);
            }
            else {
                List<String> streetList = new LinkedList<String>();
                streetList.add(street);
                intersectionMap.put(finish, streetList);
            }
        }

        for (int i = 0; i < nCars; i++) {
            int numRoads = scan.nextInt();
            for (int j = 0; j < numRoads; j++) {
                String name = scan.next();
                roadLoadsMap.put(name, roadLoadsMap.getOrDefault(name, 0) + 1);
            }
        }

        String sol = scheduleIntersections(roadLoadsMap, intersectionMap, simDuration);
       // System.out.println(sol);
        writeToFile(sol, FILENAME_SOL);
        scan.close();

    }
    
    // Utility function to write filess
    private void writeToFile(String str, String fileName)  {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(str);
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    private String scheduleIntersections(Map<String, Integer> nCars, Map<Integer, List<String>> intersections, int totalTime)
    {
        int seconds = (int)(totalTime / 1.5);
        StringBuilder output = new StringBuilder();
        int nInter = 0;
        //output.append(intersections.keySet().size() + "\n");
        for(int i : intersections.keySet())
        {
            
            int numberCarsIntersection = 0;
            //List<Integer> roadTime = new ArrayList<Integer>();
            List<String> roadsIn = intersections.get(i);
            
            int counter = 0;
            for(String road : roadsIn)
            {
                if(nCars.containsKey(road))
                {
                    counter++;
                    numberCarsIntersection += nCars.get(road);
                }
            }
            if (counter != 0) {
                nInter++;
                output.append(i + "\n");
                output.append(counter + "\n");
                for(String road : roadsIn)
                {
                    if(nCars.containsKey(road)) {
                        double prop = (int)((double)nCars.get(road)/(double)numberCarsIntersection * seconds);
                        System.out.println(prop);
                        System.out.println(seconds);
                        System.out.println(prop * seconds);
                        output.append(road + " " + ((int)prop) + "\n");
                    }
                }
            }
            
            //Iterator roadIterator = roadsIn.iterator();
            //Iterator timeIterator = roadTime.iterator();
            //while (roadIterator.hasNext() && timeIterator.hasNext())
            //{
            //    output.append(roadIterator.next() + " " + timeIterator.next() + "\n");
            //}

        }
        output.insert(0, nInter + "\n");
        return output.toString();
    }
}

