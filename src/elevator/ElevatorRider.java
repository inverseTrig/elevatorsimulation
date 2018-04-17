package elevator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * A person who rides an elevator
 *
 * @author kerlin
 */
public class ElevatorRider implements Comparable {

    private static int counter = 0;
    private int id;
    private int frustration = 0;
    private int homeFloor;
    private boolean vip = false;

    public boolean isVIP() {
        return vip;
    }

    public void promote() {
        vip = true;
    }

    @Override
    public int compareTo(Object t) {
        if (!(t instanceof ElevatorRider)) {
            return 0;
        } else {
            if (isVIP() == ((ElevatorRider) t).isVIP()) {
                return 0;
            } else if (isVIP()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    /**
     * Get the value of homeFloor
     *
     * @return the value of homeFloor
     */
    public int getHomeFloor() {
        return homeFloor;
    }

    /**
     * Set the value of homeFloor
     *
     * @param homeFloor new value of homeFloor
     */
    public void setHomeFloor(int homeFloor) {
        this.homeFloor = homeFloor;
    }


    public ElevatorRider() {
        id = ++counter;
    }

    public ElevatorRider(int floor) {
        id = ++counter;
        homeFloor = floor;
    }

    public ElevatorRider(int floor, boolean isVIP) {
        id = ++counter;
        homeFloor = floor;
        vip = isVIP;
    }


    public int getFrustration() {
        return frustration;
    }

    public void setFrustration(int frustration) {
        this.frustration = frustration;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ElevatorRider{" + "id=" + id + ", frustration=" + frustration + ", homeFloor=" + homeFloor + ", vip=" + vip + '}';
    }
}
