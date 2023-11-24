## Overview
The program runs a "ship" which is represented by a mxm matrix where each cell is open or closed. 
A "bot" inhabits one cell at a time and traverses the ship to find a goal cell(s). The bot can only move through open cells. 
The random generation of the Ship is handled in the Ship.java class. In every ship configuration, the bot can reach any open cell from any open cell. 

## The Experiment
A "leak" is placed in a random open cell in the ship. 
The bot does not have acces to the leak's location, but has access to a "sense" function which gives information depending on the location of the leak. 
The bot uses information from the sense function to make inferences on the serach space. 
### Sense
There are two types of senses: deterministic and probabilitic
The deterministic sense will return true if the leak is within a specific range k of the bot. 
The probabilitic sense returns true or false based on a probability weighted by a factor α that increases the close the bot is to the leak. 
### Progression
At the start of the experiment, the ship and spawn points of the bot and leak will be randomly generated. 
The bot will use a combination of moving (left, right, down, up) through the matrix and sensing to navigate the ship and find the leak. 

## Different Bots
There are 9 different bots. Each either uses different a different sense or handles a different scenario or chooses when to move or sense differently. 
Bots 1 and 2 use deterministic sense to find a single leak. 
Bots 3 and 4 use probabilistic sense to find a single leak. 
Bots 5 and 6 use determinmstic sense to find two leaks. 
Bots 7, 8, and 9 use probabilistic sense to find two leaks. 
Detailed descriptions of each bot can be found in the report. 

## Data collection
The ExperimentJava.java class compares the average number of Actions of each bot 
The methodology of data collection and graphs can be found in the report. 
