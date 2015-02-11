/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package relationalDb.domain.normalization;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import relationalDb.domain.CanonicalCover;
import relationalDb.domain.Dependency;
import relationalDb.domain.Relation;
import relationalDb.domain.Schema;

/**
 * blic class ThirdNFNormalizer extends Normalizer {
 *
 * public ThirdNFNormalizer(Schema schema) { super(schema); }
 *
 * @Override public Schema normalize() { Schema thirdNfSchema = this.schema;
 *
 *
 *
 * return thirdNfSchema; }
 *
 * }
 * @author Guus van Lankveld
 */
public class ThirdNFNormalizer extends Normalizer
{

    public ThirdNFNormalizer(Schema schema)
    {
        super(schema);
    }

    @Override
    public Schema normalize()
    {
        Schema thirdNfSchema = new Schema();
        schema.getDependencies().addAll(this.schema.getDependencies());
        
        
        
        CanonicalCover cover = new CanonicalCover(new TreeSet<>(this.schema.getDependencies()));

        int i = 0;
        for (Dependency d : cover.getDependencyFds())
        {
            Set<String> attributes = new HashSet<>(d.getFrom());
            attributes.addAll(d.getTo());
            boolean containsD = false;
            for (Relation r : this.schema.getRelations())
            {
                if (r.getAttributes().containsAll(attributes))
                {
                    containsD = true;
                }
            }

            if (!containsD)
            {
                continue;
            }

            i++;
            Relation r = new Relation(thirdNfSchema, "R" + i, attributes);
            thirdNfSchema.getRelations().add(r);
        }
        
        for (Relation r : thirdNfSchema.getRelations())
        {
            if (r.getCandidateKeys().size() > 0)
            {
                return thirdNfSchema;
            }
        }
        
        return thirdNfSchema;
    }

}
