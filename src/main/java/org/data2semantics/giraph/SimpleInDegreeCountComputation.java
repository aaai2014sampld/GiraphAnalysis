/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.data2semantics.giraph;

import org.apache.giraph.Algorithm;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.edge.Edge;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;

/**
 * Simple function to return the out degree for each vertex.
 */
@Algorithm(name = "Indegree Count")
public class SimpleInDegreeCountComputation extends
		BasicComputation<Text, LongWritable, NullWritable, DoubleWritable> {

	@Override
	public void compute(Vertex<Text, LongWritable, NullWritable> vertex, Iterable<DoubleWritable> messages) throws IOException {
		if (getSuperstep() == 0) {
			Iterable<Edge<Text, NullWritable>> edges = vertex.getEdges();
			for (Edge<Text, NullWritable> edge : edges) {
				sendMessage(edge.getTargetVertexId(), new DoubleWritable(1.0));
			}
		} else {
			long sum = 0;
			for (@SuppressWarnings("unused")
			DoubleWritable message : messages) {
				sum++;
			}
			LongWritable vertexValue = vertex.getValue();
			vertexValue.set(sum);
			vertex.setValue(vertexValue);
			vertex.voteToHalt();
		}
	}
}
