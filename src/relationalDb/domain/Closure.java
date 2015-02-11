/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package relationalDb.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Gebruiker
 */
public class Closure
{

    private final Schema schema;
    private final Set<Dependency> closures;
    
    public Set<Dependency> getOriginalFDs()
    {
        return this.schema.getDependencies();
    }

    public Set<Dependency> getClosures()
    {
        return closures;
    }

    public Closure(Schema schema)
    {
        this.schema = schema;
        this.closures = new TreeSet<>();

        //Get all atributes
        List<String> attributes = new ArrayList<>();
        for (Relation r : this.schema.getRelations())
        {
            attributes.addAll(r.getAttributes());
        }

        //Get all possible combinations of attributes
        Set<Set<String>> attributeCombis = new HashSet<>();
        for (int i = 0; i < Math.pow(2, attributes.size() - 1); i++)
        {
            Set<String> attr = new HashSet<>();
            String binary = Integer.toBinaryString(i);
            Integer[] indices = Util.getOneIndices(binary);
            for (Integer j : indices)
            {
                attr.add(attributes.get(j));
            }
            attributeCombis.add(attr);
        }

        //Get the closure for all single attributes
        for (Set<String> attr : attributeCombis)
        {
            Dependency closureDependency = new Dependency(schema, attr, attr, false).getClosure(this.schema.getDependencies());
            if (!closureDependency.isTrivial())
            {
                closures.add(closureDependency);
            }
        }
        
        
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        Map<Relation, Integer> smallestKeySizes = new HashMap<>();
        for (Dependency d : this.closures)
        {
            sb.append(d.toString());
            Set<Relation> keyRelations = d.superKeyFor();
            boolean writeKey = true;
            for (Relation r : keyRelations)
            {
                smallestKeySizes.putIfAbsent(r, 0);
                
                if (writeKey)
                {
                    if (d.getFrom().size() <= smallestKeySizes.get(r) || smallestKeySizes.get(r)  == 0)
                    {
                        smallestKeySizes.put(r, d.getFrom().size());
                        sb.append(", candidate key for ");
                    }
                    else
                    {
                        sb.append(", superkey for ");
                    }
                    writeKey = false;
                }
                else
                {
                    sb.append(", ");
                }
                sb.append(r.getName());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
