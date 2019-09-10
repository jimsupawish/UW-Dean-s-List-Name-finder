import java.util.*;
import java.io.*;

public class DeanListObject implements Comparable<DeanListObject> {
    private List<String[]> nameArray;
    private Map<Integer, Set<String>> lastNameMap;
    private Map<Integer, Set<String>> firstNameMap;
    private Map<Integer, Set<String>> collegeMap;
    private String qtr;
    private int year;
    
    public DeanListObject(Scanner fileScanner, String quarter, int schoolYear) throws FileNotFoundException {
         qtr = quarter;
         year = schoolYear;
         nameArray = new ArrayList<String[]>();
         if (fileScanner.hasNextLine()) {
            fileScanner.nextLine();
         }
         while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] student = line.split("\t");
            nameArray.add(student);
         }
    }
    
    public String getQtr() {
        return qtr;
    }
    
    public int getYear() {
        return year;
    }
    
    public int compareTo(DeanListObject other) {
        if (year != other.year) {
            return year - other.year;
        } else {
            return -qtr.compareTo(other.qtr);
        }
    }
    
    public String toString() {
        return "UW Dean's List " + qtr.toUpperCase().charAt(0) + qtr.substring(1) + " " + year;
    }
    
    public List<String> listToFindName(String firstName, String lastName, String college) {
        List<String> list = new ArrayList<String>();
        for (String[] student : nameArray) {
            if (student[0].toLowerCase().contains(lastName.toLowerCase()) && 
                student[1].toLowerCase().contains(firstName.toLowerCase()) &&
                student[2].toLowerCase().contains(college.toLowerCase())) {
                list.add(student[1] + " " + student[0] + ", " + student[2]);   
            }
        }
        return list;
    }
    
    private Map<Integer, Set<String>> generalOccurrences(int num) {
        Map<String, Integer> generalMap = new HashMap<String, Integer>();
        for (String[] student : nameArray) {
            String current = student[num];
            if (!generalMap.containsKey(current)) {
                generalMap.put(current, 0);
            }
            generalMap.put(current, generalMap.get(current) + 1);
        }
        Map<Integer, Set<String>> results = new TreeMap<Integer, Set<String>>();
        for (String oneStudent : generalMap.keySet()) {
            int occurrence = generalMap.get(oneStudent);
            if (!results.containsKey(occurrence)) {
                results.put(occurrence, new TreeSet<String>());
            }
            results.get(occurrence).add(oneStudent);
        }
        return results;
    }
    
    public Map<Integer, Set<String>> getLastNameMap() {
        if (lastNameMap == null) {
            lastNameMap = generalOccurrences(0);
        }
        return lastNameMap;
    }
    
    public Map<Integer, Set<String>> getFirstNameMap() {
        if (firstNameMap == null) {
            firstNameMap = generalOccurrences(1);
        }
        return firstNameMap;
    }
    
    public Map<Integer, Set<String>> getCollegeMap() {
        if (collegeMap == null) {
            collegeMap = generalOccurrences(2);
        }
        return collegeMap;
    }
}