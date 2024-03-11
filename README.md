## Docker build

This section details how to build and run the docker image.

1. Build docker with the following command in shell:

   ```shell
   docker build -t floorplanner .
   ```

2. The project uses **JavaFX** which is why docker requires an external tool called [VcXsrv Windows X Server](https://sourceforge.net/projects/vcxsrv/) to display the application

   - Additional information about the installation can be found from [aboullaite's blog](https://aboullaite.me/javafx-docker/)
   - TL;DR check `Disable access control` while setting up **Xlaunch**

3. To run docker use the following command in shell:

   ```shell
   docker run -it --rm -e DISPLAY=host.docker.internal:0.0 floorplanner
   ```
