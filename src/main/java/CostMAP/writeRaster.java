/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cost_MAP;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import javax.imageio.ImageIO;

/**
 *
 * @author BHooverAdmin
 */
public class writeRaster {

    /**
     *
     * @param constructionGrid
     */
    costSolver costs = new costSolver();

    public void writeToRaster(String path, double[][] constructionGrid) throws IOException{
        Dictionary headerInfo = costs.getHeader("Datasets/ASCII/landcover.asc");
        int rows = (int)headerInfo.get("Rows");
        int columns = (int)headerInfo.get("Columns");

        BufferedImage image = new BufferedImage(
            constructionGrid.length,
            constructionGrid[0].length,
            BufferedImage.TYPE_INT_ARGB_PRE
        );

        for (int i = 0; i < constructionGrid.length; i++) {
            for (int j = 0; j < constructionGrid[0].length; j++) {
                image.setRGB(i, j, (int) constructionGrid[i][j]*170);
            }
        }

        File ImageFile = new File(path);
        ImageIO.write(image, "png", ImageFile);
    }

    /*
    public double[][] landcoverInput(boolean isSelectedPop, String path) throws FileNotFoundException, IOException, Exception {

        System.out.println("PATH IS: " + path);

        //Read in landcover weighting
        ArrayList weights = new ArrayList();
        BufferedReader br1 = new BufferedReader(new FileReader("Datasets/weights/landcover.txt"));
        String line = br1.readLine();
        while ((line = br1.readLine()) != null) {
            String[] splited = line.split("\\s");
            weights.add(splited[2]);
        }
        br1.close();

        Dictionary headerInfo = getHeader(path);
        double[][] nlcdMatrix = getDetails(headerInfo, path);

        //Create Output matrix
        double[][] tempMatrix = new double[nlcdMatrix.length][nlcdMatrix[0].length];
        double[][] popMatrix = null;

        //Read in population file if it exists
        if (isSelectedPop == true) {
            System.out.println("Importing Population Data ...");
            popMatrix = getDetails(headerInfo, "Datasets/ASCII/population.asc");
        }

        for (int i = 0; i < nlcdMatrix.length; i++) {

            for (int j = 0; j < nlcdMatrix[0].length; j++) {
                //no data using mask
                int value = (int) nlcdMatrix[i][j];
                int a = (int) headerInfo.get("NoData");

                if (nlcdMatrix[i][j] == 21 || nlcdMatrix[i][j] == 22 || nlcdMatrix[i][j] == 23 || nlcdMatrix[i][j] == 24) {
                    if (isSelectedPop != false) {

                        double cellPop = cellPop(headerInfo,popMatrix, i, j);
                        if (cellPop == 0) {
                            tempMatrix[i][j] = 0.75;
                        } else if (cellPop > 0 & cellPop <= 5) {
                            tempMatrix[i][j] = 1;
                        } else if (cellPop > 5 & cellPop <= 25) {
                            tempMatrix[i][j] = 1.5;
                        } else if (cellPop > 25 & cellPop <= 100) {

                            tempMatrix[i][j] = 2.5;
                        } else {
                            tempMatrix[i][j] = 5;

                        }

                    } else {
                        tempMatrix[i][j] = 5;
                    }
                }
                switch(value){
                    case 11:
                        tempMatrix[i][j] = Double.parseDouble((String) weights.get(0));
                        break;
                    case 12:
                        tempMatrix[i][j] = Double.parseDouble((String) weights.get(1));
                        break;
                    case 31:
                        tempMatrix[i][j] = Double.parseDouble((String) weights.get(2));
                        break;
                    case 41:
                    case 42:
                    case 43:
                        tempMatrix[i][j] = Double.parseDouble((String) weights.get(3));
                        break;

                    case 52:
                        tempMatrix[i][j] = Double.parseDouble((String) weights.get(4));
                        break;

                    case 71:
                        tempMatrix[i][j] = Double.parseDouble((String) weights.get(5));
                        break;
                    case 81:
                        tempMatrix[i][j] = Double.parseDouble((String) weights.get(6));
                        break;
                    case 82:
                        tempMatrix[i][j] = Double.parseDouble((String) weights.get(7));
                        break;
                    case 90:
                    case 95:
                        tempMatrix[i][j] = Double.parseDouble((String) weights.get(8));
                        break;
                    default:
                        tempMatrix[i][j] = (int) headerInfo.get("NoData");
                }
            }
        }

        //Dictionary costList = new Hashtable();
        double[][] costMatrix = new double[(int) headerInfo.get("Rows")][(int) headerInfo.get("Columns")];
        int cell = 0;

        for (int i = 0; i < tempMatrix.length; i++) {
            for (int j = 0; j < tempMatrix[0].length; j++) {
                    cell = cell +1;

                    double[] landKernel = kernel(tempMatrix, i, j);
                    double[] costs = solveLand(landKernel);
                    costMatrix[i][j] = costs;
                    //costList.put(cell, costs);
            }
        }

       return costMatrix;
    }
    */
}





