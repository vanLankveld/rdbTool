/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package relationalDb.domain;

import java.util.Set;

/**
 *
 * @author Gebruiker
 */
public abstract class Normalizer
{
    private Schema schema;

    public Schema getSchema()
    {
        return schema;
    }

    public void setSchema(Schema schema)
    {
        this.schema = schema;
    }

    public Normalizer(Schema schema)
    {
        this.schema = schema;
    }
    
    public abstract Set<Relation> decompose();
}
