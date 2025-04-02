package schedule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Employee {

    private String name;
    private String lastName;
    private Map<DayEnum, Set<ShiftEnum>> preference;
    private Set<DayEnum> assignedDays;
    private final int MAX_DAYS = 5;

    public Employee(String name, String lastName){
        this.name = name;
        this.lastName = lastName;
        this.preference = new HashMap<>();
        this.assignedDays = new HashSet<>();
    }

    public String getName(){
        return this.name;
    }

    public String getLastName(){
        return this.lastName;
    }

    public void setPreference(DayEnum day, ShiftEnum shift){
        Set<ShiftEnum> preferences;
        if(this.preference.get(day) != null){ // if the preferences are not empty, then check for existence
            preferences = this.preference.get(day);
            if(!preferences.contains(shift)){ // if the preference does not exist, add to the set
                preferences.add(shift);
            }
        } else {
            preferences = new HashSet<>(); // if preferences is null, then creare a new hashSet
            preferences.add(shift);
        }
        this.preference.put(day, preferences);
    }

    public Set<ShiftEnum> getPreference(DayEnum day){
        return this.preference.get(day);
    }

    public Set<DayEnum> getAssignedDays(){
        return this.assignedDays;
    }

    public void addAssignedDay(DayEnum day){
        this.assignedDays.add(day);
    }

    public boolean isFullyScheduled(){
        return assignedDays.size() >= MAX_DAYS;
    }

    @Override
    public String toString() { // Override the toString method to display the full name only
        return name + " " + lastName;
    }


}
