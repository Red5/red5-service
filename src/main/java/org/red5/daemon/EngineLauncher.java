/**
 * Launch the Engine from a variety of sources, either through a main() or invoked through
 * Apache Daemon.
 */
public class EngineLauncher implements Daemon {
    private static final Log4J log = Log4J.getLog();
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