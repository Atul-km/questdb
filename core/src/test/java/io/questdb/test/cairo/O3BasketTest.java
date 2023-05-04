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

package io.questdb.test.cairo;


import io.questdb.cairo.CairoConfiguration;
import io.questdb.cairo.DefaultCairoConfiguration;
import io.questdb.cairo.O3Basket;
import io.questdb.test.AbstractTest;
import org.junit.Assert;
import org.junit.Test;

public class O3BasketTest extends AbstractTest {
    @Test
    public void testSimple() {
        O3Basket basket = new O3Basket();
        final CairoConfiguration configuration = new DefaultCairoConfiguration(root);

        basket.ensureCapacity(configuration,5, 2);
        assertBasket(basket, 5, 2);
        basket.clear();
        assertBasket(basket, 5, 2);

        basket.clear();
        basket.ensureCapacity(configuration,8, 2);
        assertBasket(basket, 8, 2);

        basket.clear();
        basket.ensureCapacity(configuration,8, 4);
        assertBasket(basket, 8, 4);

        basket.clear();
        basket.ensureCapacity(configuration, 3, 1);
        assertBasket(basket, 3, 1);

    }

    private void assertBasket(O3Basket basket, int columnCount, int indexerCount) {
        for (int i = 0; i < columnCount; i++) {
            Assert.assertNotNull(basket.nextPartCounter());
        }

        for (int i = 0; i < indexerCount; i++) {
            Assert.assertNotNull(basket.nextIndexer());
        }
    }
}