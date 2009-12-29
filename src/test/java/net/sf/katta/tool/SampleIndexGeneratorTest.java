/**
 * Copyright 2008 the original author or authors.
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

package net.sf.katta.tool;

import static org.junit.Assert.assertTrue;

import java.io.File;

import net.sf.katta.AbstractTest;

import org.apache.lucene.index.IndexReader;
import org.junit.Test;

public class SampleIndexGeneratorTest extends AbstractTest {

  @Test
  public void testCreateIndex() throws Exception {
    SampleIndexGenerator sampleIndexGenerator = new SampleIndexGenerator();
    File file = _temporaryFolder.newFolder("sampleIndex");
    file.mkdirs();
    sampleIndexGenerator.createIndex("./extras/benchmark/resources/alice.txt", file.getAbsolutePath(), 10, 10);
    assertTrue(IndexReader.indexExists(file.listFiles()[0]));
  }

}