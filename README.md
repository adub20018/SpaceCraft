# SpaceCraft


## Gameplay Instructions
Mine asteroids and refine them to upgrade your spaceship to mine astroids faster and better
Upgrades: 
  Click power - Increase how much the tractor beam charges when you click the spaceship. 
  idle charge - Increase how much the tractor beam charges while you are idle or refining.
  navigator - Navigate towards more dense asteroid field. More asteroids to harvest.
  harvest time - Decrease how much time the tractor takes to harvest an asteroid.

Refinery:
  Click to refine your asteroids into one of 3 minerals crucial for upgrades.
  Gravitite - Mainly used for tractor beam upgrades.
  Tritanium - Mainly used for idle/other upgrades.
  Cubane - Mainly used for Navigation and refinery.

Research:
  Tractor Quantity - Gives an extra tractor beam to charge up and use.
  Extra Refine Quantity - Refines an extra asteroid per click in refinery.
  Refine Quality - Increases the efficiency of the refinery, more resources for less asteroids.
  Scanner - Tractor beams can now target the highest rarity asteroid for optimal harvesting.





A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `android`: Android mobile platform. Needs Android SDK.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `android:lint`: performs Android project validation.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
