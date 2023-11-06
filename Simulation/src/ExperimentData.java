import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class ExperimentData{

    private int[] botOneData = new int[10];
    private int[] botTwoData = new int[10];


    public ExperimentData(){
        
    }

    public void getBotOneAndTwoRuns(){
        for(int k = 1; k <= 10; k ++){
            ExperimentController botOneExperimentController = new ExperimentController();
            botOneExperimentController.setBotOne(k);
            botOneData[k-1] = botOneExperimentController.runExperiment();

            ExperimentController botTwoExperimentController = new ExperimentController();
            botTwoExperimentController.setBotTwo(k);
            botTwoData[k-1] = botTwoExperimentController.runExperiment();
        }
    }

    public static void main(String [] args)
	{
        
	}

}