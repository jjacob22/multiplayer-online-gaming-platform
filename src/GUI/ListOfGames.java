package GUI;

import GUI.Games.Chess;
import GUI.Games.ConnectFour;
import GUI.Games.GameScene;
import GUI.Games.TicTacToe;
import match_making.Game;
import networking.Client;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * It appears this class is able to automatically attempt to instantiate a game scene by being given a game name String.
 */
public class ListOfGames {
    /*
    Here is a simpler implementation of what was originally committed in this file.
    The original implementation may be found below, it is commented out.
     */




    /*
    The original implementation of this code seemed a little sketchy and over-complex, and may be found below.
     */


    // Store Class references in a HashMap to allow fast lookups by class name
    public static Map<String, Class<? extends GameScene>> gameClassesMap = new HashMap<>();

    public ListOfGames() {
        // Define game class names

        // Populate the map with class references
        for (String gameName : findClassNames("GUI.Games")) {
            try {
                // Get Class reference
                Class<?> clazz = Class.forName(gameName);
                // Add it to the map with the class name as the key
                gameClassesMap.put(clazz.getSimpleName(), (Class<? extends GameScene>) clazz);
                System.out.println("Loaded game class: " + gameName);
            }
            catch (Exception e) {
                System.err.println("Could not load game class: " + gameName);
                e.printStackTrace();
            }
        }
    }

    public static GameScene getGame(App app, String gameName) {
        Class<? extends GameScene> gameClass = ListOfGames.gameClassesMap.get(gameName);
        if (gameClass == null) {
            throw new IllegalArgumentException("Game not found: " + gameName);
        }
        try {
            GameScene gameScene = gameClass.getDeclaredConstructor(App.class).newInstance(app);
            app.add(gameScene, App.GAME_SCENE);
            return gameScene;
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Game scene " + gameName + " does not exist.");
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Game scene " + gameName + " could not be constructed.");
        }
    }

    public static ArrayList<String> getGameNames() {
        return new ArrayList<>(gameClassesMap.keySet());
    }


    public static List<String> findClassNames(String packageName) {
        List<String> classNames = new ArrayList<>();
        try {
            // Convert package name to file path
            String path = packageName.replace('.', '/');
            URL packageURL = ListOfGames.class.getClassLoader().getResource(path);

            if (packageURL == null) {
                throw new ClassNotFoundException("Package not found: " + packageName);
            }

            File directory = new File(packageURL.toURI());
            if (directory.exists() && directory.isDirectory()) {
                // Get all class files in the directory
                File[] files = directory.listFiles((dir, name) -> name.endsWith(".class"));
                for (File file : files) {
                    if(file.getName().equals("GameScene.class")) continue;
                    // Check if the file is a top-level class (no `$` in the name)
                    String className = packageName + '.' + file.getName().replace(".class", "");
                    if (!className.contains("$")) {
                        classNames.add(className);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classNames;
    }
}