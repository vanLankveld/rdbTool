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

/**
 *
 * @author Gebruiker
 */
public class Schema
{
    private final Set<Relation> relations;
    private final Set<Dependency> dependencies;
    private Closure closure = null;

    public Set<Relation> getRelations()
    {
        return relations;
    }

    public Set<Dependency> getDependencies()
    {
        return dependencies;
    }

    public Closure getClosure()
    {
        return closure;
    }

    public Schema()
    {
        this.relations = new HashSet<>();
        this.dependencies = new HashSet<>();
    }
    
    public String computeClosure()
    {
        this.closure = new Closure(this);
        return this.closure.toString();
    }
    
    public SortedSet<String> getTransitiveTo(Dependency origin, Collection<Dependency> dependencies)
    {
        SortedSet<String> newTo = origin.getTo();
        for(Dependency d : dependencies)
        {
            if (origin.equals(d))
            {
                continue;
            }
            
            SortedSet<String> intersect = new TreeSet<>(origin.getTo());
            intersect.retainAll(d.getFrom());
            
            if (intersect.equals(d.getFrom()))
            {
                if (origin.getTo().containsAll(d.getTo()))
                {
                    continue;
                }
                newTo.addAll(d.getTo());
                newTo.addAll(getTransitiveTo(d, dependencies));
            }
        }
        return newTo;
    }
}
