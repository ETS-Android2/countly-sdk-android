package ly.count.android.sdk;

import androidx.annotation.NonNull;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

class MigrationHelper {
    /**
     * 0 - legacy version. State of the SDK before the first migration was introduced
     * 1 - adding device ID to all requests
     */
    final int DATA_SCHEMA_VERSIONS = 1;


    static final public String key_from_0_to_1_custom_id_set = "0_1_custom_id_set";

    StorageProvider storage;
    ModuleLog L;

    public MigrationHelper(StorageProvider storage, ModuleLog moduleLog) {
        this.storage = storage;
        L = moduleLog;
        L.v("[MigrationHelper] Initialising");
    }

    /**
     * Called from SDK side to perform the required steps to check if the migration is required and then execute it if it is.
     */
    public void doWork(@NonNull Map<String, Object> migrationParams) {
        int currentVersion = getCurrentSchemaVersion();
        L.v("[MigrationHelper] doWork, current version:[" + currentVersion + "]");

        if (currentVersion < 0) {
            L.e("[MigrationHelper] doWork, returned schema version is negative, encountered serious issue");
            return;
        }

        while (currentVersion < DATA_SCHEMA_VERSIONS) {
            performMigrationStep(currentVersion, migrationParams);

            currentVersion = getCurrentSchemaVersion();
        }
    }

    /**
     * Return the current schema version.
     * If no schema version is stored, the initial version will be acquired
     *
     * @return
     */
    int getCurrentSchemaVersion() {
        int currentVersion = storage.getDataSchemaVersion();

        if (currentVersion == -1) {
            //no schema version set
            setInitialSchemaVersion();
            currentVersion = storage.getDataSchemaVersion();
        }

        return currentVersion;
    }

    /**
     * Perform migration from the provided version to the next one
     *
     * @param currentVersion
     */
    void performMigrationStep(int currentVersion, @NonNull Map<String, Object> migrationParams) {
        int newVersion = currentVersion;

        switch (currentVersion) {
            case 0:
                L.w("[MigrationHelper] performMigrationStep, performing migration from version [0] -> [1]");
                performMigration0To1(migrationParams);
                newVersion = newVersion + 1;
                break;
            case DATA_SCHEMA_VERSIONS:
                L.w("[MigrationHelper] performMigrationStep, attempting to perform migration while already having the latest schema version, skipping [" + currentVersion + "]");
                break;
            default:
                L.w("[MigrationHelper] performMigrationStep, migration is performed out of the currently expected bounds, skipping [" + currentVersion + "]");
                break;
        }

        //assuming that the required migration steps are performed, increasing current schema version
        if (newVersion != currentVersion) {
            storage.setDataSchemaVersion(newVersion);
        }
    }

    /**
     * Set the current schema version the first time this code is executed
     *
     * If nothing is in storage then we can assume that this is the first run and no migration required.
     * In that case set the current version to the latest available one
     *
     * If something is in storage, assume that the SDK had been run before and migration is required.
     */
    void setInitialSchemaVersion() {
        if (storage.anythingSetInStorage()) {
            //we are on a legacy version
            storage.setDataSchemaVersion(0);
            return;
        }

        //no data means new install, apply the latest schema version
        storage.setDataSchemaVersion(DATA_SCHEMA_VERSIONS);
    }

    /**
     * Specific migration from schema version 0 to 1
     */
    void performMigration0To1(@NonNull Map<String, Object> migrationParams) {
        String deviceIDType = storage.getDeviceIDType();
        String deviceID = storage.getDeviceID();

        if(deviceIDType == null && deviceID == null) {
            //if both the ID and type are null we are in big trouble
            //set type to OPEN_UDID and generate the ID afterwards
            storage.setDeviceIDType(DeviceIdType.OPEN_UDID.toString());
            deviceIDType = DeviceIdType.OPEN_UDID.toString();
        } else if(deviceIDType == null) {
            //if the type is null, but the ID value is not null, we have to guess the type
            Boolean customIdProvided = (Boolean) migrationParams.get(key_from_0_to_1_custom_id_set);
            if(customIdProvided == null) {
                customIdProvided = false;
            }

            if(customIdProvided){
                //if a custom device ID is provided during init, assume that the previous type was dev supplied
                storage.setDeviceIDType(DeviceIdType.DEVELOPER_SUPPLIED.toString());
                deviceIDType = DeviceIdType.DEVELOPER_SUPPLIED.toString();

            } else {
                //if a custom device ID was not provided during init, assume that the previous type was SDK generated
                storage.setDeviceIDType(DeviceIdType.OPEN_UDID.toString());
                deviceIDType = DeviceIdType.OPEN_UDID.toString();
            }
        }

        //update the device ID type
        //noinspection StatementWithEmptyBody
        if (deviceIDType.equals(DeviceIdType.OPEN_UDID.toString())) {
            //current device ID is OPEN_UDID
            //nothing should change
        } else if (deviceIDType.equals(DeviceIdType.ADVERTISING_ID.toString())) {
            //current device ID is ADVERTISING_ID
            //it's type should be changed to OPEN_UDID.
            storage.setDeviceIDType(DeviceIdType.OPEN_UDID.toString());
            deviceIDType = DeviceIdType.OPEN_UDID.toString();
        }

        //generate a deviceID in case the current type is OPEN_UDID (either migrated or originally as such) and there is no ID
        if(deviceIDType.equals(DeviceIdType.OPEN_UDID.toString())) {
            if(deviceID == null || deviceID.isEmpty()) {
                //in case there is no valid ID, generate it
                storage.setDeviceID(UUID.randomUUID().toString());
            }
        }
    }
}
