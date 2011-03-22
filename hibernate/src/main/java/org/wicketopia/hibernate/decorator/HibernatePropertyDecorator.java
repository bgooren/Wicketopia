package org.wicketopia.hibernate.decorator;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.metadata.ClassMetadata;
import org.metastopheles.BeanMetaData;
import org.metastopheles.MetaDataDecorator;
import org.metastopheles.PropertyMetaData;
import org.wicketopia.editor.annotation.required.Required;
import org.wicketopia.editor.context.EditorContext;
import org.wicketopia.editor.decorator.RequiredDecorator;
import org.wicketopia.metadata.WicketopiaFacet;

import java.util.Iterator;

public class HibernatePropertyDecorator implements MetaDataDecorator<PropertyMetaData>
{
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Configuration configuration;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public HibernatePropertyDecorator(Configuration configuration)
    {
        this.configuration = configuration;
    }

//----------------------------------------------------------------------------------------------------------------------
// MetaDataDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    public void decorate(PropertyMetaData propertyMetaData)
    {
        WicketopiaFacet facet = WicketopiaFacet.get(propertyMetaData);
        final PersistentClass classMapping = findClassMapping(propertyMetaData.getBeanMetaData());
        if(classMapping != null)
        {
            final Property property = classMapping.getProperty(propertyMetaData.getPropertyDescriptor().getName());
            property.getValue();
            if(property == null)
            {
               // Do nothing, skip!
            }
            else if(property.equals(classMapping.getIdentifierProperty()))
            {
                facet.setIgnored(true);
            }
            else if(property.equals(classMapping.getVersion()))
            {
                facet.setIgnored(true);
            }

            if(!property.isOptional())
            {
                facet.addDecorator(RequiredDecorator.required());
            }
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------


    private PersistentClass findClassMapping(BeanMetaData beanMetaData)
    {
        for(Iterator i = configuration.getClassMappings(); i.hasNext(); )
        {
            PersistentClass classMapping = (PersistentClass)i.next();
            if(classMapping.getMappedClass().equals(beanMetaData.getBeanDescriptor().getBeanClass()))
            {
                return classMapping;
            }
        }
        return null;
    }
}