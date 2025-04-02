from enum import Enum
import random

class ShiftEnum(Enum):
    MORNING = 0
    AFTERNOON = 1
    EVENING = 2

class DayEnum(Enum):
    MONDAY = 0
    TUESDAY = 1
    WEDNESDAY = 2
    THURSDAY = 3
    FRIDAY = 4
    SATURDAY = 5
    SUNDAY = 6

class Employee:
    def __init__(self, name, last_name):
        self.name = name
        self.last_name = last_name
        self.preferences = {}  # Dictionary to map days to shifts
        self.assigned_days = set()
    
    def set_preference(self, day, shift):
        self.preferences[day] = shift
    
    def get_preference(self, day):
        return self.preferences.get(day)
    
    def is_fully_scheduled(self):
        return len(self.assigned_days) >= 5
    
    def __str__(self):
        return self.name, " ", self.last_name

class WeeklySchedule:
    def __init__(self):
        # Create a nested dictionary: Day -> {Shift -> list of Employees}
        self.schedule = {day: {shift: [] for shift in ShiftEnum} for day in DayEnum}
    
    def assign_employee(self, day, shift, employee):
        # Check if the employee is already assigned on this day or reached max days.
        if day in employee.assigned_days or employee.is_fully_scheduled():
            return False
        
        # Ensure each shift gets a maximum of 2 employees initially.
        if len(self.schedule[day][shift]) < 2:
            self.schedule[day][shift].append(employee)
            employee.assigned_days.add(day)
            return True
        
        return False

    def schedule_employee(self, employee, day):
        preferred = employee.get_preference(day)
        if self.assign_employee(day, preferred, employee):
            return True
        else:
            # Preferred shift is full; try the other shifts.
            for shift in ShiftEnum:
                if shift != preferred and self.assign_employee(day, shift, employee):
                    return True
        return False
    
    def fill_shifts(self, employees):
        # Ensure each shift has at least 2 employees.
        for day in DayEnum:
            for shift in ShiftEnum:
                while len(self.schedule[day][shift]) < 2:
                    # Find available candidates: not assigned on the day and haven't reached 5 days.
                    candidates = [emp for emp in employees if day not in emp.assigned_days and not emp.is_fully_scheduled()]
                    if not candidates:
                        break  # No candidates available.
                    candidate = random.choice(candidates)
                    self.assign_employee(day, shift, candidate)
    
    def print_schedule(self):
        print("Final Weekly Schedule:")
        for day in DayEnum:
            print(f"\n{day.name}:")
            for shift in ShiftEnum:
                employees_in_shift = self.schedule[day][shift]
                names = ", ".join(emp.name for emp in employees_in_shift)
                print(f"  {shift.name}: {names}")

def main():

    employees = []

    # Create employees.
    nicolas = Employee("Nicolas", "Ferradas")
    bukayo = Employee("Bukayo", "Saka")
    lionel = Employee("Lionel", "Messi")
    michael = Employee("Michael", "Jordan")
    dax = Employee("Dax", "Bradley")
    max_emp = Employee("Max", "Verstaapen")
    lewis = Employee("Lewis", "Hamilton")
    kai = Employee("Kai", "Havertz")
    zinedine = Employee("Zinedine", "Zidane")
    cristiano = Employee("Cristiano", "Ronaldo")

    # Add employees to the list.
    employees.extend([nicolas, bukayo, lionel, michael, dax, max_emp, lewis, zinedine, kai, cristiano])

    # Assign sample shift preferences for each day.
    for day in DayEnum:
        nicolas.set_preference(day, ShiftEnum.MORNING)
        bukayo.set_preference(day, ShiftEnum.AFTERNOON)
        lionel.set_preference(day, ShiftEnum.EVENING)
        dax.set_preference(day, ShiftEnum.AFTERNOON)
        max_emp.set_preference(day, ShiftEnum.MORNING)
        lewis.set_preference(day, ShiftEnum.MORNING)
        kai.set_preference(day, ShiftEnum.MORNING)
        cristiano.set_preference(day, ShiftEnum.AFTERNOON)

        # Adding some conditional preferences for variation.
        if day == DayEnum.MONDAY or day == DayEnum.WEDNESDAY:
            michael.set_preference(day, ShiftEnum.MORNING)
            zinedine.set_preference(day, ShiftEnum.MORNING)
        elif day == DayEnum.TUESDAY or day == DayEnum.SATURDAY:
            michael.set_preference(day, ShiftEnum.AFTERNOON)
            zinedine.set_preference(day, ShiftEnum.AFTERNOON)
        else:
            michael.set_preference(day, ShiftEnum.EVENING)
            zinedine.set_preference(day, ShiftEnum.EVENING)

    # Create a Schedule instance
    weekly_schedule = WeeklySchedule()

    # schedule each employee for each day based on their preferences.
    for day in DayEnum:
        for emp in employees:
            if day not in emp.assigned_days and not emp.is_fully_scheduled():
                assigned = weekly_schedule.schedule_employee(emp, day)
                if not assigned:
                    print(f"Conflict: {emp.name} could not be scheduled on {day.name} based on preferences.")

    # fill any shifts that have fewer than 2 employees.
    weekly_schedule.fill_shifts(employees)

    weekly_schedule.print_schedule()

if __name__ == "__main__":
    main()