/*******************************************************************************
 *     ___                  _   ____  ____
 *    / _ \ _   _  ___  ___| |_|  _ \| __ )
 *   | | | | | | |/ _ \/ __| __| | | |  _ \
 *   | |_| | |_| |  __/\__ \ |_| |_| | |_) |
 *    \__\_\\__,_|\___||___/\__|____/|____/
 *
 *  Copyright (c) 2014-2019 Appsicle
 *  Copyright (c) 2019-2023 QuestDB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/

package io.questdb.griffin.engine.functions;

import io.questdb.cairo.sql.Function;
import io.questdb.cairo.sql.SymbolTableSource;
import io.questdb.griffin.PlanSink;
import io.questdb.griffin.SqlException;
import io.questdb.griffin.SqlExecutionContext;
import io.questdb.griffin.engine.groupby.GroupByUtils;
import io.questdb.std.Misc;
import io.questdb.std.ObjList;

public interface MultiArgFunction extends Function {

    @Override
    default void close() {
        Misc.freeObjList(getArgs());
    }

    ObjList<Function> getArgs();

    @Override
    default void init(SymbolTableSource symbolTableSource, SqlExecutionContext executionContext) throws SqlException {
        Function.init(getArgs(), symbolTableSource, executionContext);
    }

    @Override
    default boolean isConstant() {
        ObjList<Function> args = getArgs();
        for (int i = 0, n = args.size(); i < n; i++) {
            if (!args.getQuick(i).isConstant()) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean isReadThreadSafe() {
        final ObjList<Function> args = getArgs();
        for (int i = 0, n = args.size(); i < n; i++) {
            final Function function = args.getQuick(i);
            if (!function.isReadThreadSafe()) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean isRuntimeConstant() {
        final ObjList<Function> args = getArgs();
        for (int i = 0, n = args.size(); i < n; i++) {
            final Function function = args.getQuick(i);
            if (!function.isRuntimeConstant() && !function.isConstant()) {
                return false;
            }
        }
        return true;
    }

    @Override
    default void toPlan(PlanSink sink) {
        sink.val(getName()).val('(').val(getArgs()).val(')');
    }

    @Override
    default void toTop() {
        GroupByUtils.toTop(getArgs());
    }
}
