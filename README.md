# Red5 service

Herein you will find the service / daemon portion of the project. We utilize the [Apache Commons Daemon project](https://commons.apache.org/proper/commons-daemon/) for our service process on all supported platforms; currently [Linux](https://commons.apache.org/proper/commons-daemon/jsvc.html) and [Windows](https://commons.apache.org/proper/commons-daemon/procrun.html) are supported.

Apache Daemon [Windows binaries download](http://www.apache.org/dist/commons/daemon/binaries/windows/)


## Daemon Details

To hook-in to the daemon, one would get an instance of the [`EngineLauncher` class](https://github.com/Red5/red5-service/blob/master/src/main/java/org/red5/daemon/EngineLauncher.java) and call the `start()` method to start the server process and `stop()` to terminate it. The windows implementation using `procrun` is only slightly different in that it calls `windowsService()` for both start and stop (The string `start` must be supplied via args with windows).

### Shutdown
The Server / service shutdown is performed via socket connection and the _shutdown_ token must be supplied via `args` when making the request; if its not supplied, the `shutdown.token` file will be opened, if it exists and is readable.

## Setup

### Linux
Linux daemon uses __jsvc__ and the `init.d` script `red5`.

1. Set / export __RED5_HOME__ environmental variable
2. Edit the variables in the init.d script to match your server
3. Install jsvc
 * Debian `sudo apt-get install jsvc`
 * CentOS `sudo yum ??`
4. Copy the `red5` script to your init.d directory, ex. `/etc/init.d/`
5. This step is for Systemd enabled operating systems __only__ such as CentOS 7. Copy the `red5.service` file into the `/etc/systemd/system/` directory. Modify the file as needed, ensure the `ExecStart` path is correctly pointing to the init script.
6. Install the init.d script
 * Debian
```sh
sudo update-rc.d red5 defaults
sudo update-rc.d red5 enable
```
 * CentOS
```sh
systemctl daemon-reload
systemctl enable red5.service
```
6. Start the service
 * Debian `service red5 start`
 * CentOS `systemctl start red5.service`
7. Stop the service
 * Debian `service red5 stop`
 * CentOs `systemctl stop red5.service`
 
### Windows
Windows daemon uses __procrun__.

**Install**

1. Set the __RED5_HOME__ environmental variable
2. Edit the variables in the `install-service.bat` script to match your server
3. Download the [windows binaries](http://www.apache.org/dist/commons/daemon/binaries/windows/)
4. Unzip the Apache Daemon archive into your red5 directory
5. Ensure `procrun.exe` is in your red5 home directory alongside `red5-service.jar`
6. Execute `install-service.bat` to install the service
7. Open the windows services panel `services.msc`
8. Scroll down to Red5
9. Start the service by clicking the start button in the UI
10. Stop the service by clicking the stop button in the UI

**Uninstall**

1. To uninstall the service execute `uninstall-service.bat`
