package com.jfsd.debug;

/**
 * A small marks report to practise debugging on. It has a loop, several
 * methods that call each other and a few local variables that change,
 * which is what you want to watch in a debugger.
 */
public class DebugDemo {

    public static void main(String[] args) {
        String[] names = {"Asha", "Ravi", "Meena", "Kiran"};
        int[][] marks = {
                {78, 85, 92},
                {35, 48, 52},
                {88, 91, 79},
                {60, 28, 71}
        };

        int topperIndex = 0;
        double topperAverage = 0;

        for (int i = 0; i < names.length; i++) {
            int total = calculateTotal(marks[i]);
            double average = calculateAverage(total, marks[i].length);
            String grade = findGrade(average);
            boolean passedAll = hasPassedAll(marks[i], 40);

            System.out.printf("%-6s total=%3d  average=%6.2f  grade=%-2s  %s%n",
                    names[i], total, average, grade, passedAll ? "PASS" : "FAIL");

            if (average > topperAverage) {
                topperAverage = average;
                topperIndex = i;
            }
        }

        System.out.printf("%nTopper: %s with an average of %.2f%n", names[topperIndex], topperAverage);
    }

    static int calculateTotal(int[] marks) {
        int total = 0;
        for (int i = 0; i < marks.length; i++) {
            total += marks[i];
        }
        return total;
    }

    static double calculateAverage(int total, int subjects) {
        return (double) total / subjects;
    }

    static String findGrade(double average) {
        if (average >= 90) {
            return "A+";
        } else if (average >= 75) {
            return "A";
        } else if (average >= 60) {
            return "B";
        } else if (average >= 40) {
            return "C";
        }
        return "F";
    }

    static boolean hasPassedAll(int[] marks, int passMark) {
        for (int mark : marks) {
            if (mark < passMark) {
                return false;
            }
        }
        return true;
    }
}
