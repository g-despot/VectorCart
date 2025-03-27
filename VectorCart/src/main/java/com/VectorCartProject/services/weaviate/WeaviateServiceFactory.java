package com.VectorCartProject.services.weaviate;

/**
 * Factory to get the appropriate Weaviate service implementation
 */
public class WeaviateServiceFactory {
    // Enum for client versions
    public enum ClientVersion {
        V5, V6
    }

    /**
     * Get a WeaviateService implementation
     * @param version the client version to use
     * @return WeaviateService implementation
     */
    public static WeaviateService getService(ClientVersion version) {
        switch (version) {
            case V5:
                return WeaviateV5Service.getInstance();
            case V6:
                return WeaviateV6Service.getInstance();
            default:
                throw new IllegalArgumentException("Unknown client version: " + version);
        }
    }

    /**
     * Get the V5 Weaviate service implementation
     * @return WeaviateV5Service instance
     */
    public static WeaviateV5Service getV5Service() {
        return WeaviateV5Service.getInstance();
    }

    /**
     * Get the V6 Weaviate service implementation
     * @return WeaviateV6Service instance
     */
    public static WeaviateV6Service getV6Service() {
        return WeaviateV6Service.getInstance();
    }
}