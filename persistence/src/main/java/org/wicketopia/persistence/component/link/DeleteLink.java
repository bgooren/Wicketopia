/*
 * Copyright (c) 2011 Carman Consulting, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wicketopia.persistence.component.link;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.wicketopia.persistence.PersistenceProvider;

public class DeleteLink<T> extends Link<T> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private PersistenceProvider persistenceProvider;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public DeleteLink(String id, IModel<T> model, PersistenceProvider persistenceProvider) {
        super(id, model);
        this.persistenceProvider = persistenceProvider;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    protected void afterDelete(T object) {
        // Do nothing by default.
    }

    @Override
    public final void onClick() {
        final T object = getModelObject();
        persistenceProvider.delete(object);
        afterDelete(object);
    }
}
