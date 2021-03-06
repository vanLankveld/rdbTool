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
    
    public Set<Relation> getNotInBcnf()
    {
        Set<Relation> notInBcnf = new HashSet<>();
        for (Relation r : this.getRelations())
        {
            if (!r.violatesBcnf().isEmpty() && r.getAttributes().size() > 2)
            {
                notInBcnf.add(r);
            }
        }
        return notInBcnf;
    }
    
    public Set<Relation> getNotInThirdNF()
    {
        Set<Relation> notIn3nf = new HashSet<>();
        for (Relation r : this.getRelations())
        {
            if (!r.violates3nf().isEmpty() && r.getAttributes().size() > 2)
            {
                notIn3nf.add(r);
            }
        }
        return notIn3nf;
    }

    public Set<Dependency> getDependencies()
    {
        return dependencies;
    }

    public Closure getClosure()
    {
        if (this.closure == null)
        {
            this.closure = new Closure(this);
        }
        return closure;
    }

    public Schema()
    {
        this.relations = new HashSet<>();
        this.dependencies = new HashSet<>();
    }
    
    public Schema(Schema copyOf)
    {
        this.relations = copyOf.relations;
        this.dependencies = copyOf.dependencies;
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
