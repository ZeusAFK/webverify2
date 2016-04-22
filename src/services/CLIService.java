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

package services;

import java.util.Scanner;

import forms.MainFrame;
import utils.StringUtils;

public class CLIService extends AbstractService implements Runnable {

	private static Scanner in;
	private static boolean running;

	public CLIService() {
		onInit();
	}

	@Override
	public void onInit() {
		running = true;
		in = new Scanner(System.in);
		StringUtils.printInfo("CLIService service started");
	}

	@Override
	public void onDestroy() {
		running = false;
		StringUtils.printInfo("CLIService service stopped");
	}

	@Override
	public void run() {
		while (running) {
			while (true) {
				StringUtils.printInfo("CLI Ready, now you can start writting commands");
				String command = in.next();
				switch (command) {
				case "exit": {
					StringUtils.printInfo("Shutting down services...");
					// TODO: signal services to stop gracefully
					System.exit(0);
				}
				case "startx": {
					StringUtils.printInfo("Starting main server GUI");
					MainFrame.main(null);
					break;
				}
				default:
					StringUtils.printInfo("Unknow command '" + command + "'");
				}
			}
		}
	}

	public static CLIService getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {
		protected static final CLIService instance = new CLIService();
	}
}
