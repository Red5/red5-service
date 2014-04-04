/*
 * RED5 Open Source Flash Server - http://code.google.com/p/red5/
 * 
 * Copyright 2006-2014 by respective authors (see below). All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.red5.daemon;

import java.util.Scanner;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.red5.server.net.rtmp.RTMPMinaIoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Launch the Engine from a variety of sources, either through a main() or invoked through
 * Apache Daemon.
 */
public class EngineLauncher implements Daemon {
	
	private static Logger log = LoggerFactory.getLogger(RTMPMinaIoHandler.class);

	private static Engine engine = null;

    private static EngineLauncher engineLauncherInstance = new EngineLauncher();

    /**
     * The Java entry point.
     * @param args Command line arguments, all ignored.
     */
    public static void main(String[] args) {
        // the main routine is only here so I can also run the app from the command line
        engineLauncherInstance.initialize();

        Scanner sc = new Scanner(System.in);
        // wait until receive stop command from keyboard
        System.out.printf("Enter 'stop' to halt: ");
        while(!sc.nextLine().toLowerCase().equals("stop"));

        if (!engine.isStopped()) {
            engineLauncherInstance.terminate();
        }

    }

    /**
     * Static methods called by prunsrv to start/stop
     * the Windows service.  Pass the argument "start"
     * to start the service, and pass "stop" to
     * stop the service.
     *
     * Taken lock, stock and barrel from Christopher Pierce's blog at http://blog.platinumsolutions.com/node/234
     *
     * @param args Arguments from prunsrv command line
     **/
    public static void windowsService(String args[]) {
        String cmd = "start";
        if (args.length > 0) {
            cmd = args[0];
        }

        if ("start".equals(cmd)) {
            engineLauncherInstance.windowsStart();
        }
        else {
            engineLauncherInstance.windowsStop();
        }
    }

    public void windowsStart() {
        log.debug("windowsStart called");
        initialize();
        while (!engine.isStopped()) {
            // don't return until stopped
            synchronized(this) {
                try {
                    this.wait(60000);  // wait 1 minute and check if stopped
                }
                catch(InterruptedException ie){}
            }
        }
    }

    public void windowsStop() {
        log.debug("windowsStop called");
        terminate();
        synchronized(this) {
            // stop the start loop
            this.notify();
        }
    }

    // Implementing the Daemon interface is not required for Windows but is for Linux
    @Override
    public void init(DaemonContext arg0) throws Exception {
        log.debug("Daemon init");
        }

    @Override
    public void start() {
        log.debug("Daemon start");
        initialize();
    }

    @Override
    public void stop() {
        log.debug("Daemon stop");
        terminate();
    }

    @Override
    public void destroy() {
        log.debug("Daemon destroy");
    }

    /**
     * Do the work of starting the engine
     */
    private void initialize() {
        if (engine == null) {
            log.info("Starting the Engine");
 ... spawn threads etc
        }
    }

    /**
     * Cleanly stop the engine.
     */
    public void terminate() {
        if (engine != null) {
            log.info("Stopping the Engine");
            engine.stop();
            log.info("Engine stopped");
        }
    }
}