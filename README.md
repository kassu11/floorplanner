## About The Project

This project provides users with an easy-to-use and efficient tool for designing and creating floorplans. It offers a variety of features that facilitate design, such as measuring dimensions and calculating total area. This project is open-source.

If you would like to go more into detail about the projects code structure, there are plenty of [UML graphs](https://github.com/kassu11/floorplanner/tree/main/UML-graphs) about the inner workings of the program.

For more detailed information on how each method works, here is a link to the [Javadoc](https://users.metropolia.fi/~aaposs/javadoc/) that was created for this project.

### Shortcuts keys

A short list of all the default shortcut keys that the application supports.

| Keys | Action |
|----------|------|
| <kbd>ctrl</kbd> + <kbd>a</kbd> | Select all shapes |
| <kbd>ctrl</kbd> + <kbd>n</kbd> | New file |
| <kbd>ctrl</kbd> + <kbd>z</kbd> | Undo action |
| <kbd>ctrl</kbd> + <kbd>y</kbd> | Redo action |
| <kbd>1</kbd> | Set draw shape to line |
| <kbd>2</kbd> | Set draw shape to multiline |
| <kbd>3</kbd> | Set draw shape to door |
| <kbd>4</kbd> | Set draw shape to rectangle |
| <kbd>s</kbd> | Activate select tool |
| <kbd>e</kbd> | Activate delete tool |
| <kbd>r</kbd> | Activate rotate tool / flip door when drawing |
| <kbd>b</kbd> | Activate drawing mode with the last selected shape (default is line) |
| <kbd>a</kbd> | Activate area tool |

### Built with

- [![Java][Java.java]][Java-url]
- [JavaFX](https://openjfx.io/)
- [Maven](https://maven.apache.org/)

## Docker build

This section details how to build and run the docker image.

1. Build the docker images and run docker-compose with the following command in shell:

   ```shell
   docker-compose up -d
   ```

2. The project uses **JavaFX** which is why docker requires an external tool called [VcXsrv Windows X Server](https://sourceforge.net/projects/vcxsrv/) to display the application

   - Additional information about the installation can be found from [aboullaite's blog](https://aboullaite.me/javafx-docker/)
   - TL;DR check `Disable access control` while setting up **Xlaunch**

3. Docker uses multiple containers, one for a PostgreSQL database, and the other for the actual app. Instead of running the Dockerfile seperately, running the compose.yaml file will create all containers and run them. If the images are already created, it will just automatically run all of them as defined in the compose.yaml file.

<!-- MARKDOWN LINKS & IMAGES -->

[Java.java]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://www.java.com/en/
