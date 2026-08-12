/*
 * Copyright 2026 Databricks Doctor contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dbdoctor.core.model;

/**
 * dbdoctor's own representation of a Databricks SQL warehouse, decoupled from the SDK's model.
 *
 * @param id               warehouse ID
 * @param name             warehouse name
 * @param state             current warehouse state, e.g. RUNNING, STOPPED (as reported by the SDK)
 * @param clusterSize      warehouse cluster size, e.g. "2X-Small"
 * @param numClusters      number of clusters (for multi-cluster load balancing)
 * @param autoStopMinutes  configured auto-stop timeout; {@code 0} or {@code null} means disabled
 */
public record WarehouseInfo(
        String id,
        String name,
        String state,
        String clusterSize,
        Long numClusters,
        Long autoStopMinutes
) {
}
