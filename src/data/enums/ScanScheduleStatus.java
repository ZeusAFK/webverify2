/**
Copyright 2016 ZeusAFK

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package data.enums;

public enum ScanScheduleStatus {
	Deleted(0), Scheduled(1), Scanning(2), Failed(3), Completed(4);

	private int value;

	private ScanScheduleStatus(int value) {
		this.value = value;
	}

	public static ScanScheduleStatus fromValue(int value) throws IllegalArgumentException {
		try {
			return ScanScheduleStatus.values()[value];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Unknown enum value :" + value);
		}
	}

	public int getValue() {
		return value;
	}
}
