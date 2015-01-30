/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package relationalDb.domain.normalization;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import relationalDb.domain.Dependency;
import relationalDb.domain.Relation;
import relationalDb.domain.Schema;

/**
 *
 * @author Gebruiker
 */
public class BcnfNormalizer extends Normalizer
{

    public BcnfNormalizer(Schema schema)
    {
        super(schema);
    }

    @Override
    public Schema normalize()
    {
        Schema bcnfSchema = this.schema;
        while (!bcnfSchema.getNotInBcnf().isEmpty())
        {
            List<Relation> allNotInBcnf = new ArrayList<>(this.schema.getNotInBcnf());
            Relation notInBcnf = allNotInBcnf.get(0);
            List<Dependency> allViolations = new ArrayList<>(notInBcnf.violatesBcnf());
            Dependency violation = allViolations.get(0);
            SortedSet<String> r1Attributes = new TreeSet<>(notInBcnf.getAttributes());
            r1Attributes.removeAll(violation.getTo());
            Relation r1 = new Relation(bcnfSchema, notInBcnf.getName()+"a", r1Attributes);
            SortedSet<String> r2Attributes = new TreeSet<>(violation.getFrom());
            r2Attributes.addAll(violation.getTo());
            Relation r2 = new Relation(bcnfSchema, notInBcnf.getName()+"b", r2Attributes);
            bcnfSchema.getRelations().remove(notInBcnf);
            bcnfSchema.getRelations().add(r1);
            bcnfSchema.getRelations().add(r2);
        }
        
        return bcnfSchema;
    }

}
