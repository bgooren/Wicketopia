/*
 * Copyright (c) 2011 Carman Consulting, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wicketopia.metadata;

import org.apache.commons.lang.StringUtils;
import org.metastopheles.FacetKey;
import org.metastopheles.PropertyMetaData;
import org.wicketopia.builder.EditorBuilder;
import org.wicketopia.builder.ViewerBuilder;
import org.wicketopia.builder.feature.ComponentBuilderFeature;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @since 1.0
 */
public class WicketopiaFacet implements Comparable, Serializable
{
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final FacetKey<WicketopiaFacet> FACET_KEY = new FacetKey<WicketopiaFacet>()
    {
    };

    private String labelTextMessageKey;
    private String defaultLabelText;
    private int order = Integer.MAX_VALUE;
    private String editorType;
    private String viewerType;
    private final Set<ComponentBuilderFeature<EditorBuilder>> editorFeatures = new HashSet<ComponentBuilderFeature<EditorBuilder>>();
    private final Set<ComponentBuilderFeature<ViewerBuilder>> viewerFeatures = new HashSet<ComponentBuilderFeature<ViewerBuilder>>();
    private boolean ignored = false;
    private final PropertyMetaData propertyMetaData;

//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    public static WicketopiaFacet get(PropertyMetaData propertyMetaData)
    {
        synchronized (propertyMetaData)
        {
            WicketopiaFacet meta = propertyMetaData.getFacet(FACET_KEY);
            if (meta == null)
            {
                meta = new WicketopiaFacet(propertyMetaData);
                propertyMetaData.setFacet(FACET_KEY, meta);
            }
            return meta;
        }
    }

    public static void sort(List<PropertyMetaData> propertyMetaDataList)
    {
        Collections.sort(propertyMetaDataList, new Comparator<PropertyMetaData>()
        {
            @Override
            public int compare(PropertyMetaData o1, PropertyMetaData o2)
            {
                return WicketopiaFacet.get(o1).compareTo(WicketopiaFacet.get(o2));
            }
        });
    }

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    WicketopiaFacet(PropertyMetaData propertyMetaData)
    {
        this.propertyMetaData = propertyMetaData;
        this.labelTextMessageKey = propertyMetaData.getBeanMetaData().getBeanDescriptor().getBeanClass().getName() + "." + propertyMetaData.getPropertyDescriptor().getName();
        this.defaultLabelText = calculateDefaultLabelText(propertyMetaData.getPropertyDescriptor());
    }

    private String calculateDefaultLabelText(PropertyDescriptor propertyDescriptor)
    {
        String[] words = StringUtils.splitByCharacterTypeCamelCase(propertyDescriptor.getName());
        words[0] = StringUtils.capitalize(words[0]);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++)
        {
            String word = words[i];
            sb.append(word);
            if (i != words.length - 1)
            {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

//----------------------------------------------------------------------------------------------------------------------
// Comparable Implementation
//----------------------------------------------------------------------------------------------------------------------

    public int compareTo(Object o)
    {
        if (o instanceof WicketopiaFacet)
        {
            WicketopiaFacet other = (WicketopiaFacet) o;
            return new Integer(getOrder()).compareTo(other.getOrder());
        }
        return 1;
    }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public String getDefaultLabelText()
    {
        return defaultLabelText;
    }

    public void setDefaultLabelText(String defaultLabelText)
    {
        this.defaultLabelText = defaultLabelText;
    }

    public Set<ComponentBuilderFeature<EditorBuilder>> getEditorFeatures()
    {
        return editorFeatures;
    }

    public String getEditorType()
    {
        return editorType;
    }

    public void setEditorType(String editorType)
    {
        this.editorType = editorType;
    }

    public String getLabelTextMessageKey()
    {
        return labelTextMessageKey;
    }

    public void setLabelTextMessageKey(String labelTextMessageKey)
    {
        this.labelTextMessageKey = labelTextMessageKey;
    }

    public int getOrder()
    {
        return order;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    public PropertyMetaData getPropertyMetaData()
    {
        return propertyMetaData;
    }

    public Set<ComponentBuilderFeature<ViewerBuilder>> getViewerFeatures()
    {
        return viewerFeatures;
    }

    public String getViewerType()
    {
        return viewerType;
    }

    public void setViewerType(String viewerType)
    {
        this.viewerType = viewerType;
    }

    public boolean isIgnored()
    {
        return ignored;
    }

    public void setIgnored(boolean ignored)
    {
        this.ignored = ignored;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public void addEditorFeature(ComponentBuilderFeature<EditorBuilder> feature)
    {
        editorFeatures.add(feature);
    }

    public void addViewerFeature(ComponentBuilderFeature<ViewerBuilder> feature)
    {
        viewerFeatures.add(feature);
    }

    private Object writeReplace()
    {
        return new SerializedForm(getPropertyMetaData());
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    private static class SerializedForm implements Serializable
    {
        private final PropertyMetaData propertyMetaData;

        private SerializedForm(PropertyMetaData propertyMetaData)
        {
            this.propertyMetaData = propertyMetaData;
        }

        private Object readResolve()
        {
            return WicketopiaFacet.get(propertyMetaData);
        }
    }
}
