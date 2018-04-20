# Elevator Simulator for HCI
This project is for a small elevator simulation for Human Computer Interaction.

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
