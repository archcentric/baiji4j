package com.ctriposs.ci

class ProjectVersion {
    final Integer major
    final Integer minor
    final String build
    final Boolean isSnapshot

    ProjectVersion(Integer major, Integer minor, String build, Boolean isSnapshot) {
        this.major = major
        this.minor = minor
        this.build = build
        this.isSnapshot = isSnapshot
    }

    @Override
    String toString() {
        String fullVersion = "$major.$minor"

        if (build) {
            fullVersion += ".$build"
        }
        
        if (isSnapshot) {
            fullVersion += "-SNAPSHOT"
        }
        
        fullVersion
    }
}
