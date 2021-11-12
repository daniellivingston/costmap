package cost_MAP;

import java.io.File;

public class costEnv {
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

    public static void setDatasetDir(String dir) {
        datasetDir = dir;
    }

    public static String getDatsetDir() {
        return datasetDir;
    }

    private static String findPath(String filename) {
        return datasetDir + File.separatorChar + filename;
    }

    public static String getAspectPath() {
        return findPath(aspect);
    }
    public static String getFedPath() {
        return findPath(fed);
    }
    public static String getLandcoverPath() {
        return findPath(landcover);
    }
    public static String getPipelinesPath() {
        return findPath(pipelines);
    }
    public static String getPopulationPath() {
        return findPath(population);
    }
    public static String getRailroadsPath() {
        return findPath(railroads);
    }
    public static String getRiversPath() {
        return findPath(rivers);
    }
    public static String getRoadsPath() {
        return findPath(roads);
    }
    public static String getSlopePath() {
        return findPath(slope);
    }

    public static String getOutputPath() {
        return outputDir;
    }
}
