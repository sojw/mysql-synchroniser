/*
 *  Copyright (c) 2013 James Buncle
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package com.jbuncle.mysqlsynchroniser.structure.objects;

import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

/**
 *
 * @author James Buncle
 */
public class Database {

    private final Map<String, Table> tables;
    private final Map<String, View> views;

    public Database(final Map<String, Table> tables, final Map<String, View> views) {
        this.tables = tables;
        this.views = views;
    }

    public Map<String, Table> getTables() {
        return tables;
    }

    public Map<String, View> getViews() {
        return views;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Database)) {
            return false;
        }
        final Database other = (Database) obj;
        if (!CollectionUtils.isEqualCollection(this.tables.values(), other.tables.values())) {
            return false;
        }
        return CollectionUtils.isEqualCollection(this.views.values(), other.views.values());
    }

}
