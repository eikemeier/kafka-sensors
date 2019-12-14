load("@rules_java//java:defs.bzl", "java_library")

java_library(
    name = "kafka-schema-registry-client",
    srcs = glob(["client/src/main/java/**/*.java"]),
    resources = glob(["client/src/main/resources/**"]),
    visibility = ["//visibility:public"],
    deps = [
        "@confluent_common//:common-config",
        "@confluent_common//:common-utils",
        "@maven//:com_fasterxml_jackson_core_jackson_annotations",
        "@maven//:com_fasterxml_jackson_core_jackson_core",
        "@maven//:com_fasterxml_jackson_core_jackson_databind",
        "@maven//:com_google_guava_guava",
        "@maven//:io_swagger_swagger_annotations",
        "@maven//:org_apache_avro_avro",
        "@maven//:org_apache_kafka_kafka_clients",
        "@maven//:org_slf4j_slf4j_api",
    ],
    javacopts = [
        "-Xlint:-deprecation,-serial",
    ],
)

java_library(
    name = "kafka-avro-serializer",
    srcs = glob(["avro-serializer/src/main/java/**/*.java"]),
    visibility = ["//visibility:public"],
    deps = [
        ":kafka-schema-registry-client",
        "@confluent_common//:common-config",
        "@maven//:org_apache_avro_avro",
        "@maven//:org_apache_kafka_kafka_2_12",
        "@maven//:org_apache_kafka_kafka_clients",
    ],
    javacopts = [
        "-Xlint:-deprecation,-rawtypes,-unchecked",
    ],
)

java_library(
    name = "kafka-streams-avro-serde",
    srcs = glob(["avro-serde/src/main/java/**/*.java"]),
    visibility = ["//visibility:public"],
    deps = [
        ":kafka-avro-serializer",
        ":kafka-schema-registry-client",
        "@maven//:org_apache_avro_avro",
        "@maven//:org_apache_kafka_kafka_clients",
    ],
    javacopts = [
        "-Xlint:-unchecked",
    ],
)
