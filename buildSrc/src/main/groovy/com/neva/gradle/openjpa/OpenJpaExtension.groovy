package com.neva.gradle.openjpa

import org.gradle.api.file.FileCollection

class OpenJpaExtension {

    Boolean addDefaultConstructor = true
    String connectionDriverName
    Map<String, String> connectionProperties
    Boolean enforcePropertyRestrictions = false
    FileCollection files
    File persistenceXmlFile
    Map toolProperties = [:]

    Properties toProperties() {
        def result = new Properties()
        result.put("addDefaultConstructor", addDefaultConstructor)
        if (connectionDriverName) {
            result.put("connectionDriverName", connectionDriverName)
        }
        if (connectionProperties) {
            String propertiesAsString = connectionProperties.collect { key, value ->
                "$key=$value"
            }.join(",")
            result.put("connectionProperties", propertiesAsString)
        }
        result.put("enforcePropertyRestrictions", enforcePropertyRestrictions)
        if (persistenceXmlFile) {
            result.put("persistenceXmlFile", persistenceXmlFile.absolutePath)
        }
        result.putAll(toolProperties)
        return result;
    }

}