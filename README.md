# HelicopterPong

HelicopterPong is a simple pong game with helicopters flying around and colliding in the background.

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

## Video

<https://github.com/user-attachments/assets/8bc08d40-129f-44ff-aab8-764c64d94d06>

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.

### Running

To run the project use the following command:

```bash
./gradlew lwjgl3:run
```

Then to create the build .jar file use the following command:

```bash
./gradlew lwjgl3:jar
```

The `.jar` file will be stored in the `lwjgl3/build/libs` directory.

## Report

You can find the report [here](Software%20Architecture%20Patterns.pdf)
