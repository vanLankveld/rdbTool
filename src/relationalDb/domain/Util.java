/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package relationalDb.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author Gebruiker
 */
public class Util
{

    public static final String attrStringRegex = "([a-zA-Z0-9]([,\\s;]+[a-zA-Z0-9])*)*";

    public static SortedSet<String> stringToSortedSet(String input) throws InvalidAttributeStringException
    {
        if (!input.matches(Util.attrStringRegex))
        {
            throw new InvalidAttributeStringException(input);
        }
        String[] attributesSplit = input.split("[,\\s;]+");
        TreeSet<String> set = new TreeSet<>();
        set.addAll(Arrays.asList(attributesSplit));
        return set;
    }

    public static Integer[] getOneIndices(String binaryRepresentation)
    {
        binaryRepresentation = new StringBuilder(binaryRepresentation).reverse().toString();
        ArrayList<Integer> indices = new ArrayList<>();
        if (binaryRepresentation.contains("1"))
        {
            for (int i = 0; i < binaryRepresentation.length(); i++)
            {
                char charAt = binaryRepresentation.charAt(i);
                if (charAt == '1')
                {
                    indices.add(i);
                }
            }
        }
        return indices.toArray(new Integer[indices.size()]);
    }
}
