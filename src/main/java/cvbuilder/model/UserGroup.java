/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cvbuilder.model;


import java.util.ArrayList;



/**
 *
 * @author mxgar
 */
public class UserGroup 
{
    
    private ArrayList<User> names = new ArrayList();
    private static UserGroup instance;
    
    
    private UserGroup()
    {
        
    }
    
    
    public static UserGroup getInstance()
    {
        if (instance == null)
        {
            instance = new UserGroup();
        }
        
        return instance;
    }

    /**
     * Get the value of names
     *
     * @return the value of names
     */
    
    public ArrayList<User> getNames()
    {
        return names;
    }

    /**
     * Set the value of names
     *
     * @param names new value of names
     */
    public void setNames(ArrayList<User> names) 
    {
        this.names = names;
    }

    @Override
    public String toString() 
    {
        return "UserGroup{" + "names=" + names + '}';
    }
    
    

}

