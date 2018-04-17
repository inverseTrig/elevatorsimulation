package elevator;

import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This is the Elevator Stack
 *
 * @author kerlin
 */
public class Elevator {
    private ElevatorRider[] stack = new ElevatorRider[20];
    private int riders = 0;
    private int currentFloor = 1;

    public boolean isFull() {
        if (riders == stack.length)
            return true;
        return false;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public boolean push(ElevatorRider e) {
        if (riders < stack.length) {
            stack[riders] = e;
            riders++;
            return true;
        }

        return false;
    }

    public ElevatorRider peek() {
        if (riders > 0)
            return stack[riders - 1];
        return null;
    }

    public ElevatorRider pop() {
        ElevatorRider temp = peek();
        if (temp != null) {
            riders--;
            stack[riders] = null;
        }
        return temp;

    }

    public void frustrate(int i, FrustrationTypes fT) {
        //System.out.println(this);
        for (ElevatorRider e : stack) {
            if (e != null) {
                switch (fT) {
                    case LINEAR:
                        e.setFrustration(e.getFrustration() + i);
                    case LOGARITHMIC:
                        if (e.getFrustration() == 0) {
                            e.setFrustration(i);
                        }
                        e.setFrustration((int) Math.log(e.getFrustration()));
                    case POLYNOMIAL:
                        if (e.getFrustration() == 0) {
                            e.setFrustration(i);
                        }
                        e.setFrustration((int) Math.pow(i, e.getFrustration()));
                    case EXPONENTIAL:
                        if (e.getFrustration() == 0) {
                            e.setFrustration(i);
                        }
                        e.setFrustration((int) Math.pow(e.getFrustration(), i));
//				case FACTORIAL:
//					e.setFrustration(factorial(e.getFrustration()));
                    default:
                        e.setFrustration(e.getFrustration() + i);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Elevator{" + "riders=" + riders + ", currentFloor=" + currentFloor + ", stack=" + Arrays.toString(stack) + '}';
    }

//    public int factorial(int n) {
//    	if (n == 0) return 1;
//    	else return n * factorial(n-1);
//    }

}
