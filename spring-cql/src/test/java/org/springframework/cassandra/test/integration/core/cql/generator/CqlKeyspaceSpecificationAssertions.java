/*
 * Copyright 2013-2014 the original author or authors.
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
package org.springframework.cassandra.test.integration.core.cql.generator;

import static org.junit.Assert.*;

import java.util.Map;

import org.springframework.cassandra.core.keyspace.KeyspaceDescriptor;
import org.springframework.cassandra.core.keyspace.Option;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Session;

/**
 * @author John McPeek
 */
public class CqlKeyspaceSpecificationAssertions {

	@SuppressWarnings("unchecked")
	public static void assertKeyspace(KeyspaceDescriptor expected, String keyspace, Session session) {
		KeyspaceMetadata kmd = session.getCluster().getMetadata().getKeyspace(keyspace.toLowerCase());

		assertEquals(expected.getName(), kmd.getName());

		Map<String, String> options = kmd.getReplication();
		Map<String, Object> expectedOptions = expected.getOptions();
		Map<Option, Object> replicationMap = (Map<Option, Object>) expectedOptions.get("replication");
		assertEquals(replicationMap.size(), options.size());

		for (Map.Entry<Option, Object> optionEntry : replicationMap.entrySet()) {
			String optionValue = options.get(optionEntry.getKey().getName());
			String repMapValue = "" + optionEntry.getValue();
			assertTrue(optionValue.endsWith(repMapValue));
		}
	}
}
