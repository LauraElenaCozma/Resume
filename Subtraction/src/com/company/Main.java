package com.company;

import java.io.*;
import java.util.*;
public class Main {
    //declare the data of the problem as static members of the class
    private static List<Integer> A;
    private static int M;

    public static void read(String nameOfFile) throws IOException {
        //read the values from the file having the name nameOfFile
        A = new ArrayList<>();
        BufferedReader fin = new BufferedReader(new FileReader(nameOfFile));
        String line;
        line = fin.readLine();                         //first line contains M
        String[] stringM = line.split(" ");
        M = Integer.parseInt(stringM[0]);              //get M from the first line
        while((line = fin.readLine()) != null) {       //read the values from the array
            String[] elem = line.split(" ");
            for(String e : elem)                       //add the elements into the list A
                A.add(Integer.parseInt(e));
        }
    }
    public static void hashMethod() {
        /*
        Time complexity:
        Best case time complexity: the values are distinct, so get and put methods have time-complexity O(1) in HashMap
        When the pairs are formed, each key has a list with one value, so it takes O(1) time to form the pair
        => total time is O(n)
        Worst case time complexity: the values are not distinct, there are many duplicates
        If the array is {1, 1, 1, 2, 2, 2, 2} (has only 2 values) the time-complexity to form the pairs will be O(n^2)
        => total time is O(n^2)
        Space complexity: O(n) for A and O(n) for map => O(n)
         */
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(int i = 0 ; i < A.size() ; i++) {
            if (!map.containsKey(A.get(i))) {
                //if the map doesn't contain the key, add the pair (value, ArrayList) into the map
                //create the corresponding ArrayList containing the index i of the value
                List<Integer> index = new ArrayList<>();
                index.add(i);
                map.put(A.get(i), index);
            }
            else {
                //map has a key == A.get(i); add only the index into the list
                List<Integer> index = map.get(A.get(i));
                index.add(i);
            }
        }
        for(Map.Entry<Integer,List<Integer>> hms : map.entrySet()) {
            //iterate through hashmap
            int key1 = hms.getKey();
            //key1 - key2 = M => key2 = key1 - M
            int key2 = key1 - M;
            //key1 pair is in the map
            if(map.containsKey(key2)) {
                List<Integer> list1 = hms.getValue();  //the corresponding indexes of key1
                List<Integer> list2 = map.get(key2);   //the corresponding indexes of key2
                for(int elem1 : list1)
                    for(int elem2 : list2) {           //form the pairs of indexes
                        System.out.println("(" + elem1 + "," + elem2 + ")");
                    }
            }
        }
    }
    public static void sortMethod() {
        /*
        Time complexity:
        Best case time complexity: the values are distinct, sort will take O(nlogn), iterate through list + binary search for each element => O(nlogn)
                                   If the values are distinct, copy won't go to left or to right to seach values = val2 to form the pairs, so forming the pairs will take O(1)
        Worst case time complexity: the values are not distinct, there are many duplicates
                                    It will take O(n) to seach elements = val2, so the total time complexity will be O(n^2)
        => total time is O(n^2)
        Space complexity: O(n) for A and O(n) to store pairs of (index, value)
        */
        //create a list of pairs representing the index of the value in the initial order and the value
        List<Map.Entry<Integer, Integer>> pairs = new ArrayList<>();
        for(int i = 0 ; i < A.size() ; i++)
            pairs.add(Map.entry(i, A.get(i)));
        //sort the pairs in ascending order after the value
        pairs.sort(Comparator.comparingInt(Map.Entry::getValue));
        for(Map.Entry<Integer,Integer> pair : pairs) {
            //searching for the pairs (val1, val2), val1 - val2 = M
            int val1 = pair.getValue();
            int val2 = val1 - M;
            //we have val1, search with binary search if there is val2 into the list => O(log n)
            int index = Collections.binarySearch(pairs, Map.entry(-1, val2), Comparator.comparingInt(Map.Entry::getValue));
            if(index >= 0) {   //val2 was found
                System.out.println("(" + pair.getKey() + " " + pairs.get(index).getKey() + ")");
                //go to the right to form the pairs if val2 has duplicates
                int copy = index - 1;
                while(copy >= 0 && pairs.get(copy).getValue().equals(pairs.get(index).getValue())) {
                    System.out.println("(" + pair.getKey() + " " + pairs.get(copy).getKey() + ")");
                    copy--;
                }
                //go to the right to form the pairs if val2 has duplicates
                copy = index + 1;
                while(copy < pairs.size() && pairs.get(copy).getValue().equals(pairs.get(index).getValue())) {
                    System.out.println("(" + pair.getKey() + " " + pairs.get(copy).getKey() + ")");
                    copy++;
                }
            }
        }
    }
    public static void main(String[] args) {
        try {
            read("data.in");

            System.out.println("Enter 1 for Hash Method and 2 for Sort Method");
            Scanner in = new Scanner(System.in);
            int methodUsed = in.nextInt();
            //Hash Method has best time complexity O(n) because HashMap is used
            //while Sort Method has best time complexity O(nlogn) because of sort(O(nlogn)) and binary search into the for loop(O(nlogn))
            if(methodUsed == 1)
                hashMethod();
            else if (methodUsed == 2)
                sortMethod();

        } catch (IOException e) {
            System.out.println("Error when reading from file");
        }

    }
}