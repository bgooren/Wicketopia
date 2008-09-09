package org.wicketopia.metadata;

import org.apache.commons.lang.StringUtils;
import org.wicketopia.editor.PropertyEditorFacet;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @since 1.0
 */
public class PropertyMetadata implements Serializable, Comparable
{
//**********************************************************************************************************************
// Fields
//**********************************************************************************************************************

    private static final long serialVersionUID = 1L;
    private final String propertyName;
    private final BeanMetadata beanMetadata;
    private final Class<?> propertyType;
    private String labelTextMessageKey;
    private String defaultLabelText;
    private int order = 0;
    private String editorType;
    private Set<PropertyEditorFacet> facets = new HashSet<PropertyEditorFacet>();

//**********************************************************************************************************************
// Constructors
//**********************************************************************************************************************

    PropertyMetadata( BeanMetadata beanMetadata, PropertyDescriptor propertyDescriptor )
    {
        this.beanMetadata = beanMetadata;
        this.propertyName = propertyDescriptor.getName();
        this.propertyType = propertyDescriptor.getPropertyType();
        this.labelTextMessageKey = beanMetadata.getBeanClass().getName() + "." + propertyDescriptor.getName();
        this.defaultLabelText = calculateDefaultLabelText(propertyDescriptor);
    }

//**********************************************************************************************************************
// Comparable Implementation
//**********************************************************************************************************************


    public PropertyDescriptor getPropertyDescriptor()
    {
        try
        {
            PropertyDescriptor[] propertyDescriptors =
                    Introspector.getBeanInfo(beanMetadata.getBeanClass()).getPropertyDescriptors();
            for( PropertyDescriptor propertyDescriptor : propertyDescriptors )
            {
                if( propertyName.equals(propertyDescriptor.getName()) )
                {
                    return propertyDescriptor;
                }
            }
            return null;
        }
        catch( IntrospectionException e )
        {
            throw new RuntimeException("Unable to obtain property descriptor.", e);
        }


    }

    public int compareTo( Object o )
    {
        if( o instanceof PropertyMetadata )
        {
            PropertyMetadata other = ( PropertyMetadata ) o;
            return new Integer(order).compareTo(other.order);
        }
        return 1;
    }

//**********************************************************************************************************************
// Getter/Setter Methods
//**********************************************************************************************************************

    public BeanMetadata getBeanMetadata()
    {
        return beanMetadata;
    }

    public String getDefaultLabelText()
    {
        return defaultLabelText;
    }

    public void setDefaultLabelText( String defaultLabelText )
    {
        this.defaultLabelText = defaultLabelText;
    }

    public String getEditorType()
    {
        return editorType;
    }

    public void setEditorType( String editorType )
    {
        this.editorType = editorType;
    }

    public Set<PropertyEditorFacet> getFacets()
    {
        return facets;
    }

    public String getLabelTextMessageKey()
    {
        return labelTextMessageKey;
    }

    public void setLabelTextMessageKey( String labelTextMessageKey )
    {
        this.labelTextMessageKey = labelTextMessageKey;
    }

    public int getOrder()
    {
        return order;
    }

    public void setOrder( int order )
    {
        this.order = order;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public Class<?> getPropertyType()
    {
        return propertyType;
    }

//**********************************************************************************************************************
// Other Methods
//**********************************************************************************************************************

    private String calculateDefaultLabelText( PropertyDescriptor propertyDescriptor )
    {
        String[] splits = StringUtils.splitByCharacterTypeCamelCase(propertyDescriptor.getName());
        splits[0] = StringUtils.capitalize(splits[0]);
        return StringUtils.join(splits);
    }

    public void addFacet( PropertyEditorFacet facet )
    {
        facets.add(facet);
    }
}