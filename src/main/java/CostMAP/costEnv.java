package cost_MAP;

import java.io.File;
import java.io.*;

public class costEnv {
    private static boolean debugMode = true;
    private static String datasetDir;
    private static String outputDir;

    private static String aspect = "aspect.asc";
    private static String fed = "fed.asc";
    private static String landcover = "landcover.asc";
    private static String pipelines = "pipelines.asc";
    private static String population = "population.asc";
    private static String railroads = "railroads.asc";
    private static String rivers = "rivers.asc";
    private static String roads = "roads.asc";
    private static String slope = "slope.asc";
    
    private static void printDebug(String s) {
        if (debugMode) {
            System.out.println(s);
        }
    }
    
    public static void toggleDebug(boolean mode) {
        debugMode = mode;
    }

    public static void setDatasetDir(String dir) {
        printDebug("Set: dataset dir = " + dir);
        datasetDir = dir;
    }

    public static String getDatsetDir() {
        return datasetDir;
    }

    private static String findPath(String filename) {
        return datasetDir + File.separatorChar + filename;
    }

    public static String getAspectPath() {
        printDebug("Query: aspect path = " + findPath(aspect));
        return findPath(aspect);
    }
    public static String getFedPath() {
        printDebug("Query: fed path = " + findPath(fed));
        return findPath(fed);
    }
    public static String getLandcoverPath() {
        printDebug("Query: landcover path = " + findPath(landcover));
        return findPath(landcover);
    }
    public static String getPipelinesPath() {
        printDebug("Query: pipelines path = " + findPath(pipelines));
        return findPath(pipelines);
    }
    public static String getPopulationPath() {
        printDebug("Query: population path = " + findPath(population));
        return findPath(population);
    }
    public static String getRailroadsPath() {
        printDebug("Query: railroads path = " + findPath(railroads));
        return findPath(railroads);
    }
    public static String getRiversPath() {
        printDebug("Query: rivers path = " + findPath(rivers));
        return findPath(rivers);
    }
    public static String getRoadsPath() {
        printDebug("Query: roads path = " + findPath(roads));
        return findPath(roads);
    }
    public static String getSlopePath() {
        printDebug("Query: slope path = " + findPath(slope));
        return findPath(slope);
    }

    public static String getOutputPath() {
        printDebug("Query: output path = " + outputDir);
        return outputDir;
    }
}
