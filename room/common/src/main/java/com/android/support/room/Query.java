/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.support.room;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method in a {@link Dao} annotated class as a query method.
 * <p>
 * The value of the annotation includes the query that will be run when this method is called. This
 * query is <b>verified at compile time</b> by Room to ensure that it compiles fine against the
 * database.
 * <p>
 * The arguments of the method will be bound to the bind arguments in the SQL statement. See
 * < href="https://www.sqlite.org/c3ref/bind_blob.html">SQLite's binding documentation</> for
 * details of bind arguments in SQLite.
 * <p>
 * Room supports only 2 types of bind arguments. {@code ?} placeholder and {@code :name}
 * named bind parameter. If there are more than 1 arguments, you must use named bind parameters to
 * avoid confusion.
 * <p>
 * Room will automatically bind the parameters of the method into the bind arguments. When named
 * bind arguments are used, this is done by matching the name of the parameters to the name of the
 * bind arguments.
 * <pre>
 *     {@literal @}Query("SELECT * FROM user WHERE user_name LIKE :name AND last_name LIKE :last")
 *     public abstract List&lt;User&gt; findUsersByNameAndLastName(String name, String last);
 * </pre>
 * <p>
 * As an extension over SQLite bind arguments, Room supports binding a list of parameters to the
 * query. At runtime, Room will build the correct query to have matching number of bind arguments
 * depending on the number of items in the method parameter.
 * <pre>
 *     {@literal @}Query("SELECT * FROM user WHERE uid IN(?)")
 *     public abstract List<User> findByIds(int[] userIds);
 * </pre>
 * For the example above, if the {@code userIds} is an array of 3 elements, Room will run the
 * query as: {@code SELECT * FROM user WHERE uid IN(?, ?, ?)} and bind each item in the
 * {@code userIds} array into the statement.
 * <p>
 * There are 3 types of queries supported in {@code Query} methods: SELECT, UPDATE and DELETE.
 * <p>
 * For SELECT queries, Room will infer the result contents from the method's return type and
 * generate the code that will automatically convert the query result into the method's return
 * type.
 * <p>
 * UPDATE or DELETE queries can return {@code void} or {@code int}. If it is an {@code int},
 * the value is the number of rows affected by this query.
 * <p>
 * You can return arbitrary POJOs from your query methods as long as the fields of the POJO match
 * the column names in the query result.
 * For example, if you have class:
 * <pre>
 * class UserName {
 *     public String name;
 *     {@literal @}ColumnInfo(name = "last_name")
 *     public String lastName;
 * }
 * </pre>
 * You can write a query like this:
 * <pre>
 *     {@literal @}Query("SELECT last_name, name FROM user WHERE uid = ? LIMIT 1")
 *     public abstract UserName findOneUserName(int userId);
 * </pre>
 * And Room will create the correct implementation to convert the query result into a
 * {@code UsrName} object. If there is a mismatch between the query result and the fields of the
 * POJO, Room prints a {@link RoomWarnings#CURSOR_MISMATCH} warning and sets as many fields as it
 * can.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Query {
    /**
     * The SQLite query to be run.
     * @return The query to be run.
     */
    String value();
}
