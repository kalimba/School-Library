/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package REGISTER;

/**
 *
 * @author Kalimba
 */
public class ComboItem {

    private String id;
    private String name;

    public ComboItem(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId(){
        return id;
    }

    @Override
    public String toString(){
        return name;   // what appears in JComboBox
    }
}