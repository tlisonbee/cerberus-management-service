/*
 * Copyright (c) 2017 Nike, Inc.
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

package com.nike.cerberus.record;

public class SecureDataRecord {

    private Integer id;
    private String sdboxId;
    private String path;
    private String encryptedBlob;
    private Integer topLevelKVCount;

    public Integer getId() {
        return id;
    }

    public SecureDataRecord setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getSdboxId() {
        return sdboxId;
    }

    public SecureDataRecord setSdboxId(String sdboxId) {
        this.sdboxId = sdboxId;
        return this;
    }

    public String getPath() {
        return path;
    }

    public SecureDataRecord setPath(String path) {
        this.path = path;
        return this;
    }

    public String getEncryptedBlob() {
        return encryptedBlob;
    }

    public SecureDataRecord setEncryptedBlob(String encryptedBlob) {
        this.encryptedBlob = encryptedBlob;
        return this;
    }

    public Integer getTopLevelKVCount() {
        return topLevelKVCount;
    }

    public SecureDataRecord setTopLevelKVCount(Integer topLevelKVCount) {
        this.topLevelKVCount = topLevelKVCount;
        return this;
    }
}
