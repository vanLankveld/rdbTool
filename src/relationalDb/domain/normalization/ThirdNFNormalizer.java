/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package relationalDb.domain.normalization;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import relationalDb.domain.Dependency;
import relationalDb.domain.Relation;
import relationalDb.domain.Schema;

/**
 *
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
        Schema bcnfSchema = this.schema;
        while (!bcnfSchema.getNotInThirdNF().isEmpty())
        {
            
        }
        return bcnfSchema;
    }
    
}
