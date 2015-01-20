/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dbnormalizer;

import java.util.Iterator;
import java.util.SortedSet;

/**
 *
 * @author Gebruiker
 */
public class Relation
{    
    
    private String name;
    private SortedSet<String> attributes;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    public SortedSet<String> getAttributes()
    {
        return attributes;
    }

    public Relation(String name, String attributeString) throws InvalidAttributeStringException
    {
        this.name = name;
        attributes = Util.stringToSortedSet(attributeString);
    }
    
    public void addAttribute(String attr)
    {
        attributes.add(attr);
    }
    
    public void removeAttribute(String attr) 
    {
        attributes.remove(attr);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append("(");
        Iterator<String> it = this.getAttributes().iterator();
        while(it.hasNext()){
            sb.append(it.next());
            if (it.hasNext())
            {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
