-keepparameternames
-repackageclasses thanos.core

-keep class org.thanos.core.bean.*{
    public *;
}
-keep class org.thanos.core.MorningDataAPI{
    public *;
}
-keep class org.thanos.core.StatisticsUtil{
    public *;
}
-keep class org.thanos.core.ThanosCloudConstants{
    public *;
}
-keep class org.thanos.core.MorningDataAPI$*{
    public *;
}
-keep class org.thanos.core.push.ThanosPushConfig{
    public *;
}
-keep class org.thanos.core.internal.MorningDataCore{
    public *;
}
-keep class org.thanos.core.utils.ThanosDataCorePrefUtils{
    public *;
}