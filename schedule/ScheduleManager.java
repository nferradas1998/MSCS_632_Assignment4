package schedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleManager {

    public static void main(String[] args) {
        // defining some sample employees for demo functionality
        List<Employee> employees = new ArrayList<>();
        Employee nicolas = new Employee("Nicolas", "Ferradas");
        Employee bukayo = new Employee("Bukayo", "Saka");
        Employee lionel = new Employee("Lionel", "Messi");
        Employee michael = new Employee("Michael", "Jordan");
        Employee dax = new Employee("Dax", "Bradley");
        Employee max = new Employee("Max", "Verstaapen");
        Employee lewis = new Employee("Lewis", "Hamilton");
        Employee kai = new Employee("Kai","Havertz");
        Employee zinedine = new Employee("Zinedine", "Zidane");
        Employee cristiano = new Employee("Cristiano", "Ronaldo");

        // add them to the employee list
        employees.add(nicolas);
        employees.add(bukayo);
        employees.add(lionel);
        employees.add(michael);
        employees.add(dax);
        employees.add(max);
        employees.add(lewis);
        employees.add(zinedine);
        employees.add(kai);
        employees.add(cristiano);


        for (DayEnum day : DayEnum.values()) { // for each day, assign sample prefrences
            nicolas.setPreference(day, ShiftEnum.MORNING);
            nicolas.setPreference(day, ShiftEnum.EVENING);
            bukayo.setPreference(day, ShiftEnum.AFTERNOON);
            lionel.setPreference(day, ShiftEnum.EVENING);
            dax.setPreference(day, ShiftEnum.AFTERNOON);
            dax.setPreference(day,ShiftEnum.EVENING);
            max.setPreference(day, ShiftEnum.MORNING);
            lewis.setPreference(day, ShiftEnum.MORNING);
            lewis.setPreference(day, ShiftEnum.AFTERNOON);
            kai.setPreference(day, ShiftEnum.MORNING);
            kai.setPreference(day, ShiftEnum.AFTERNOON);
            cristiano.setPreference(day, ShiftEnum.AFTERNOON);

            // Adding some conditional prefrences for variation
            if (day.equals(DayEnum.MONDAY) || day.equals(DayEnum.WEDNESDAY)) {
                michael.setPreference(day, ShiftEnum.MORNING);
                zinedine.setPreference(day, ShiftEnum.MORNING);
            } else if (day.equals(DayEnum.TUESDAY) || day.equals(DayEnum.SATURADAY)) {
                michael.setPreference(day, ShiftEnum.AFTERNOON);
                zinedine.setPreference(day, ShiftEnum.AFTERNOON);
            } else {
                michael.setPreference(day, ShiftEnum.EVENING);
                zinedine.setPreference(day, ShiftEnum.EVENING);
            }
        }

       
        WeeklySchedule weeklySchedule = new WeeklySchedule(); // Initializing the schedule class

        // try to schedule each employee on their preferred shift for each day.
        for (DayEnum day : DayEnum.values()) {
            for (Employee employee : employees) {
                if (!employee.getAssignedDays().contains(day) && !employee.isFullyScheduled()) {
                    boolean assigned = weeklySchedule.scheduleEmployee(employee, day);
                    if (!assigned) { //if not assigned output the reason
                        System.out.println("Conflict: " + employee.getName() + " could not be scheduled on " + day +
                                " on any shift based on preferences.");
                    }
                }
            }
        }

        // Fill empty schedules with non-preferred shifts
        weeklySchedule.fillEmptyShifts(employees);

        weeklySchedule.printSchedule();
    }
    
}
