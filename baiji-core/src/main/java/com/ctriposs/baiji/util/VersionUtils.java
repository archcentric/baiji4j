package com.ctriposs.baiji.util;

import java.io.File;
import java.security.CodeSource;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Created by yqdong on 2014/9/25.
 */
public final class VersionUtils {

    private static final String DEFAULT_VERSION = "0.0.0.0";
    private static final String SPEC_VERSION_ATTRIBUTE = "Specification-Version";

    private VersionUtils() {
    }

    public static String getPackageVersion(Class<?> clazz) {
        String version = DEFAULT_VERSION;
        try {
            CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
            if (codeSource != null) {
                File file = new File(codeSource.getLocation().getFile());
                if (file.isFile()) {
                    JarFile jar = new JarFile(file);
                    Manifest manifest = jar.getManifest();
                    if (manifest != null) {
                        version = manifest.getMainAttributes().getValue(SPEC_VERSION_ATTRIBUTE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }
}
