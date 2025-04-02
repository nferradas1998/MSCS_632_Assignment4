package schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class WeeklySchedule {

    private Map<DayEnum, Map<ShiftEnum, List<Employee>>> schedule;

    public WeeklySchedule() { // Initialize the weekly schedule
        this.schedule = new HashMap<>();
        for(DayEnum day : DayEnum.values()){
            Map<ShiftEnum, List<Employee>> weeklyShifts = new HashMap<>();
            for(ShiftEnum shift : ShiftEnum.values()){
                weeklyShifts.put(shift, new ArrayList<>());
            }
            this.schedule.put(day, weeklyShifts);
        }
    }

    public void assignEmployee(DayEnum day, ShiftEnum shiftEnum, Employee employee) throws Exception {
        if(employee.getAssignedDays().contains(day) || employee.isFullyScheduled()){
            // if employee is already assigned during that day or employee is already scheduled for 5 days, throw exception
            throw new Exception("Cannot assign this employee to this shift");
        }
        List<Employee> assigned = this.schedule.get(day).get(shiftEnum);
        if(assigned.size() < 2){ // if shift is not full, assign employee
            assigned.add(employee);
            employee.addAssignedDay(day); // add the assigned day to the employee
        } else {
            // if shift is full, throw excaption as it cannot be assigned any more employees
            throw new Exception("The shift is full, cannot assign more employees");
        }
    }

    public boolean scheduleEmployee(Employee employee, DayEnum day) {
        Set<ShiftEnum> preference = employee.getPreference(day); // get employee preferences 
        for(ShiftEnum shift : preference){ // for each employee preference try to assign
            try{
                assignEmployee(day, shift, employee);
                return true; // if no exception thrown, that means that employee was assigned so we return true, no need to try on another prefrence
            } catch (Exception e){
                System.out.println("Unable to assign employee -> " + e.getMessage());
            }
        }  
        for(ShiftEnum shift : ShiftEnum.values()){ // try to assign for unpreferred schedule
            if(!preference.contains(shift)){
                try {
                    assignEmployee(day, shift, employee);
                    return true;
                } catch (Exception e){
                    System.out.println("Unable to schedule employee to unpreffered schedule -> " + day + " | " + shift);
                }
            }
        }
        return false;
    }

    public void fillEmptyShifts(List<Employee> employees){
        Random rand = new Random();
        for (DayEnum day : DayEnum.values()) {
            for (ShiftEnum shift : ShiftEnum.values()) {
                List<Employee> employeesInShift = schedule.get(day).get(shift);
                while (employeesInShift.size() < 2) {
                    // get employees that are not already assigned on this day and are not fully scheduled
                    List<Employee> candidates = employees.stream()
                            .filter(emp -> !emp.getAssignedDays().contains(day) && !emp.isFullyScheduled())
                            .collect(Collectors.toList());
                    if (candidates.isEmpty()) {
                        break;
                    }
                    Employee candidate = candidates.get(rand.nextInt(candidates.size())); // get a candidate at random
                    try{
                        assignEmployee(day, shift, candidate);
                    } catch (Exception e){
                        System.out.println("Unable to assign " + candidate.toString() + " for " + day + " in the " + shift);
                    } 
                }
            }
        }
    }

    public void printSchedule(){
        System.out.println("Final Weekly Schedule:");
        for (DayEnum day : DayEnum.values()) {
            System.out.println("\n" + day + ":");
            for (ShiftEnum shift : ShiftEnum.values()) {
                List<Employee> emps = schedule.get(day).get(shift);
                String names = emps.stream().map(Employee::toString).collect(Collectors.joining(", "));
                System.out.printf("  %s: %s%n", shift, names);
            }
        }
    }
    
}
