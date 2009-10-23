/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hawtdb.internal.index;

import org.apache.activemq.util.buffer.Buffer;
import org.apache.activemq.util.marshaller.FixedBufferMarshaller;
import org.apache.activemq.util.marshaller.LongMarshaller;
import org.apache.hawtdb.api.BTreeIndexFactory;
import org.apache.hawtdb.api.Index;
import org.apache.hawtdb.api.Transaction;
import org.apache.hawtdb.internal.Benchmarker.BenchmarkAction;
import org.junit.Test;

/**
 * 
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class BTreeIndexBenchmark extends IndexBenchmark {

    private boolean deferredEncoding;

    public BTreeIndexBenchmark() {
        this.benchmark.setSamples(5);
    }
    
    protected Index<Long, Buffer> createIndex(Transaction tx) {
        BTreeIndexFactory<Long, Buffer> factory = new BTreeIndexFactory<Long, Buffer>();
        factory.setKeyMarshaller(LongMarshaller.INSTANCE);
        factory.setValueMarshaller(new FixedBufferMarshaller(DATA.length));
        factory.setDeferredEncoding(deferredEncoding);
        return factory.create(tx, tx.allocator().alloc(1));
    }

    @Test
    public void insertDeffered() throws Exception {
        deferredEncoding = true;
        benchmark.benchmark(1, new BenchmarkAction<IndexActor>("insert with deffered encoding") {
            protected void execute(IndexActor actor) throws InterruptedException {
                actor.benchmarkIndex();
            }
        });        
    }

    @Test
    public void insert() throws Exception {
        deferredEncoding = false;
        benchmark.benchmark(1, new BenchmarkAction<IndexActor>("insert without deffered encoding") {
            protected void execute(IndexActor actor) throws InterruptedException {
                actor.benchmarkIndex();
            }
        });        
    }
    
}