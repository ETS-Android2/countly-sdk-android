package ly.count.android.sdk;

import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ModuleRemoteConfig extends ModuleBase {
    boolean updateRemoteConfigAfterIdChange = false;

    RemoteConfig remoteConfigInterface = null;

    ModuleRemoteConfig(Countly cly, CountlyConfig config) {
        super(cly);

        if (_cly.isLoggingEnabled()) {
            Log.v(Countly.TAG, "[ModuleRemoteConfig] Initialising");
        }

        remoteConfigInterface = new RemoteConfig();
    }

    /**
     * Internal call for updating remote config keys
     *
     * @param keysOnly set if these are the only keys to update
     * @param keysExcept set if these keys should be ignored from the update
     * @param requestShouldBeDelayed this is set to true in case of update after a deviceId change
     * @param callback called after the update is done
     */
    void updateRemoteConfigValues(final String[] keysOnly, final String[] keysExcept, final ConnectionQueue connectionQueue_, final boolean requestShouldBeDelayed, final RemoteConfigCallback callback) {
        if (Countly.sharedInstance().isLoggingEnabled()) {
            Log.d(Countly.TAG, "[ModuleRemoteConfig] Updating remote config values, requestShouldBeDelayed:[" + requestShouldBeDelayed + "]");
        }
        String keysInclude = null;
        String keysExclude = null;

        if (keysOnly != null && keysOnly.length > 0) {
            //include list takes precedence
            //if there is at least one item, use it
            JSONArray includeArray = new JSONArray();
            for (String key : keysOnly) {
                includeArray.put(key);
            }
            keysInclude = includeArray.toString();
        } else if (keysExcept != null && keysExcept.length > 0) {
            //include list was not used, use the exclude list
            JSONArray excludeArray = new JSONArray();
            for (String key : keysExcept) {
                excludeArray.put(key);
            }
            keysExclude = excludeArray.toString();
        }

        if (connectionQueue_.getDeviceId().getId() == null) {
            //device ID is null, abort
            if (Countly.sharedInstance().isLoggingEnabled()) {
                Log.d(Countly.TAG, "[ModuleRemoteConfig] RemoteConfig value update was aborted, deviceID is null");
            }

            if (callback != null) {
                callback.callback("Can't complete call, device ID is null");
            }

            return;
        }

        if (connectionQueue_.getDeviceId().temporaryIdModeEnabled() || connectionQueue_.queueContainsTemporaryIdItems()) {
            //temporary id mode enabled, abort
            if (Countly.sharedInstance().isLoggingEnabled()) {
                Log.d(Countly.TAG, "[ModuleRemoteConfig] RemoteConfig value update was aborted, temporary device ID mode is set");
            }

            if (callback != null) {
                callback.callback("Can't complete call, temporary device ID is set");
            }

            return;
        }

        String requestData = connectionQueue_.prepareRemoteConfigRequest(keysInclude, keysExclude);
        if (Countly.sharedInstance().isLoggingEnabled()) {
            Log.d(Countly.TAG, "[ModuleRemoteConfig] RemoteConfig requestData:[" + requestData + "]");
        }

        ConnectionProcessor cp = connectionQueue_.createConnectionProcessor();

        (new ImmediateRequestMaker()).execute(requestData, "/o/sdk", cp, requestShouldBeDelayed, new ImmediateRequestMaker.InternalFeedbackRatingCallback() {
            @Override
            public void callback(JSONObject checkResponse) {
                if (Countly.sharedInstance().isLoggingEnabled()) {
                    Log.d(Countly.TAG, "[ModuleRemoteConfig] Processing remote config received response, received response is null:[" + (checkResponse == null) + "]");
                }
                if (checkResponse == null) {
                    if (callback != null) {
                        callback.callback("Encountered problem while trying to reach the server, possibly no internet connection");
                    }
                    return;
                }

                //merge the new values into the current ones
                RemoteConfigValueStore rcvs = loadConfig();
                if (keysExcept == null && keysOnly == null) {
                    //in case of full updates, clear old values
                    rcvs.values = new JSONObject();
                }
                rcvs.mergeValues(checkResponse);

                if (Countly.sharedInstance().isLoggingEnabled()) {
                    Log.d(Countly.TAG, "[ModuleRemoteConfig] Finished remote config processing, starting saving");
                }

                saveConfig(rcvs);

                if (Countly.sharedInstance().isLoggingEnabled()) {
                    Log.d(Countly.TAG, "[ModuleRemoteConfig] Finished remote config saving");
                }

                if (callback != null) {
                    callback.callback(null);
                }
            }
        });
    }

    Object getValue(String key) {
        RemoteConfigValueStore rcvs = loadConfig();
        return rcvs.getValue(key);
    }

    void saveConfig(RemoteConfigValueStore rcvs) {
        CountlyStore cs = new CountlyStore(_cly.context_);
        cs.setRemoteConfigValues(rcvs.dataToString());
    }

    RemoteConfigValueStore loadConfig() {
        CountlyStore cs = new CountlyStore(_cly.context_);
        String rcvsString = cs.getRemoteConfigValues();
        //noinspection UnnecessaryLocalVariable
        RemoteConfigValueStore rcvs = RemoteConfigValueStore.dataFromString(rcvsString);
        return rcvs;
    }

    void clearValueStore() {
        CountlyStore cs = new CountlyStore(_cly.context_);
        cs.setRemoteConfigValues("");
    }

    Map<String, Object> getAllRemoteConfigValuesInternal() {
        RemoteConfigValueStore rcvs = loadConfig();
        return rcvs.getAllValues();
    }

    static class RemoteConfigValueStore {
        public JSONObject values = new JSONObject();

        //add new values to the current storage
        public void mergeValues(JSONObject newValues) {
            if (newValues == null) {
                return;
            }

            Iterator<String> iter = newValues.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object value = newValues.get(key);
                    values.put(key, value);
                } catch (Exception e) {
                    if (Countly.sharedInstance().isLoggingEnabled()) {
                        Log.e(Countly.TAG, "[RemoteConfigValueStore] Failed merging new remote config values");
                    }
                }
            }
        }

        private RemoteConfigValueStore(JSONObject values) {
            this.values = values;
        }

        public Object getValue(String key) {
            return values.opt(key);
        }

        public Map<String, Object> getAllValues() {
            Map<String, Object> ret = new HashMap<>();

            Iterator<String> keys = values.keys();

            while (keys.hasNext()) {
                String key = keys.next();

                try {
                    ret.put(key, values.get(key));
                } catch (Exception ex) {
                    if (Countly.sharedInstance().isLoggingEnabled()) {
                        Log.e(Countly.TAG, "[RemoteConfigValueStore] Got JSON exception while calling 'getAllValues': " + ex.toString());
                    }
                }
            }

            return ret;
        }

        public static RemoteConfigValueStore dataFromString(String storageString) {
            if (storageString == null || storageString.isEmpty()) {
                return new RemoteConfigValueStore(new JSONObject());
            }

            JSONObject values;
            try {
                values = new JSONObject(storageString);
            } catch (JSONException e) {
                if (Countly.sharedInstance().isLoggingEnabled()) {
                    Log.e(Countly.TAG, "[RemoteConfigValueStore] Couldn't decode RemoteConfigValueStore successfully: " + e.toString());
                }
                values = new JSONObject();
            }
            return new RemoteConfigValueStore(values);
        }

        public String dataToString() {
            return values.toString();
        }
    }

    void clearAndDownloadAfterIdChange() {
        if (Countly.sharedInstance().isLoggingEnabled()) {
            Log.v(Countly.TAG, "[RemoteConfig] Clearing remote config values and preparing to download after ID update");
        }

        _cly.remoteConfig().clearStoredValues();
        if (_cly.remoteConfigAutomaticUpdateEnabled && _cly.getConsent(Countly.CountlyFeatureNames.remoteConfig)) {
            updateRemoteConfigAfterIdChange = true;
        }
    }

    @Override
    void deviceIdChanged() {
        if (Countly.sharedInstance().isLoggingEnabled()) {
            Log.v(Countly.TAG, "[RemoteConfig] Device ID changed will update values: [" + updateRemoteConfigAfterIdChange + "]");
        }

        if (updateRemoteConfigAfterIdChange) {
            updateRemoteConfigAfterIdChange = false;
            updateRemoteConfigValues(null, null, _cly.connectionQueue_, true, null);
        }
    }

    @Override
    public void halt() {
        remoteConfigInterface = null;
    }

    public class RemoteConfig {
        /**
         * Clear all stored remote config_ values
         */
        public void clearStoredValues() {
            synchronized (_cly) {
                if (_cly.isLoggingEnabled()) {
                    Log.i(Countly.TAG, "[RemoteConfig] Calling 'clearStoredValues'");
                }

                clearValueStore();
            }
        }

        public Map<String, Object> getAllValues() {
            synchronized (_cly) {
                if (_cly.isLoggingEnabled()) {
                    Log.i(Countly.TAG, "[RemoteConfig] Calling 'getAllValues'");
                }

                if (!_cly.getConsent(Countly.CountlyFeatureNames.remoteConfig)) {
                    return null;
                }

                return getAllRemoteConfigValuesInternal();
            }
        }

        /**
         * Get the stored value for the provided remote config_ key
         *
         * @param key
         * @return
         */
        public Object getValueForKey(String key) {
            synchronized (_cly) {
                if (_cly.isLoggingEnabled()) {
                    Log.i(Countly.TAG, "[RemoteConfig] Calling remoteConfigValueForKey, " + key);
                }

                if (!_cly.getConsent(Countly.CountlyFeatureNames.remoteConfig)) {
                    return null;
                }

                return getValue(key);
            }
        }

        /**
         * Manual remote config update call. Will update all keys except the ones provided
         *
         * @param keysToExclude
         * @param callback
         */
        public void updateExceptKeys(String[] keysToExclude, RemoteConfigCallback callback) {
            synchronized (_cly) {
                if (_cly.isLoggingEnabled()) {
                    Log.i(Countly.TAG, "[RemoteConfig] Manually calling to updateRemoteConfig with exclude keys");
                }

                if (!_cly.getConsent(Countly.CountlyFeatureNames.remoteConfig)) {
                    if (callback != null) {
                        callback.callback("No consent given");
                    }
                    return;
                }
                if (keysToExclude == null && _cly.isLoggingEnabled()) {
                    Log.w(Countly.TAG, "[RemoteConfig] updateRemoteConfigExceptKeys passed 'keys to ignore' array is null");
                }
                updateRemoteConfigValues(null, keysToExclude, _cly.connectionQueue_, false, callback);
            }
        }

        /**
         * Manual remote config_ update call. Will only update the keys provided.
         *
         * @param keysToInclude
         * @param callback
         */
        public void updateForKeysOnly(String[] keysToInclude, RemoteConfigCallback callback) {
            synchronized (_cly) {
                if (_cly.isLoggingEnabled()) {
                    Log.i(Countly.TAG, "[RemoteConfig] Manually calling to updateRemoteConfig with include keys");
                }
                if (!_cly.getConsent(Countly.CountlyFeatureNames.remoteConfig)) {
                    if (callback != null) {
                        callback.callback("No consent given");
                    }
                    return;
                }
                if (keysToInclude == null && _cly.isLoggingEnabled()) {
                    Log.w(Countly.TAG, "[RemoteConfig] updateRemoteConfigExceptKeys passed 'keys to include' array is null");
                }
                updateRemoteConfigValues(keysToInclude, null, _cly.connectionQueue_, false, callback);
            }
        }

        /**
         * Manually update remote config_ values
         *
         * @param callback
         */
        public void update(RemoteConfigCallback callback) {
            synchronized (_cly) {
                if (_cly.isLoggingEnabled()) {
                    Log.i(Countly.TAG, "[RemoteConfig] Manually calling to updateRemoteConfig");
                }

                if (!_cly.getConsent(Countly.CountlyFeatureNames.remoteConfig)) {
                    return;
                }

                updateRemoteConfigValues(null, null, _cly.connectionQueue_, false, callback);
            }
        }
    }
}
