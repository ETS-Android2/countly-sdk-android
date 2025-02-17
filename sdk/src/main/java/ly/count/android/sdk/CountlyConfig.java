package ly.count.android.sdk;

import android.app.Application;
import android.content.Context;
import java.util.Map;

public class CountlyConfig {

    /**
     * Internal fields and fields for testing
     */
    protected CountlyStore countlyStore = null;

    /**
     * Used to pass the consent provider to all modules and features
     */
    protected ConsentProvider consentProvider = null;

    /**
     * Used to pass the storage provider to all modules and features
     */
    protected StorageProvider storageProvider = null;

    protected EventProvider eventProvider = null;

    protected EventQueueProvider eventQueueProvider = null;

    protected RequestQueueProvider requestQueueProvider = null;

    protected DeviceIdProvider deviceIdProvider = null;

    protected BaseInfoProvider baseInfoProvider = null;

    protected ModuleBase testModuleListener = null;

    protected boolean checkForNativeCrashDumps = true;

    //used to deliver this object to connection queue
    //protected DeviceId deviceIdInstance = null;

    // Fields used for SDK configuration during init

    /**
     * Android context.
     * Mandatory field.
     */
    protected Context context = null;

    /**
     * URL of the Countly server to submit data to.
     * Mandatory field.
     */
    protected String serverURL = null;

    /**
     * app key for the application being tracked; find in the Countly Dashboard under Management &gt; Applications.
     * Mandatory field.
     */
    protected String appKey = null;

    /**
     * unique ID for the device the app is running on; note that null in deviceID means that Countly will fall back to OpenUDID, then, if it's not available, to Google Advertising ID.
     */
    protected String deviceID = null;

    /**
     * enum value specifying which device ID generation strategy Countly should use: OpenUDID or Google Advertising ID.
     */
    protected DeviceIdType idMode = null;

    /**
     * sets the limit after how many sessions, for each apps version, the automatic star rating dialog is shown.
     */
    protected int starRatingSessionLimit = 5;

    /**
     * the callback function that will be called from the automatic star rating dialog.
     */
    protected StarRatingCallback starRatingCallback = null;

    /**
     * the shown title text for the star rating dialogs.
     */
    protected String starRatingTextTitle = null;

    /**
     * the shown message text for the star rating dialogs.
     */
    protected String starRatingTextMessage = null;

    /**
     * the shown dismiss button text for the shown star rating dialogs.
     */
    protected String starRatingTextDismiss = null;

    protected boolean loggingEnabled = false;

    protected boolean enableUnhandledCrashReporting = false;

    protected boolean enableViewTracking = false;

    protected boolean autoTrackingUseShortName = false;

    protected Class[] autoTrackingExceptions = null;

    protected Map<String, Object> automaticViewSegmentation = null;

    protected Map<String, String> customNetworkRequestHeaders = null;

    protected boolean pushIntentAddMetadata = false;

    protected boolean enableRemoteConfigAutomaticDownload = false;
    protected RemoteConfig.RemoteConfigCallback remoteConfigCallbackOld = null;
    protected RemoteConfigCallback remoteConfigCallbackNew = null;

    protected boolean shouldRequireConsent = false;
    protected String[] enabledFeatureNames = null;

    protected boolean httpPostForced = false;

    protected boolean temporaryDeviceIdEnabled = false;

    protected String tamperingProtectionSalt = null;

    protected Integer eventQueueSizeThreshold = null;

    protected boolean trackOrientationChange = true;

    protected boolean manualSessionControlEnabled = false;

    protected boolean recordAllThreadsWithCrash = false;

    protected boolean disableUpdateSessionRequests = false;

    protected boolean shouldIgnoreAppCrawlers = false;

    protected String[] appCrawlerNames = null;

    protected String[] publicKeyPinningCertificates = null;

    protected String[] certificatePinningCertificates = null;

    protected Boolean enableAttribution = null;

    protected Map<String, Object> customCrashSegment = null;

    protected Integer sessionUpdateTimerDelay = null;

    protected CrashFilterCallback crashFilterCallback;

    protected boolean starRatingDialogIsCancellable = false;

    protected boolean starRatingShownAutomatically = false;

    protected boolean starRatingDisableAskingForEachAppVersion = false;

    protected Application application = null;

    protected boolean recordAppStartTime = false;

    boolean disableLocation = false;

    String locationCountyCode = null;

    String locationCity = null;

    String locationLocation = null;

    String locationIpAddress = null;

    Map<String, String> metricOverride = null;

    Long appStartTimestampOverride = null;

    boolean appLoadedManualTrigger = false;

    boolean manualForegroundBackgroundTrigger = false;

    int maxRequestQueueSize = 1000;

    ModuleLog.LogCallback providedLogCallback;

    String daCampaignType = null;
    String daCampaignData = null;
    Map<String, String> iaAttributionValues = null;

    //SDK internal limits
    Integer maxKeyLength;
    Integer maxValueSize;
    Integer maxSegmentationValues;
    Integer maxBreadcrumbCount;
    Integer maxStackTraceLinesPerThread;
    Integer maxStackTraceLineLength;
    int maxStackTraceThreadCount = 30;

    //public synchronized CountlyConfig setMaxKeyLength(int maxKeyLength) {
    //    this.maxKeyLength = maxKeyLength;
    //    return this;
    //}

    /**
     * Set the maximum value size for values used internally. This affects things like: segmentation values
     * user property values, breadcrumb text.
     * If those values exceed the set limit, they will be truncated.
     * @param maxValueSize
     * @return
     */
    //public synchronized CountlyConfig setMaxValueSize(int maxValueSize) {
    //    this.maxValueSize = maxValueSize;
    //    return this;
    //}

    /**
     * Sets how many segmentation values can be recorded when recording an event or view.
     * Values exceeding this count will be ignored.
     * @param maxSegmentationValues
     * @return
     */
    public synchronized CountlyConfig setMaxSegmentationValues(int maxSegmentationValues) {
        this.maxSegmentationValues = maxSegmentationValues;
        return this;
    }

    /**
     * Set the maximum amount of breadcrumbs that can be recorded.
     * After exceeding the limit, the oldest values will be removed.
     * @param maxBreadcrumbCount
     * @return
     */
    public synchronized CountlyConfig setMaxBreadcrumbCount(int maxBreadcrumbCount) {
        this.maxBreadcrumbCount = maxBreadcrumbCount;
        return this;
    }

    //public synchronized CountlyConfig setMaxStackTraceLinesPerThread(int maxStackTraceLinesPerThread) {
    //    this.maxStackTraceLinesPerThread = maxStackTraceLinesPerThread;
    //    return this;
    //}
    //
    //public synchronized CountlyConfig setMaxStackTraceLineLength(int maxStackTraceLineLength) {
    //    this.maxStackTraceLineLength = maxStackTraceLineLength;
    //    return this;
    //}

    public CountlyConfig() {
    }

    /**
     * @param context
     * @param appKey
     * @param serverURL
     * @deprecated Please use the constructor that takes the application class
     */
    public CountlyConfig(Context context, String appKey, String serverURL) {
        setContext(context);
        setAppKey(appKey);
        setServerURL(serverURL);
    }

    public CountlyConfig(Application application, String appKey, String serverURL) {
        setAppKey(appKey);
        setServerURL(serverURL);
        setApplication(application);
    }

    /**
     * Android context.
     * Mandatory field.
     *
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setContext(Context context) {
        this.context = context;
        return this;
    }

    /**
     * URL of the Countly server to submit data to.
     * Mandatory field.
     *
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setServerURL(String serverURL) {
        this.serverURL = serverURL;
        return this;
    }

    /**
     * app key for the application being tracked; find in the Countly Dashboard under Management &gt; Applications.
     * Mandatory field.
     *
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    /**
     * unique ID for the device the app is running on; note that null in deviceID means that Countly will fall back to OpenUDID, then, if it's not available, to Google Advertising ID.
     *
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setDeviceId(String deviceID) {
        this.deviceID = deviceID;
        return this;
    }

    /**
     * enum value specifying which device ID generation strategy Countly should use: OpenUDID or Google Advertising ID.
     *
     * @return Returns the same config object for convenient linking
     * @deprecated use this call with the other type override. The new type has the same values so a simple substitution is enough
     */
    public synchronized CountlyConfig setIdMode(DeviceId.Type idMode) {
        this.idMode = ModuleDeviceId.fromOldDeviceIdToNew(idMode);
        return this;
    }

    /**
     * enum value specifying which device ID generation strategy Countly should use: OpenUDID or Google Advertising ID.
     *
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setIdMode(DeviceIdType idMode) {
        this.idMode = idMode;
        return this;
    }

    /**
     * sets the limit after how many sessions, for each apps version, the automatic star rating dialog is shown.
     *
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setStarRatingSessionLimit(int starRatingLimit) {
        this.starRatingSessionLimit = starRatingLimit;
        return this;
    }

    /**
     * the callback function that will be called from the automatic star rating dialog.
     *
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setStarRatingCallback(StarRatingCallback starRatingCallback) {
        this.starRatingCallback = starRatingCallback;
        return this;
    }

    /**
     * the shown title text for the star rating dialogs.
     *
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setStarRatingTextTitle(String starRatingTextTitle) {
        this.starRatingTextTitle = starRatingTextTitle;
        return this;
    }

    /**
     * the shown message text for the star rating dialogs.
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setStarRatingTextMessage(String starRatingTextMessage) {
        this.starRatingTextMessage = starRatingTextMessage;
        return this;
    }

    /**
     * the shown dismiss button text for the shown star rating dialogs.
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setStarRatingTextDismiss(String starRatingTextDismiss) {
        this.starRatingTextDismiss = starRatingTextDismiss;
        return this;
    }

    /**
     * Set to true of you want to enable countly internal debugging logs
     * Those logs will be printed to the console
     *
     * @param enabled Set to true of you want to enable countly internal debugging logs
     */
    public synchronized CountlyConfig setLoggingEnabled(boolean enabled) {
        this.loggingEnabled = enabled;
        return this;
    }

    /**
     * Call to enable uncaught crash reporting
     *
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig enableCrashReporting() {
        this.enableUnhandledCrashReporting = true;
        return this;
    }

    /**
     * Set if automatic view tracking should be enabled
     *
     * @param enable
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setViewTracking(boolean enable) {
        this.enableViewTracking = enable;
        return this;
    }

    /**
     * Set if automatic activity tracking should use short names
     *
     * @param enable set true if you want short names
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setAutoTrackingUseShortName(boolean enable) {
        this.autoTrackingUseShortName = enable;
        return this;
    }

    /**
     *
     * @param segmentation
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setAutomaticViewSegmentation(Map<String, Object> segmentation) {
        automaticViewSegmentation = segmentation;
        return this;
    }

    /**
     * Set which activities should be excluded from automatic view tracking
     *
     * @param exceptions activities which should be ignored
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setAutoTrackingExceptions(Class[] exceptions) {
        if (exceptions != null) {
            for (Class exception : exceptions) {
                if (exception == null) {
                    throw new IllegalArgumentException("setAutoTrackingExceptions() does not accept 'null' activities");
                }
            }
        }

        autoTrackingExceptions = exceptions;
        return this;
    }

    /**
     * Allows you to add custom header key/value pairs to each request
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig addCustomNetworkRequestHeaders(Map<String, String> customHeaderValues) {
        this.customNetworkRequestHeaders = customHeaderValues;
        return this;
    }

    /**
     *
     * @param enable
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setPushIntentAddMetadata(boolean enable) {
        pushIntentAddMetadata = enable;
        return this;
    }

    /**
     * If enable, will automatically download newest remote config values.
     *
     * @param enabled set true for enabling it
     * @param callback callback called after the update was done
     * @return Returns the same config object for convenient linking
     * @deprecated use the other version of this call that uses a different callback
     */
    public synchronized CountlyConfig setRemoteConfigAutomaticDownload(boolean enabled, RemoteConfig.RemoteConfigCallback callback) {
        enableRemoteConfigAutomaticDownload = enabled;
        remoteConfigCallbackOld = callback;
        return this;
    }

    /**
     * If enable, will automatically download newest remote config values.
     *
     * @param enabled set true for enabling it
     * @param callback callback called after the update was done
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setRemoteConfigAutomaticDownload(boolean enabled, RemoteConfigCallback callback) {
        enableRemoteConfigAutomaticDownload = enabled;
        remoteConfigCallbackNew = callback;
        return this;
    }

    /**
     * Set if consent should be required
     *
     * @param shouldRequireConsent
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setRequiresConsent(boolean shouldRequireConsent) {
        this.shouldRequireConsent = shouldRequireConsent;
        return this;
    }

    /**
     * Sets which features are enabled in case consent is required
     *
     * @param featureNames
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setConsentEnabled(String[] featureNames) {
        enabledFeatureNames = featureNames;
        return this;
    }

    /**
     * Set the override for forcing to use HTTP POST for all connections to the server
     *
     * @param isForced the flag for the new status, set "true" if you want it to be forced
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setHttpPostForced(boolean isForced) {
        httpPostForced = isForced;
        return this;
    }

    /**
     *
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig enableTemporaryDeviceIdMode() {
        temporaryDeviceIdEnabled = true;
        return this;
    }

    /**
     *
     * @param callback
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setCrashFilterCallback(CrashFilterCallback callback) {
        crashFilterCallback = callback;
        return this;
    }

    /**
     *
     * @param salt
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setParameterTamperingProtectionSalt(String salt) {
        tamperingProtectionSalt = salt;
        return this;
    }

    /**
     *
     * @param shouldTrackOrientation
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setTrackOrientationChanges(boolean shouldTrackOrientation) {
        trackOrientationChange = shouldTrackOrientation;
        return this;
    }

    /**
     *
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setRecordAllThreadsWithCrash() {
        recordAllThreadsWithCrash = true;
        return this;
    }

    /**
     * Set if attribution should be enabled
     *
     * @param enableAttribution set true if you want to enable it, set false if you want to disable it
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setEnableAttribution(boolean enableAttribution) {
        this.enableAttribution = enableAttribution;
        return this;
    }

    /**
     * Allows public key pinning.
     * Supply list of SSL certificates (base64-encoded strings between "-----BEGIN CERTIFICATE-----" and "-----END CERTIFICATE-----" without end-of-line)
     * along with server URL starting with "https://". Countly will only accept connections to the server
     * if public key of SSL certificate provided by the server matches one provided to this method.
     *
     * @param certificates List of SSL public keys
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig enablePublicKeyPinning(String[] certificates) {
        publicKeyPinningCertificates = certificates;
        return this;
    }

    /**
     * Allows certificate pinning.
     * Supply list of SSL certificates (base64-encoded strings between "-----BEGIN CERTIFICATE-----" and "-----END CERTIFICATE-----" without end-of-line)
     * along with server URL starting with "https://". Countly will only accept connections to the server
     * if certificate provided by the server matches one provided to this method.
     *
     * @param certificates List of SSL certificates
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig enableCertificatePinning(String[] certificates) {
        certificatePinningCertificates = certificates;
        return this;
    }

    /**
     * Set if Countly SDK should ignore app crawlers
     *
     * @param shouldIgnore if crawlers should be ignored
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setShouldIgnoreAppCrawlers(boolean shouldIgnore) {
        shouldIgnoreAppCrawlers = shouldIgnore;
        return this;
    }

    /**
     * List of app crawler names that should be ignored
     *
     * @param appCrawlerNames the names to be ignored
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setAppCrawlerNames(String[] appCrawlerNames) {
        this.appCrawlerNames = appCrawlerNames;
        return this;
    }

    /**
     * Set the threshold for event grouping. Event count that is bellow the
     * threshold will be sent on update ticks.
     *
     * @param threshold
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setEventQueueSizeToSend(int threshold) {
        eventQueueSizeThreshold = threshold;
        return this;
    }

    public synchronized CountlyConfig enableManualSessionControl() {
        manualSessionControlEnabled = true;
        return this;
    }

    /**
     * Set custom crash segmentation which will be added to all recorded crashes
     *
     * @param crashSegment segmentation information. Accepted values are "Integer", "String", "Double", "Boolean"
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setCustomCrashSegment(Map<String, Object> crashSegment) {
        customCrashSegment = crashSegment;
        return this;
    }

    /**
     * For use during testing
     * @param checkForDumps
     * @return Returns the same config object for convenient linking
     */
    protected synchronized CountlyConfig checkForNativeCrashDumps(boolean checkForDumps) {
        checkForNativeCrashDumps = checkForDumps;
        return this;
    }

    /**
     * Sets the interval for the automatic session update calls
     * min value 1 (1 second),
     * max value 600 (10 minutes)
     *
     * @param delay in seconds
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setUpdateSessionTimerDelay(int delay) {
        sessionUpdateTimerDelay = delay;
        return this;
    }

    /**
     * For use during testing
     * @param store
     * @return Returns the same config object for convenient linking
     */
    protected synchronized CountlyConfig setCountlyStore(CountlyStore store) {
        countlyStore = store;
        return this;
    }

    /**
     * Disable periodic session time updates.
     * By default, Countly will send a request to the server each 60 seconds with a small update
     * containing session duration time. This method allows you to disable such behavior.
     * Note that event updates will still be sent every 100 events or 60 seconds after event recording.
     *
     * @param disable whether or not to disable session time updates
     * @return Returns the same config object for convenient linking
     */
    protected synchronized CountlyConfig setDisableUpdateSessionRequests(boolean disable) {
        disableUpdateSessionRequests = disable;
        return this;
    }

    /**
     * Set if the star rating dialog is cancellable
     *
     * @param isCancellable set this true if it should be cancellable
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setIfStarRatingDialogIsCancellable(boolean isCancellable) {
        starRatingDialogIsCancellable = isCancellable;
        return this;
    }

    /**
     * Set if the star rating should be shown automatically
     *
     * @param isShownAutomatically set it true if you want to show the app star rating dialog automatically for each new version after the specified session amount
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setIfStarRatingShownAutomatically(boolean isShownAutomatically) {
        starRatingShownAutomatically = isShownAutomatically;
        return this;
    }

    /**
     * Set if the star rating is shown only once per app lifetime
     *
     * @param disableAsking set true if you want to disable asking the app rating for each new app version (show it only once per apps lifetime)
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setStarRatingDisableAskingForEachAppVersion(boolean disableAsking) {
        starRatingDisableAskingForEachAppVersion = disableAsking;
        return this;
    }

    /**
     * Set the link to the application class
     *
     * @param application
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setApplication(Application application) {
        this.application = application;
        return this;
    }

    /**
     * Enable the recording of the app start time
     *
     * @param recordAppStartTime
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setRecordAppStartTime(boolean recordAppStartTime) {
        this.recordAppStartTime = recordAppStartTime;
        return this;
    }

    /**
     * Disable location tracking
     *
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setDisableLocation() {
        disableLocation = true;
        return this;
    }

    /**
     * Set location parameters.
     * This will be ignored if set together with `setDisableLocation`
     *
     * @param country_code ISO Country code for the user's country
     * @param city Name of the user's city
     * @param gpsCoordinates comma separate lat and lng values. For example, "56.42345,123.45325"
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setLocation(String country_code, String city, String gpsCoordinates, String ipAddress) {
        locationCountyCode = country_code;
        locationCity = city;
        locationLocation = gpsCoordinates;
        locationIpAddress = ipAddress;
        return this;
    }

    /**
     * Set the metrics you want to override or additional custom metrics you want to provide
     *
     * @param providedMetricOverride
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setMetricOverride(Map<String, String> providedMetricOverride) {
        metricOverride = providedMetricOverride;
        return this;
    }

    /**
     * Override the app start timestamp in case you have a more precise way to measure it
     *
     * @param appStartTimestampOverride
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setAppStartTimestampOverride(long appStartTimestampOverride) {
        this.appStartTimestampOverride = appStartTimestampOverride;
        return this;
    }

    /**
     * Set to manually trigger the moment when the app has finished loading
     *
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig enableManualAppLoadedTrigger() {
        appLoadedManualTrigger = true;
        return this;
    }

    /**
     * Set this in case you want to control these triggers manually
     *
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig enableManualForegroundBackgroundTriggerAPM() {
        manualForegroundBackgroundTrigger = true;
        return this;
    }

    /**
     * Add a log callback that will duplicate all logs done by the SDK.
     * For each message you will receive the message string and it's targeted log level.
     *
     * @param logCallback
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setLogListener(ModuleLog.LogCallback logCallback) {
        providedLogCallback = logCallback;
        return this;
    }

    /**
     * Set's the new maximum size for the request queue.
     * @param newMaxSize Minimum value is "1".
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setMaxRequestQueueSize(int newMaxSize) {
        maxRequestQueueSize = newMaxSize;
        return this;
    }

    /**
     * Report direct user attribution
     *
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setDirectAttribution(String campaignType, String campaignData) {
        daCampaignType = campaignType;
        daCampaignData = campaignData;
        return this;
    }

    /**
     * Report indirect user attribution
     *
     * @return Returns the same config object for convenient linking
     */
    public synchronized CountlyConfig setIndirectAttribution(Map<String, String> attributionValues) {
        iaAttributionValues = attributionValues;
        return this;
    }
}
