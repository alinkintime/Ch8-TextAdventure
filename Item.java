
/**
 * Write a description of class Item here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Item
{
    // instance variables - replace the example below with your own
    private String description;
    private double weight;

    /**
     * Constructor for Item Objects
     * @param description This is a short description of the item
     * @param weight This is the weight of the item
     */
    public Item(String description, double weight)
    {
       this.description =  description;
       this.weight = weight;
    }

    /**
     * Getters for weight and description
     */
    public String getDescription()
    {
     return description;   
    }
    
    public double getWeight()
    {
     return weight;   
    }
}
