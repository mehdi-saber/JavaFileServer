package ir.ac.aut.ceit.ap.fileserver.filesys;

public enum FileCategory {
    AUDIO("src/main/resources/files.png",
            "aif", "cda", "mid", "mp3", "mpa", "ogg", "wav", "wma", "wpl"),
    COMPRESSED("src/main/resources/files.png",
            "7z", "arj", "deb", "pkg", "rar", "rpm", "tar.gz", "z", "zip"),
    EXECUTABLE("src/main/resources/files.png",
            "apk", "bat", "bin", "cgi", "com", "exe", "gadget", "jar", "py", "wsf"),
    IMAGE("src/main/resources/files.png",
            "ai", "bmp", "gif", "ico", "jpeg", "jpg", "png", "ps", "psd", "svg", "tif"),
    VIDEO("src/main/resources/files.png",
            "3g2", "3gp", "avi", "flv", "h264", "m4v", "mkv", "mov", "mp4", "mpg", "rm", "swf", "vob", "wmv"),
    DOCUMENT("src/main/resources/files.png",
            "doc", "odt", "pdf", "rtf", "tex", "txt", "wks", "wpd");
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
