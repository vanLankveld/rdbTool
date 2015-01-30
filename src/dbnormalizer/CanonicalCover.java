/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbnormalizer;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author Gebruiker
 */
public class CanonicalCover
{

    private SortedSet<Dependency> dependencyFds;

    public CanonicalCover(SortedSet<Dependency> initialFds)
    {
        this.dependencyFds = computeCover(initialFds);
    }

    private SortedSet<Dependency> computeCover(SortedSet<Dependency> initialFds)
    {
        SortedSet<Dependency> oldFds = new TreeSet<>(initialFds);
        SortedSet<Dependency> newFds = new TreeSet<>();

        boolean completed = false;

        while (!completed)
        {
            SortedSet<Dependency> currentSet = new TreeSet<>(oldFds);

            for (Dependency d : currentSet)
            {
                newFds.add(d.removeExtraneousLH(currentSet));
            }

            currentSet = new TreeSet<>(newFds);
            newFds.clear();

            for (Dependency d : currentSet)
            {
                newFds.add(d.removeExtraneousRH(currentSet));
            }

            currentSet = new TreeSet<>(newFds);
            newFds.clear();

            Set<Dependency> removed = new HashSet<>();
            for (Dependency d1 : currentSet)
            {
                for (Dependency d2 : currentSet)
                {
                    if (d1.getFrom().equals(d2.getFrom()) && !d1.equals(d2))
                    {
                        removed.add(d1);
                        removed.add(d2);
                        SortedSet<String> newTo = new TreeSet<>(d1.getTo());
                        newTo.addAll(d2.getTo());
                        Dependency union = new Dependency(d1.getSchema(), d1.getFrom(), newTo, false);
                        newFds.add(union);
                    }
                }
            }

            Set<Dependency> notRemoved = new TreeSet<>(currentSet);
            notRemoved.removeAll(removed);

            newFds.addAll(notRemoved);

            if (oldFds.equals(newFds))
            {
                completed = true;
            } else
            {
                oldFds = new TreeSet<>(newFds);
                newFds.clear();
            }
        }
        return newFds;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (Dependency d : this.dependencyFds)
        {
            sb.append(d.toString());
            sb.append("\n");
        }
        return sb.toString().trim();
    }

}
