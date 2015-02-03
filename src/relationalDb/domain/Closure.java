/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package relationalDb.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gebruiker
 */
public class Closure
{

    private Schema schema;
    private Set<Dependency> closures;

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
        for (Dependency d : this.closures)
        {
            sb.append(d.toString());
            Set<Relation> keyRelations = d.candidateKeyFor();
            boolean writeKey = true;
            for (Relation r : keyRelations)
            {
                if (writeKey)
                {
                    sb.append(", candidate key for ");
                    writeKey = false;
                } else
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
