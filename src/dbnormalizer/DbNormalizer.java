/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbnormalizer;

import java.util.Scanner;

/**
 *
 * @author Gebruiker
 */
public class DbNormalizer
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Schema schema = new Schema();

        Scanner in = new Scanner(System.in);
        String input = "start";
        int i = 1;
        System.out.println("Specify relations by typing a comma seperated list of attributes.");

        //Create relations
        while (true)
        {
            String name = "R" + i;
            System.out.printf("Relation %s: ", name);
            input = in.nextLine();

            if (input.isEmpty())
            {
                break;
            }

            try
            {
                Relation relation = new Relation(name, input);
                schema.getRelations().add(relation);
                System.out.printf("Relation created: %s", relation.toString());
                System.out.println();
                i++;
            } catch (InvalidAttributeStringException e)
            {
                System.err.println(e.getMessage());
            }
        }

        //Create Dependencies
        System.out.println("Specify dependencies for schema:");
        i = 1;
        while (true)
        {
            System.out.printf("Dependency %s, ", i);
            System.out.printf("Left hand side: ");
            input = in.nextLine();
            if (input.isEmpty())
            {
                break;
            }
            String lh = input;
            while (true)
            {
                System.out.println();
                System.out.printf("Dependency %s, ", i);
                System.out.printf("Right hand side: ");
                input = in.nextLine();
                if (!input.isEmpty())
                {
                    break;
                }
            }
            String rh = input;
            try
            {
                Dependency dependency = new Dependency(schema, lh, rh, false);
                schema.getDependencies().add(dependency);
                System.out.printf("Dependency created: %s", dependency.toString());
                System.out.println();
                i++;
            } catch (InvalidAttributeStringException ex)
            {
                System.err.println(ex.getMessage());
            }
        }
        
        String closureString = schema.computeClosure();
        System.out.println("Closure of the current schema: ");
        System.out.println(closureString);
    }

}