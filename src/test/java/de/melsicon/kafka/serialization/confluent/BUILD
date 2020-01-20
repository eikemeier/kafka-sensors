load("@rules_java//java:defs.bzl", "java_library", "java_test")

java_library(
    name = "tests",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = [
        "//src/main/java/de/melsicon/annotation",
        "//src/main/java/de/melsicon/kafka/model",
        "//src/main/java/de/melsicon/kafka/serde",
        "//src/main/java/de/melsicon/kafka/serde/avro",
        "//src/main/java/de/melsicon/kafka/serde/reflect",
        "//src/test/java/de/melsicon/kafka/testutil",
        "//toolchain:nullaway",
        "@maven//:junit_junit",
        "@maven//:org_apache_kafka_kafka_clients",
        "@maven//:org_assertj_assertj_core",
    ],
)

java_test(
    name = "SerializationTest",
    size = "small",
    runtime_deps = [
        ":tests",
    ],
)