/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package relationalDb.domain;

import java.util.Collection;
import java.util.HashSet;
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
        SortedSet<String> attributes = new TreeSet<>();
        for (Relation r : this.schema.getRelations())
        {
            attributes.addAll(r.getAttributes());
        }

        //Get the closure for all single attributes
        for (String a : attributes)
        {
            try
            {
                Dependency closureDependency = new Dependency(schema, a, a, false);
                closures.add(closureDependency.getClosure(this.schema.getDependencies()));
            } catch (InvalidAttributeStringException ex)
            {
                Logger.getLogger(Closure.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        for (Dependency d : this.schema.getDependencies())
        {
            if (d.getFrom().size() == 1)
            {
                continue;
            }
            closures.add(d.getClosure(this.schema.getDependencies()));
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
