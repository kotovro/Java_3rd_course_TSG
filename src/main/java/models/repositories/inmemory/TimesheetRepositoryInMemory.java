package models.repositories.inmemory;

import models.entities.Timesheet;
import models.repositories.interfaces.ITimesheetRepository;

import java.util.LinkedList;
import java.util.List;

public class TimesheetRepositoryInMemory implements ITimesheetRepository {
    List<Timesheet> timesheets = new LinkedList<>();

    @Override
    public void add(Timesheet Timesheet) {

    }

    @Override
    public Timesheet getTimesheetById(int timesheetId) {
        return null;
    }

    @Override
    public void updateTimesheet(Timesheet timesheet) {

    }

    @Override
    public void deleteTimesheet(int timesheetId) {

    }
}
