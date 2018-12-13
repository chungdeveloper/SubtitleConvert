/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiotool;

/**
 *
 * @author shoot
 */
public class BandRateObject {

    private String bandName;
    private double value;

    public BandRateObject(String name, double value) {
        this.bandName = name;
        this.value = value;
    }

    public String getName() {
        return bandName;
    }

    public double getValue() {
        return value;
    }
}
