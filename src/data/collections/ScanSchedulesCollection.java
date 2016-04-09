package data.collections;

import java.util.ArrayList;

import data.models.ScanSchedule;

@SuppressWarnings("serial")
public class ScanSchedulesCollection extends ArrayList<ScanSchedule> {

	public ScanSchedule getById(int id) {
		for (ScanSchedule schedule : this) {
			if (schedule.getId() == id)
				return schedule;
		}
		return null;
	}
}
