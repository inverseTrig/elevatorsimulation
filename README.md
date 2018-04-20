# Elevator Simulator for HCI
This project is for a small elevator simulation for Human Computer Interaction.

There were two portions of work to be done: back-end and front-end. We modified the initial code that were given to us so that it could take the user's input as variables. To randomize the variables between two values, check the box next to the category, and write the values before and after the tilda "~". Currently doesn't have the option to specify the number of riders homed on each floor, thus, the code works when the values are randomized (please check the box).

We store a clone of the elevator at each step into an ArrayList<Elevator> that we use to step through the progress of the elevator as time goes by.

Author: HeeChan Kang & Yusuf Azam



======================================================
Class Main - Elevator Animation
======================================================

The animation for the elevator is created in the main class
,in the handle method. When the play button is called it runs
the move() method. The move method takes in an array of doubles
which corresponds to the floors in the Elevator Driver. The
double passed in is generated in the handle method. It iterates
over a for loop using an array list SimulationAM. SimulationAM
adds Elevator objects for the elevator class that then contains
all the information for that Elevator e.g floor number. The for
loop in the handle method then takes the floor numbers from these
Elevator objects form SimulationAM. The Elevator scales this to
the Y-Axis in the posY [] array. Hence it is representative of the
actual floors that the elevator is moving on. Hence, if the ElevatorDriver
 is never run, then the Elevator can never move.
