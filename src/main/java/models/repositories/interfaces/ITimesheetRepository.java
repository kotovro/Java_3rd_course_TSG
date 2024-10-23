package models.repositories.interfaces;

import models.entities.Timesheet;

public interface ITimesheetRepository {
    void add(Timesheet Timesheet);
    Timesheet getTimesheetById(int timesheetId);
    void updateTimesheet(Timesheet timesheet);
    void deleteTimesheet(int timesheetId);

}
