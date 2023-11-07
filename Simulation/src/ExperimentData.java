import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

public class ExperimentData{

    static final int numExperiments = 100;

    private double[] botOneData = new double[10];
    private double[] botTwoData = new double[10];

    private double[] kVals = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};


    public ExperimentData(){
        
    }

    public void getBotOneAndTwoRuns(){
        
        for(int k = 1; k <= 10; k ++){
            int[] botOneDataTotal = new int[numExperiments];
            int[] botTwoDataTotal = new int[numExperiments];

            for(int i = 0; i < numExperiments; i ++){
                System.out.println("Test: " + i + " For Bot One");
                ExperimentController botOneExperimentController = new ExperimentController();
                botOneExperimentController.getShip().formShip();
                botOneExperimentController.setBotOne(k);
                botOneDataTotal[i] = botOneExperimentController.runExperiment();
                System.out.println("Test: " + i + " For Bot One Completed");

                System.out.println("Test: " + i + " For Bot Two");
                ExperimentController botTwoExperimentController = new ExperimentController();
                botTwoExperimentController.getShip().formShip();
                botTwoExperimentController.setBotTwo(k);
                botTwoDataTotal[i] = botTwoExperimentController.runExperiment();
                System.out.println("Test: " + i + " For Bot One Completed");
            }

            botOneData[k-1] = calculateArrayAverage(botOneDataTotal);
            botTwoData[k-1] = calculateArrayAverage(botTwoDataTotal);
            System.out.println("k val: " + k + " completed");
            
        }

        XYChart botOneVSTwo = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Bot1 Vs Bot 2")
                .xAxisTitle("K Values")
                .yAxisTitle("Average Number of Moves")
                .build();

        botOneVSTwo.addSeries("Bot 1", kVals, botOneData);
        botOneVSTwo.addSeries("Bot 2", kVals, botTwoData);

        botOneVSTwo.getStyler().setMarkerSize(8);
        botOneVSTwo.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        
        new SwingWrapper<>(botOneVSTwo).displayChart();
        
    }

    public static double calculateArrayAverage(int[] array) {
        int sum = 0;
        for (int element : array) {
            sum += element;
        }

        double average = (double) sum / array.length;
        return average;
    }

    public static void main(String [] args)
	{
        ExperimentData experimentData = new ExperimentData();
        experimentData.getBotOneAndTwoRuns();
	}

}