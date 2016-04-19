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

import services.events.ServiceEvent;

public abstract class AbstractService implements ServiceEvent {

    private boolean started;

    public AbstractService() {
    }

    public final void start() {
        if (this.started) {
            return;
        }

        this.onInit();
        this.started = true;
    }

    public final void stop() {
        if (!this.started) {
            return;
        }

        this.onDestroy();
        this.started = false;
    }

    public final void restart() {
        this.stop();
        this.start();
    }
}
