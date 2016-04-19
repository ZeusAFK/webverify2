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

public enum ScanStatus {
	Created(1), Running(2), Failed(3), Completed(5), Deleted(0);

	private int value;

	private ScanStatus(int value) {
		this.value = value;
	}

	public static ScanStatus fromValue(int value) throws IllegalArgumentException {
		try {
			return ScanStatus.values()[value];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Unknown enum value :" + value);
		}
	}

	public int getValue() {
		return value;
	}
}
