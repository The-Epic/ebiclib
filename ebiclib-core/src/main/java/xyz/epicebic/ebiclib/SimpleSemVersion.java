package xyz.epicebic.ebiclib;

public class SimpleSemVersion {

    private int major;
    private int minor;
    private int patch;

    public SimpleSemVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * Parses a string representation of a SimpleSemVersion into a SimpleSemVersion object.
     *
     * @param version the string representation of the SimpleSemVersion
     * @return the parsed SimpleSemVersion object
     */
    public static SimpleSemVersion fromString(String version) {
        version = version.replaceAll("[^\\d.]", "");
        String[] parts = version.split("\\.");

        if (parts.length != 3) {
            throw new IllegalArgumentException("String must be in the format major.minor.patch");
        }

        SimpleSemVersion semVersion = null;
        try {
            semVersion = new SimpleSemVersion(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2]));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("String must be in the format major.minor.patch");
        }
        return semVersion;
    }

    /**
     * Check if the current SimpleSemVersion object is newer than the provided SimpleSemVersion object.
     *
     * @param other The SimpleSemVersion object to compare against.
     * @return Returns true if the current version is newer, false otherwise.
     */
    public boolean isNewerThan(SimpleSemVersion other) {
        if (this.major != other.major) {
            return this.major > other.major;
        } else if (this.minor != other.minor) {
            return this.minor > other.minor;
        } else if (this.patch != other.patch) {
            return this.patch > other.patch;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.major + "." + this.minor + "." + this.patch;
    }

}
