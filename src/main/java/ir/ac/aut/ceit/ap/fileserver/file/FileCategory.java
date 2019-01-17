package ir.ac.aut.ceit.ap.fileserver.file;

public enum FileCategory {
    AUDIO("files.png",
            "aif", "cda", "mid", "mp3", "mpa", "ogg", "wav", "wma", "wpl"),
    COMPRESSED("files.png",
            "7z", "arj", "deb", "pkg", "rar", "rpm", "tar.gz", "z", "zip"),
    EXECUTABLE("files.png",
            "apk", "bat", "bin", "cgi", "com", "exe", "gadget", "jar", "py", "wsf"),
    IMAGE("files.png",
            "ai", "bmp", "gif", "ico", "jpeg", "jpg", "png", "ps", "psd", "svg", "tif"),
    VIDEO("files.png",
            "3g2", "3gp", "avi", "flv", "h264", "m4v", "mkv", "mov", "mp4", "mpg", "rm", "swf", "vob", "wmv"),
    DOCUMENT("files.png",
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
