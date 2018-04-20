package elevator;

import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Elevator Simulation
 *
 * @author
 */
public class ElevatorDriver {

    private ArrayList<Elevator> simulationAM;
    private ArrayList<Elevator> simulationPM;
    private PriorityQueue<ElevatorRider>[] building;



    public ElevatorDriver() {
        simulationAM = new ArrayList<Elevator>();
        simulationPM = new ArrayList<Elevator>();
    }

    public ArrayList<Elevator> getSimulationAM() {
        return simulationAM;
    }

    public ArrayList<Elevator> getSimulationPM() {
        return simulationPM;
    }

    public PriorityQueue<ElevatorRider>[] getBuilding() {
        return building;
    }

    /**
     * @param args the command line arguments
     */
    public void Simulate(int numberOfFloors, int numberOfRidersToAdd, int frustrationFactor, FrustrationTypes fT, int percentageVip, int[] ridersHomed) {

        //Setup riders
        ArrayList<ElevatorRider> riders = new ArrayList<ElevatorRider>();
        //Setup Building
        building = new PriorityQueue[numberOfFloors];
        for (int i = 0; i < building.length; i++)
            building[i] = new PriorityQueue();

        // Setup riders homed for each floor
        for (int i = 1; i < building.length; i++) {
            addRiders(ridersHomed[i], i + 1, riders);

        }

        Collections.shuffle(riders);

        //Make VIPs
        for (int i = 0; i < Math.floor(riders.size() * percentageVip / 100); i++) {
            riders.get(i).promote();
        }

        Collections.shuffle(riders);

        //Setup Elevator
        Elevator elevator = new Elevator();

        //Test code to make sure that the riders worked!
        /*
        for (ElevatorRider rider: riders)
            System.out.println(rider);
        */

        //Morning Mode
        int currentRider = 0;
        int count = 0;
        do {
            //New rider!
            if (currentRider < riders.size()) {
                //n new riders
                for (int index = 0; index < numberOfRidersToAdd; index++) {
                    if (currentRider < riders.size()) {
                        building[0].add(riders.get(currentRider++));
                    }
                }
            }

            //Pop riders for elevator's current floor
            while (elevator.peek() != null && elevator.peek().getHomeFloor() == elevator.getCurrentFloor()) {
                building[elevator.getCurrentFloor() - 1].add(elevator.pop());

            }

            //Frustrate
            elevator.frustrate(frustrationFactor, fT); // Need to change to user input

            //Push riders from current floor to elevator
            while (elevator.getCurrentFloor() == 1 && !elevator.isFull() && !building[elevator.getCurrentFloor() - 1].isEmpty())
                elevator.push(building[elevator.getCurrentFloor() - 1].remove());




            //Move elevator
            if (elevator.peek() != null) {
                elevator.setCurrentFloor(elevator.peek().getHomeFloor());
            } else {
                elevator.setCurrentFloor(1);
            }

            //System.out.println("ELEVATOR AT END: " + elevator);
            simulationAM.add(elevator);
            System.out.println(simulationAM.get(count).getCurrentFloor() + " -------------------------Size--------------------- = " + simulationAM.size());
            count ++;
        }
        while (currentRider < riders.size() || elevator.peek() != null || building[0].peek() != null);



        //Output
        int result = 0;
        int resultVIP = 0;
        int resultNoVIP = 0;
        for (ElevatorRider rider : riders) {
            result += rider.getFrustration();
            if (rider.isVIP()) {
                resultVIP += rider.getFrustration();
            } else {
                resultNoVIP += rider.getFrustration();
            }
        }

        System.out.println("AM MODE:\n\tAverage (MEAN) Frustration Level is: " + ((double) result / riders.size()));
        System.out.println("AM MODE:\n\tAverage (MEAN) VIP Frustration Level is: " + ((double) resultVIP / (riders.size() / 10)));
        System.out.println("AM MODE:\n\tAverage (MEAN) Non-VIP Frustration Level is: " + ((double) resultNoVIP / (riders.size() * .9)));


        //Evening Mode
        //Reset Starting Conditions
        currentRider = 0;
        for (int i = 0; i < riders.size(); i++) {
            riders.get(i).setFrustration(0);
        }
        for (int i = 0; i < building.length; i++)
            while (!building[i].isEmpty())
                building[i].remove();
        do {
            //New rider!
            if (currentRider < riders.size()) {
                //10 new riders
                for (int index = 0; index < numberOfRidersToAdd; index++) {
                    if (currentRider < riders.size()) {
                        building[riders.get(currentRider).getHomeFloor() - 1].add(riders.get(currentRider));
                        riders.get(currentRider).setHomeFloor(1);
                        currentRider++;
                    }
                }
            }

            //Pop riders for elevator's current floor
            while (elevator.peek() != null && elevator.peek().getHomeFloor() == elevator.getCurrentFloor()) {
                building[elevator.getCurrentFloor() - 1].add(elevator.pop());
            }

            //Frustrate
            elevator.frustrate(frustrationFactor, fT); // Need to change to user input
            while (elevator.getCurrentFloor() != 1 && !elevator.isFull() && !building[elevator.getCurrentFloor() - 1].isEmpty()) {
                elevator.push(building[elevator.getCurrentFloor() - 1].remove());
            }
            //Move elevator
            Random gen = new Random();
            if (elevator.peek() != null)
                elevator.setCurrentFloor(elevator.peek().getHomeFloor());
            else
                elevator.setCurrentFloor(gen.nextInt(numberOfFloors-1) + 2);

            System.out.println("ELEVATOR AT END: " + elevator);
            simulationPM.add(elevator);
        }
        while (currentRider < riders.size() || elevator.peek() != null || buildingPeekFloors(building));

        //Output
        result = 0;
        resultVIP = 0;
        resultNoVIP = 0;
        for (ElevatorRider rider : riders) {
            result += rider.getFrustration();
            if (rider.isVIP()) {
                resultVIP += rider.getFrustration();
            } else {
                resultNoVIP += rider.getFrustration();
            }
        }

        System.out.println("PM MODE:\n\tAverage (MEAN) Total Frustration Level is: " + ((double) result / riders.size()));
        System.out.println("PM MODE:\n\tAverage (MEAN) VIP Frustration Level is: " + ((double) resultVIP / (riders.size() / 10)));
        System.out.println("PM MODE:\n\tAverage (MEAN) Non-VIP Frustration Level is: " + ((double) resultNoVIP / (riders.size() * .9)));
        System.out.println(simulationAM.size() + "<-------------------------IN Side Driver------------------------------->");

    }

    public static void addRiders(int quantity, int floor, ArrayList<ElevatorRider> list) {
        for (int i = 0; i < quantity; i++)
            list.add(new ElevatorRider(floor));
    }

    public static boolean buildingPeekFloors(PriorityQueue<ElevatorRider>[] building) {
        boolean isOccupied = false;
        for (int i = 1; i < building.length; i++) {
            if (building[i].peek() != null) {
                isOccupied = true;
            }
        }
        return isOccupied;
    }
    /*
    public Elevator getElevator() {
        return elevator;
    }
    */
}
