package ir.ac.aut.ceit.ap.fileserver.file;

public enum FileCategory {
    AUDIO("file.png",
            "aif", "cda", "mid", "mp3", "mpa", "ogg", "wav", "wma", "wpl"),
    COMPRESSED("file.png",
            "7z", "arj", "deb", "pkg", "rar", "rpm", "tar.gz", "z", "zip"),
    EXECUTABLE("file.png",
            "apk", "bat", "bin", "cgi", "com", "exe", "gadget", "jar", "py", "wsf"),
    IMAGE("file.png",
            "bmp", "gif", "jpeg", "jpg", "png", "tif"),
    VIDEO("file.png",
            "avi", "h264", "mkv", "mov", "mp4", "mpg", "wmv"),
    DOCUMENT("file.png",
            "rtf", "tex", "txt", "pdf");
    String[] extensions;
    String iconPath;

    FileCategory(String iconPath, String... extensions) {
        this.extensions = extensions;
        this.iconPath = iconPath;

    }

    public String getIconPath() {
        return iconPath;
    }

    public String[] getExtensions() {
        return extensions;
    }


}
