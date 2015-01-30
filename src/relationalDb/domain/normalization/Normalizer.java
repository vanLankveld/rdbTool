/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package relationalDb.domain.normalization;

import relationalDb.domain.Schema;

/**
 *
 * @author Gebruiker
 */
public abstract class Normalizer
{
    protected Schema schema;

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
    
    public abstract Schema normalize();
}
