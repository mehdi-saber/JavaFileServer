package ir.ac.aut.ceit.ap.fileserver.file;

public enum FileCategory {
    AUDIO("file.png",
            "aif", "cda", "mid", "mp3", "mpa", "ogg", "wav", "wma", "wpl"),
    COMPRESSED("file.png",
            "7z", "arj", "deb", "pkg", "rar", "rpm", "tar.gz", "z", "zip"),
    EXECUTABLE("file.png",
            "apk", "bat", "bin", "cgi", "com", "exe", "gadget", "jar", "py", "wsf"),
    IMAGE("file.png",
            "ai", "bmp", "gif", "ico", "jpeg", "jpg", "png", "ps", "psd", "svg", "tif"),
    VIDEO("file.png",
            "3g2", "3gp", "avi", "flv", "h264", "m4v", "mkv", "mov", "mp4", "mpg", "rm", "swf", "vob", "wmv"),
    DOCUMENT("file.png",
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
