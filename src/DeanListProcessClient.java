import java.util.*;
import java.io.*;

public class DeanListProcessClient {
    private static final String[] quarters = {"fall", "winter", "spring"};
    
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        intro();
        List<DeanListObject> allDeanList = createListOfDeanLists(console);
        Collections.sort(allDeanList);
        if (!allDeanList.isEmpty()) {
            System.out.println();
            menu();
            System.out.println();
            int choice = prompt(console, "Enter your choice: ");
            int size = allDeanList.size();
            while (choice != 7) {
                if (choice < 1 || choice > 7){
                    System.out.println("Please enter a valid number from 1 to 7.");
                } else {
                    System.out.println("List of available Dean's List:");
                    for (int i = 0; i < size; i++) {
                        System.out.println((i + 1) + ". " + allDeanList.get(i));
                    }
                    System.out.println();
                    Set<DeanListObject> setOfDeanLists = new TreeSet<DeanListObject>();
                    boolean keepAdding = false;
                    if (choice > 1 && choice < 4) {
                        keepAdding = true;
                    }
                    setOfDeanLists.add(allDeanList.get(getDeanListChoice(console, size) - 1));
                    while (keepAdding) {
                        setOfDeanLists.add(allDeanList.get(getDeanListChoice(console, size) - 1));
                        keepAdding = askYesNo("Do you want to process another Dean's List? ", console);
                    }
                    if (choice == 1 || choice == 2) {
                        findName(setOfDeanLists, console, false);
                    } else if (choice == 3) {
                        findName(setOfDeanLists, console, true);
                    } else {
                        findOccurrences(setOfDeanLists, choice - 3, console);
                    }
                }
                System.out.println();
                menu();
                choice = prompt(console, "Enter your choice: ");
                System.out.println();
            }
        }
    }
    
    public static void intro() {
        System.out.println("Welcome to the UW Dean's List Program!");
        System.out.println("This program is a text-based program allowing the users to search the UW Dean's List database");
        System.out.println();
    }
    
    public static void menu() {
        System.out.println("Menu");
        System.out.println("1. Find the student names by first name, last name, and college.");
        System.out.println("2. Find the student names that are common in two or more Dean's Lists by first name, ");
        System.out.println("last name, and college.");
        System.out.println("3. Find the student names that are in at least one or more Dean's Lists by first name, ");
        System.out.println("last name, and college.");
        System.out.println("4. Find the last names that has more than or equal to the number of occurrences that");
        System.out.println("the user enters.");
        System.out.println("5. Find the first names that has more than or equal to the number of occurrences that");
        System.out.println("the user enters.");
        System.out.println("6. Find the colleges that has more than or equal to the number of occurrences that");
        System.out.println("the user enters.");
        System.out.println("7. Quit");
    }
    
    public static List<DeanListObject> createListOfDeanLists(Scanner console) throws FileNotFoundException {
        ArrayList<DeanListObject> totalDeanList = new ArrayList<DeanListObject>();
        if (askYesNo("Do you want to read a UW Dean's List file? ", console)) {
            totalDeanList.add(createDeanList(console));
            while (askYesNo("Do you want to read another UW Dean's List file? ", console)) {
                totalDeanList.add(createDeanList(console));
            }
        }
        return totalDeanList;
    }
    
    public static DeanListObject createDeanList(Scanner console) throws FileNotFoundException {
        String fileName = prompt("Input a file name: ", console);
        Scanner fileScanner = new Scanner(new File(fileName));
        String newFileName = fileName.toLowerCase().replace("_", " ");
        newFileName = newFileName.replace(".", " ");
        Scanner fileNameScanner = new Scanner(newFileName);
        String[] fileNameArray = newFileName.split(" ");
        int year = 0;
        String qtr = "";
        if (fileNameArray.length == 5 && fileNameScanner.next().equals("uw") && 
            fileNameScanner.next().equals("deanlist") &&
            quarterContains(fileNameScanner.next()) && 
            fileNameScanner.hasNextInt()) { // file in Format of "UW_DeanList_<qtr>_<year>.txt" (Case-insensitive)
            year = fileNameScanner.nextInt();
            qtr = fileNameArray[2];
        } else {
            year = prompt(console, "Enter the year: ");
            qtr = prompt("Enter the quarter: ", console).toLowerCase();
            while (!quarterContains(qtr)) {
                System.out.println("Please enter a valid quarter (Fall, Winter, or Spring), case-insensitively.");
                qtr = prompt("Enter the quarter: ", console).toLowerCase();
            }
        }
        return new DeanListObject(fileScanner, qtr, year);
    }
    
    public static boolean quarterContains(String word) {
        return arrayContains(quarters, word);
    }
    
    public static boolean arrayContains(String[] array, String word) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(word)) {
                return true;
            }
        }
        return false;
    }
    
    public static int prompt(Scanner console, String question) {
        System.out.print(question);
        while (!console.hasNextInt()) {
            System.out.println("Please input a number.");
            console.next();
            System.out.print(question);
        }
        return console.nextInt();
    }
    
    public static String prompt(String question, Scanner console) {
        System.out.print(question);
        return console.nextLine();
    }
    
    public static boolean askYesNo(String question, Scanner console) {
        String answer = prompt(question, console);
        answer = answer.toLowerCase();
        while (!answer.equals("yes") && !answer.equals("y") && !answer.equals("no") && !answer.equals("n")) {
            System.out.println("Please answer one of the following (case insensitive): yes, y, no, or n");
            answer = prompt(question, console);
            answer = answer.toLowerCase();
        }
        if (answer.charAt(0) == 'y') {
            return true;    
        } else {
            return false;
        }
    }
    
    public static int getDeanListChoice(Scanner console, int size) {
        int deanListChoice = prompt(console, "Enter the Dean's List you want to process (just the number): ");
        while (deanListChoice < 1 || deanListChoice > size) {
            System.out.println("Please enter the number as presented above.");
            deanListChoice = prompt(console, "Enter the Dean's List you want to process (just the number): ");
        }
        console.nextLine();
        return deanListChoice;
    }
    
    public static void findName(Set<DeanListObject> currentSet, Scanner console, boolean includeAll) {
        System.out.println("Hit enter for each criteria if you do not want to consider that criteria.");
        System.out.println("The search is case-insensitive.");
        String firstName = prompt("First name: ", console);
        String lastName = prompt("Last name: ", console);
        String college = prompt("College: ", console);
        Iterator<DeanListObject> iter = currentSet.iterator();
        List<String> listOfNames = iter.next().listToFindName(firstName, lastName, college);
        while (iter.hasNext()) {
            if (includeAll) {
                listOfNames.addAll(iter.next().listToFindName(firstName, lastName, college));
            } else {
                listOfNames.retainAll(iter.next().listToFindName(firstName, lastName, college));
            }
        }
        int listSize = listOfNames.size();
        if (listSize == 0) {
            System.out.println("There are no names that matches your search criteria.");
        } else if (listSize == 1) {
            System.out.println("There is a name that matches your search criteria.");
        } else {
            System.out.println("There are " + listSize + " names that match your search criteria.");
        }
        if (listSize > 0) {
            int numNames = -1;
            if (askYesNo("Do you want to see the results of the search on-screen? ", console)) {
                numNames = prompt(console, "Enter the maximum number of names to include on the screen (Enter 0 for no maximum): ");
                console.nextLine();
            }
            System.out.println();
            if (numNames == 0) {
                for (String student : listOfNames) {
                    System.out.println(student);
                }
            } else if (numNames > 0) {
                Iterator<String> iterStr = listOfNames.iterator();
                int currentCount = 0;
                while (iterStr.hasNext() && currentCount < numNames) {
                    currentCount++;
                    System.out.println(iter.next());
                }
            }
        }
    }
    
    public static void findOccurrences(Set<DeanListObject> dSet, int num, Scanner console) {
        boolean reverse = askYesNo("Do you want the list of occurrences to be in decreasing order? ", console);
        int limit = prompt(console, "What is the minimum number of occurrences to include (Enter 0 for no minimum)? ");
        Iterator<DeanListObject> iter = dSet.iterator();
        generalOccurrences(iter.next(), num, limit, reverse);
    }
    
    public static void generalOccurrences(DeanListObject d, int num, int limit, boolean reverse) {
        Map<Integer, Set<String>> results = null;
        if (num == 1) {
            results = d.getLastNameMap();
        } else if (num == 2) {
            results = d.getFirstNameMap();
        } else {
            results = d.getCollegeMap();
        }
        List<Integer> occurrencesList = new ArrayList<Integer>();
        for (int occurrence : results.keySet()) {
            occurrencesList.add(occurrence);
        }
        if (reverse) {
            Collections.reverse(occurrencesList);
        }
        int limitIndex = occurrencesList.indexOf(limit);
        if (limit == 0) {
            for (int occurrence : occurrencesList) {
                for (String word : results.get(occurrence)) {
                    System.out.println(word + ", occurrence: " + occurrence);
                }
            }
        } else if (limit > 0 && limitIndex != -1) {
            if (reverse) {
                for (int i = 0; i <= limitIndex; i++) {
                    for (String word : results.get(occurrencesList.get(i))) {
                        System.out.println(word + ", occurrence: " + occurrencesList.get(i));
                    }
                }
            } else {
                for (int i = limitIndex; i < occurrencesList.size(); i++) {
                    for (String word : results.get(occurrencesList.get(i))) {
                        System.out.println(word + ", occurrence: " + occurrencesList.get(i));
                    }
                }
            }
        } else {
            System.out.println("No occurrences.");
        }
    }
}