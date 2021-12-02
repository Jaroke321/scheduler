import java.util.Scanner;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.io.*;

/**
 * @author Jacob Keller
 * @author Patrick Ryan
 * @author Collin Murphy
 * @author Brad
 * 
 * @since 12/1/2021
 */
public class Scheduler {

    // Class lookup
    private Map<String, ArrayList<String>> curriculum = new HashMap<String, ArrayList<String>>();
    int maxPerSemester;

    /**
     * Constructor method
     * 
     * @param filename       File that holds the curriculum data.
     * @param maxPerSemester Integer that represents the maximum number of classes
     *                       per semester that the student can take
     */
    public Scheduler(String filename, int maxPerSemester) {

        // Set the maximum number of classes per semester the user can take
        if (maxPerSemester > 0) {
            this.maxPerSemester = maxPerSemester;
        }

        // Initialize Fiel object with file name
        File inputFile = new File(filename);

        try {
            // read from file into curriculum
            Scanner input = new Scanner(inputFile);

            // Go until the end of the file is reached
            while (input.hasNext()) {

                String line = input.nextLine(); // Get current line

                // Split line into seperate courses
                String[] str_arr = line.split(" ");
                // First value is the current course
                String course = str_arr[0].strip();
                ArrayList<String> prereqs = new ArrayList<String>();

                // Cycle through the prereq courses and add them to ArrayList
                for (int i = 1; i < str_arr.length; i++) {
                    prereqs.add(str_arr[i].strip()); // Add each pre req to list
                }
                // Add to the class lookup class variable
                this.curriculum.put(course, prereqs);
            }
        } catch (FileNotFoundException e) {
            // Handle exception
            System.out.println("[!] ERROR: That file cannot be found...");
        }

    }

    /**
     * Simple show method that will display all of the classes and their prereqs.
     * Mostly used for debugging.
     */
    public void show() {

        System.out.println("\n--------------COMPUTER SCIENCE CURRICULUM----------------\n");

        int count = 1;

        // Go through each of the courses in the curriculum and print them out
        for (Map.Entry<String, ArrayList<String>> e : this.curriculum.entrySet()) {
            String ret = String.valueOf(count) + ") ";
            ret += e.getKey() + ": \n     prerequisites => ";
            ret += e.getValue() + "\n";

            System.out.println(ret);
            count++;
        }

    }

    /**
     * Determines if the prereqs for a specific course are satisfied by comparing
     * them to all the courses already taken.
     * 
     * @param taken   All the courses the student has already taken
     * @param prereqs All the prereqs for a specific course
     * @return True if the prereqs for the course have all been taken, false
     *         otherwise
     */
    public boolean isSatisfied(ArrayList<String> taken, ArrayList<String> prereqs) {

        // Go through each prereq to check if it has been taken
        for (int i = 0; i < prereqs.size(); i++) {
            String prereq = prereqs.get(i);
            if (!taken.contains(prereq)) { // prereq course has not been taken
                return false;
            }
        }

        return true;

    }

    /**
     * Gets all of the available options given all of the courses that have already
     * been taken.
     * This corresponds to all of the courses with prereqs that have already been
     * taken
     * 
     * @param taken All courses that have been taken in previous semesters
     * @return An ArrayList containing all the courses that can be taken by the
     *         student.
     */
    public ArrayList<String> getOptions(ArrayList<String> taken) {

        // Arraylist to hold all available options
        ArrayList<String> options = new ArrayList<String>();

        // Go through each course and get the ones available to the student
        for (Map.Entry<String, ArrayList<String>> course : this.curriculum.entrySet()) {

            // Check that course has not been taken and prereqs are satisfied
            if (!taken.contains(course.getKey()) && isSatisfied(taken, course.getValue()))) { 
                

            }

        }

        return options;
    }

    /**
     * Picks courses for a semester given all available courses the student
     * qualifies for. Will not exceed the value for the maximum number of courses
     * the user can take in a semester.
     * 
     * @param options All available options for the current semester.
     * @return ArrayList of the courses picked for the current semester.
     */
    public ArrayList<String> pickCourses(ArrayList<String> options) {

        ArrayList<String> picked = new ArrayList<String>();

        // Less options than the student can take in a semester
        if (options.size() <= this.maxPerSemester) {
            return picked = options;
        } else { // More options than can be taken
            picked.addAll(options.subList(0, this.maxPerSemester));
        }

        return picked;
    }

    /**
     * Creates a schedule semester by semester based on the curriculum and the
     * maximum number of courses that can be taken each semester.
     */
    public ArrayList<ArrayList<String>> makeSchedule() {

        // Create list to hold the final schedules
        ArrayList<ArrayList<String>> schedule = new ArrayList<ArrayList<String>>();

        // Holds all of the taken courses for the student
        ArrayList<String> taken = new ArrayList<String>();
        // Get the current options available
        ArrayList<String> options = getOptions(taken);

        // Cycle until there are no more classes to take. ie options is empty
        while (!options.isEmpty()) {

            // Get the courses for the current semester
            ArrayList<String> current_semester = pickCourses(options);

            schedule.add(current_semester); // Add this semester to the schedule
            taken.addAll(current_semester); // Add this semesters courses to the taken list

            options = getOptions(taken); // Get new options for the next semester
        }

        return schedule;

    }

    /**
     * Driver function
     * 
     * @param args
     */
    public static void main(String[] args) {

        // Default input file for now
        String filename = "comp_sci.txt";
        int maxPerSemester = 5; // Maximum number of classes to take during a semester
        // Create the Scheduler object
        Scheduler scheduler = new Scheduler(filename, maxPerSemester);
        // Make the students schedule
        scheduler.show();
        // Print the schedule to the console

    }

}
