/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbnormalizer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author Gebruiker
 */
public class Dependency implements Comparable<Dependency>
{

    private final Schema schema;
    private SortedSet<String> from;
    private SortedSet<String> to;

    boolean multiValued;

    public SortedSet<String> getFrom()
    {
        return from;
    }

    public void setFrom(SortedSet<String> from)
    {
        this.from = from;
    }

    public SortedSet<String> getTo()
    {
        return to;
    }

    public void setTo(SortedSet<String> to)
    {
        this.to = to;
    }

    public boolean isMultiValued()
    {
        return multiValued;
    }

    public void setMultiValued(boolean multiValued)
    {
        this.multiValued = multiValued;
    }

    public Dependency(Schema schema, String from, String to, boolean multiValued) throws InvalidAttributeStringException
    {
        this.from = Util.stringToSortedSet(from);
        this.to = Util.stringToSortedSet(to);
        this.multiValued = multiValued;
        this.schema = schema;
    }

    public Dependency(Schema schema, SortedSet<String> from, SortedSet<String> to, boolean multiValued)
    {
        this.from = from;
        this.to = to;
        this.multiValued = multiValued;
        this.schema = schema;
    }
    
    public Dependency getClosure(Collection<Dependency> dependencies) 
    {
        SortedSet<String> closureTo = new TreeSet<>(this.to);
        closureTo.addAll(this.from);
        boolean changed = true;
        while (changed)
        {
            SortedSet<String> closureToPrevious = new TreeSet<>(closureTo);
            for (Dependency d : dependencies)
            {
                if (closureTo.containsAll(d.from))
                {
                    closureTo.addAll(d.to);
                }
            }
            
            changed = !closureToPrevious.equals(closureTo);
        }
        return new Dependency(schema, from, closureTo, false);
    }
    
    public Dependency removeExtraneousLH(Collection<Dependency> depedencies) {
        SortedSet<String> newFrom = new TreeSet<>();
        for (String s : this.from)
        {
            SortedSet<String> fromWithoutS = new TreeSet<>(this.from);
            fromWithoutS.remove(s);
            
            Dependency withoutS = new Dependency(this.schema, fromWithoutS, this.to, false);
            Dependency closure = withoutS.getClosure(depedencies);
            if (!closure.to.containsAll(this.to))
            {
                //s is not extraneous
                newFrom.add(s);
            }
        }
        return new Dependency(this.schema, newFrom, this.to, false);
    }
    
    public Dependency removeExtraneousRH(Collection<Dependency> dependencies)
    {
        SortedSet<String> newTo = new TreeSet<>();
        for (String s : this.to)
        {
            Set<Dependency> limitedFds = new HashSet<>(dependencies);
            limitedFds.remove(this);
            SortedSet<String> testTo = new TreeSet<>(this.to);
            testTo.remove(s);
            limitedFds.add(new Dependency(this.schema, this.from, testTo, false));
            Dependency closure = this.getClosure(limitedFds);
            if (!closure.to.contains(s))
            {
                //s is not extraneous
                newTo.add(s);
            }
        }
        return new Dependency(this.schema, this.from, newTo, false);
    }

    public String fromToString()
    {
        StringBuilder sb = new StringBuilder();
        for (String s : this.from)
        {
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    public String toToString()
    {
        StringBuilder sb = new StringBuilder();
        for (String s : this.to)
        {
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(fromToString());
        sb.append(" ");
        if (multiValued)
        {
            sb.append("→→");
        } else
        {
            sb.append("→");
        }
        sb.append(" ");
        sb.append(toToString());
        return sb.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o.getClass() != this.getClass())
        {
            return false;
        }
        Dependency other = (Dependency) o;
        return other.from.equals(this.from) && other.to.equals(this.to) && other.multiValued == this.multiValued;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.from);
        hash = 89 * hash + Objects.hashCode(this.to);
        hash = 89 * hash + (this.multiValued ? 1 : 0);
        return hash;
    }

    @Override
    public int compareTo(Dependency o)
    {
        if (this.from.size() != o.from.size())
        {
            return Integer.compare(this.from.size(), o.from.size());
        }
        if (this.fromToString().equals(o.fromToString()))
        {  
            return this.toToString().compareTo(o.toToString());
        }
        return this.fromToString().compareTo(o.fromToString());
    }

}
