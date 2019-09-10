import java.util.*;
import java.io.*;

public class StudentNameProcess {
    public static final String FILE_NAME = "UW_DeanList_All_Modified.txt";
    public static void main (String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        Scanner fileScanner = new Scanner(new File(FILE_NAME));
        ArrayList<String[]> nameArray = new ArrayList<String[]>();
        buildArrayList(fileScanner, nameArray);
        Map<String, Integer> lastNameMap = createMap(0, nameArray);
        Map<String, Integer> firstNameMap = createMap(1, nameArray);
        Map<String, Integer> collegeMap = createMap(2, nameArray);
        int choice;
        intro();
        do {
            int num = 0;
            int num2 = 0;
            choice = inputInt(console, "Enter choice: ", "Please enter an integer from 1 to 5.");
            console = new Scanner(System.in);
            if (choice == 1) {
                findName(console, nameArray);
            } else if (choice == 2) {
                num = inputInt(console, "Enter the minimum number of occurences for a first name to " +
                                  "appear on a list: ", "Please enter a valid integer");
                printMap(firstNameMap, "first name", num);
            } else if (choice == 3) {
                num = inputInt(console, "Enter the minimum number of occurences for a last name to " +
                                  "appear on a list: ", "Please enter a valid integeru");
                printMap(lastNameMap, "last name", num);  
            } else if (choice == 4) {
                printMap(collegeMap, "college", 1);
            } else if (choice != 5) {
                System.out.println("Enter a valid integer from 1 to 5.");
            }
            System.out.println();
        } while (choice != 5);
        System.out.println("End program...");
        System.out.println("Thank you for using the program! :)");
    }
    
    public static void intro() {
        System.out.println("This program will process the name of the students in the UW Quarterly Dean's List Autumn 2018.");
        System.out.println("The student will be on the Dean's List if they have a GPA of higher than 3.5 in 12 or more graded");
        System.out.println("credits in that quarter.");
        System.out.println();
        System.out.println("The user has to type in the choices as follow: ");
        System.out.println("1 - find name in the Dean's List");
        System.out.println("2 - print out the first names, alphabetically, that has more than or equal to the number of occurrences that");
        System.out.println("    the user enters");
        System.out.println("3 - print out the last names, alphabetically, that has more than or equal to the number of occurrences that");
        System.out.println("    the user enters");
        System.out.println("4 - print out the college and their occurrences");
        System.out.println("5 - end the program");
        System.out.println();
    }
    
    public static int inputInt(Scanner console, String prompt1, String prompt2) {
        System.out.print(prompt1);
        int choice;
        while (!console.hasNextInt()) {
            console.next();
            System.out.println(prompt2);
            System.out.print(prompt1);
        }
        choice = console.nextInt();
        return choice;
    }
    
    public static void buildArrayList(Scanner fileScanner, ArrayList<String[]> nameArray) {
        fileScanner.nextLine();
        Scanner line = new Scanner(fileScanner.nextLine());
        String lastName = line.next().replace("*", " ");
        String firstName = line.next().replace("*", " ");
        String college = line.next();
        while (line.hasNext()) {
            college = college + " " + line.next(); 
        }
        String[] data = {lastName, firstName, college};
        nameArray.add(data);
        while (fileScanner.hasNextLine()) {
            String pLastName = data[0];
            String pFirstName = data[1];
            String pCollege = data[2];
            line = new Scanner(fileScanner.nextLine());
            lastName = line.next().replace("*", " ");
            firstName = line.next().replace("*", " ");
            college = line.next();
            while (line.hasNext()) {
                college = college + " " + line.next(); 
            }
            if (!lastName.equals(pLastName) || !firstName.equals(pFirstName) || 
                !college.equals(pCollege)) {
                data = new String[3];
                data[0] = lastName;
                data[1] = firstName;
                data[2] = college;
                nameArray.add(data);
            }
        }
    }
    
    public static void findName(Scanner console, ArrayList<String[]> nameArray) {
        System.out.println("Find name in UW Quarterly Dean's List Autumn 2018");
        System.out.print("Last name: ");
        String last = console.nextLine();
        System.out.print("First name: ");
        String first = console.nextLine();
        boolean found = false;
        int i = 0;
        int count = 0;
        String[] data;
        System.out.println();
        do {
            data = nameArray.get(i);
            if (data[0].toLowerCase().contains(last.toLowerCase()) && 
                data[1].toLowerCase().contains(first.toLowerCase())) {
                found = true;
                count++;
                System.out.println("Name: " + data[1] + " " + data[0]);
                System.out.println("College: " + data[2]);
                System.out.println();
            }
            i++;
        } while (i < nameArray.size());
        if (!found) {
            System.out.println("First Name and last name not found on Dean's List.");
        } else if (count == 1) {
            System.out.println("1 name found.");
        } else {
            System.out.println(count + " names found.");
        }
    }
    
    public static Map<String, Integer> createMap(int num, ArrayList<String[]> nameArray) {
        Map<String, Integer> elementMap = new TreeMap<String, Integer>();
        for (int i = 0; i < nameArray.size(); i++) {
            String element = (nameArray.get(i))[num];
            if (elementMap.containsKey(element)) {
                elementMap.put(element, elementMap.get(element) + 1);
            } else {
                elementMap.put(element, 1);
            }
        }
        return elementMap;
    }
    
    public static void printMap(Map<String, Integer> map, String description, int num) {
        System.out.print("Print unique " + description + "s and their occurrences");
        if (num > 0) {
            System.out.print(" for " + description + "s with more than or equal to " + num + " occurrence");
        } 
        if (num > 1) {
            System.out.print("s");
        }
        System.out.println(".");
        Set<String> elementSet = map.keySet();
        boolean foundMore = false;
        int count = 0;
        int countMore = 0;
        int countName = 0;
        int maxCount = 0;
        String maxElement = "";
        String capDescription = Character.toUpperCase(description.charAt(0)) + description.substring(1);
        for (String element : elementSet) {
            if (map.get(element) >= num) {
                foundMore = true;
                countName += map.get(element);
                countMore++;
                System.out.println(capDescription + ": " + element + ", occurences: " + map.get(element));
            }
            if (maxCount < map.get(element)) {
                maxElement = element;
                maxCount = map.get(element);
            }
            count++;
        }
        if (!foundMore) {
            System.out.println("There is no " + description + "s with more than or equal to " + num + " occurences.");
        }
        System.out.println("Number of unique " + description + "s: " + count);
        System.out.println("Number of " + description + "s with more than or equal to " + num + " occurrences: " + countMore);
        System.out.println("Number of occurrences of those " + description + "s: " + countName);
        System.out.println(capDescription + " with most count: " + maxElement + ", occurrences: " + maxCount);
    }
}