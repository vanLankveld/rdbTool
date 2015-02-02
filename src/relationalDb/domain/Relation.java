/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package relationalDb.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author Gebruiker
 */
public class Relation
{

    private String name;
    private SortedSet<String> attributes;
    private Schema schema;

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

    /**
     * Gets all dependencies that hold in this relation
     *
     * @return
     */
    public Set<Dependency> getDependencies()
    {
        Set<Dependency> dependencies = new HashSet<>();
        for (Dependency d : this.schema.getDependencies())
        {
            Set<String> toAndFrom = new TreeSet<>(d.getFrom());
            toAndFrom.addAll(d.getTo());
            if (this.attributes.containsAll(d.getTo()))
            {
                dependencies.add(d);
            }
        }
        return dependencies;
    }

    public Set<Dependency> violatesBcnf()
    {
        Set<Dependency> violations = new HashSet<>();
        for (Dependency d : this.getDependencies())
        {
            if (d.isTrivial())
            {
                continue;
            }
            Set<Relation> superkeyRelations = d.candidateKeyFor();
            if (!superkeyRelations.contains(this))
            {
                violations.add(d);
            }
        }
        return violations;
    }

    public Set<Dependency> violates3nf()
    {
        Set<Dependency> violations = new HashSet<>();
        for (Dependency d : this.getDependencies())
        {
            if (d.isTrivial())
            {
                continue;
            }
            Set<String> toMinusFrom = new HashSet<>(d.getTo());
            toMinusFrom.removeAll(d.getFrom());
            boolean isContainedInKey = false;
            for (Dependency k : this.getCandidateKeys())
            {
                if (k.getFrom().containsAll(toMinusFrom))
                {
                    isContainedInKey = true;
                    break;
                }
            }
            Set<Relation> superkeyRelations = d.candidateKeyFor();
            if (!isContainedInKey && !superkeyRelations.contains(this))
            {
                violations.add(d);
            }
        }
        return violations;
    }

    public Set<Dependency> getCandidateKeys()
    {
        Set<Dependency> candidateKeys = new HashSet<>();
        Closure closure = this.schema.getClosure();
        for (Dependency d : closure.getClosures())
        {
            if (d.getTo().equals(this.attributes))
            {
                candidateKeys.add(d);
            }
        }
        return candidateKeys;
    }

    public Relation(Schema schema, String name, String attributeString) throws InvalidAttributeStringException
    {
        this.schema = schema;
        this.name = name;
        this.attributes = Util.stringToSortedSet(attributeString);
    }

    public Relation(Schema schema, String name, Collection<String> attributes)
    {
        this.schema = schema;
        this.name = name;
        this.attributes = new TreeSet<>(attributes);
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
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append("(");
        Iterator<String> it = this.getAttributes().iterator();
        while (it.hasNext())
        {
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
