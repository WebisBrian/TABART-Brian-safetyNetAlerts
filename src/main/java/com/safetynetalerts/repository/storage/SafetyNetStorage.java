package com.safetynetalerts.repository.storage;

import com.safetynetalerts.model.SafetyNetData;

/**
 * Storage layer (persistence) responsible only for loading/saving SafetyNetData.
 * Logic only for file I/O.
 */
public interface SafetyNetStorage {

    /**
     * Loads SafetyNetData from the configured writable JSON file.
     * If the writable file is missing (or empty), it must be initialized from classpath data.json.
     */
    SafetyNetData load();

    /**
     * Saves SafetyNetData to the configured writable JSON file (atomic write if possible).
     */
    void save(SafetyNetData data);
}
