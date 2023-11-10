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

    static final int numExperiments = 20;

    private double[] botOneData = new double[10];
    private double[] botTwoData = new double[10];
    private double[] kVals = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    private double[] botThreeData = new double[4];
    private double[] botFourData = new double[4];
    private double[] alphaVals = {0.1, 0.2, 0.3, 0.4};

    static final int botFourNumSenses = 3;
    static final int botFourNumMoves = 3;

    private double[] botFourNumSensesTest = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    
    private double[] botFourTestResults = new double[10];

    public ExperimentData(){
        
    }

    public void getBotOneAndTwoRuns(){
        
        for(int k = 1; k <= 10; k ++){
            int[] botOneDataTotal = new int[numExperiments];
            int[] botTwoDataTotal = new int[numExperiments];

            for(int i = 0; i < numExperiments; i ++){

                ExperimentController botOneExperimentController = new ExperimentController();
                botOneExperimentController.getShip().formShip();
                botOneExperimentController.setBotOne(k);
                botOneDataTotal[i] = botOneExperimentController.runExperiment();



                ExperimentController botTwoExperimentController = new ExperimentController();
                botTwoExperimentController.getShip().formShip();
                botTwoExperimentController.setBotTwo(k);
                botTwoDataTotal[i] = botTwoExperimentController.runExperiment();

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

    public void getBotThreeAndFourRuns(){
        int count = 0;
        for(double alpha: alphaVals){
            int[] botThreeDataTotal = new int[numExperiments];
            int[] botFourDataTotal = new int[numExperiments];

            for(int i = 0; i < numExperiments; i ++){
                System.out.println("Bot 3 Processing");
                ExperimentController botThreeExperimentController = new ExperimentController();
                botThreeExperimentController.getShip().formShip();
                botThreeExperimentController.setBotThree(alpha);
                botThreeDataTotal[i] = botThreeExperimentController.runExperiment();
                System.out.println("Bot 3 Completed: " + botThreeDataTotal[i]);

                System.out.println("Bot 4 Processing");
                ExperimentController botFourExperimentController = new ExperimentController();
                botFourExperimentController.getShip().formShip();
                botFourExperimentController.setBotFour(alpha, botFourNumSenses, botFourNumMoves);
                botFourDataTotal[i] = botFourExperimentController.runExperiment();
                System.out.println("Bot 4 Completed: " + botFourDataTotal[i]);
            }

            botThreeData[count] = calculateArrayAverage(botThreeDataTotal);
            botFourData[count] = calculateArrayAverage(botFourDataTotal);
            System.out.println("alpha val: " + alpha + " completed");
            count++;
        }

        XYChart botThreeVSFour = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Bot3 Vs Bot 4")
                .xAxisTitle("Alpha Values")
                .yAxisTitle("Average Number of Moves")
                .build();

        botThreeVSFour.addSeries("Bot 3", alphaVals, botThreeData);
        botThreeVSFour.addSeries("Bot ", alphaVals, botFourData);

        botThreeVSFour.getStyler().setMarkerSize(8);
        botThreeVSFour.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        
        new SwingWrapper<>(botThreeVSFour).displayChart();
    }

    public void getBotFourVariations(int numActions){
        int count = 0;
        for(double numSenses: botFourNumSensesTest){
            int[] botFourDataTotal = new int[numExperiments];
            for(int i = 0; i < numExperiments; i++){
                ExperimentController botFourExperimentController = new ExperimentController();
                botFourExperimentController.getShip().formShip();
                botFourExperimentController.setBotFour(0.2, (int)numSenses, numActions);
                botFourDataTotal[i] = botFourExperimentController.runExperiment();
            }
            System.out.println("Completed numSenses: " + numSenses);
            botFourTestResults[count] = calculateArrayAverage(botFourDataTotal);
            count++;
        }

        XYChart botFourTestResultsChart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Bot 4 Test Results with numActions = " + numActions)
                .xAxisTitle("numSenses")
                .yAxisTitle("Average Number of Moves")
                .build();

        botFourTestResultsChart.addSeries("Bot 4", botFourNumSensesTest, botFourTestResults);

        botFourTestResultsChart.getStyler().setMarkerSize(8);
        botFourTestResultsChart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        
        new SwingWrapper<>(botFourTestResultsChart).displayChart();
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
        experimentData.getBotFourVariations(10);
	}

}