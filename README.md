# Isolate AoK graphic files by civilization type

*A simple modding utility for Age of Kings and The Conquerors*

If you need to separate out the graphics SLPs by civilization architecture type, use this tool to do that for you in one go. Simply point it to your directory of extracted SLPs (extracted _en masse_ from graphics.drs – use DRS Explorer or Turtle Pack if you haven't already done so) and then select the civilization type you are interested in.

This is useful when developing mods of building graphics. By separating the SLPs into separate folders, you can begin focusing on modding the graphics and stop trying to find the correct SLP to change.

## Installation

**Using Eclipse:**

1. Import the whole folder as a project into your workspace.
2. (Optional) Modify the `configuration.properties` file to point to the your SLP folder.
3. Use the `Build` and `Run` commands in Eclipse to execute.

**Without Eclipse:**

1. Use `javac` to compile the file `src/Extractor.java` manually.
2. (Optional) Modify the `configuration.properties` file to point to the your SLP folder.
3. Use `java` to run the compiled `Extractor` class. A pre-compiled version is available in `bin/` folder, which may work fine for you instead.

## License
Copyright (c) 2011 [http://twitter.com/pulkitkarwal](Pulkit Karwal)
Licensed under the MIT license
