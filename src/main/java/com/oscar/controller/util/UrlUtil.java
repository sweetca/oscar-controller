package com.oscar.controller.util;

import com.oscar.controller.exceptions.OscarDataException;
import com.oscar.controller.model.component.Component;

public class UrlUtil {

    public static final String git_postfix = ".git";
    private static final String empty = "";
    private static final String https = "https://";
    private static final String gitlab_private_access_url = "https://gitlab-ci-token:%s@%s.git";
    private static final String github_private_access_url = "https://%s@%s.git";

    public static String privateAccessUrlGitLab(String token, String url) {
        url = url.replace(https, empty).replace(git_postfix, empty);
        return String.format(gitlab_private_access_url, token, url);
    }

    public static String privateAccessUrlGitHub(String token, String url) {
        url = url.replace(https, empty).replace(git_postfix, empty);
        return String.format(github_private_access_url, token, url);
    }

    public static String privateAccessUrl(Component component) {
        switch (component.getType()) {
            case github:
                return privateAccessUrlGitHub(component.getCredentials().getAccessToken(), component.getUrl());
            case gitlab:
                return privateAccessUrlGitLab(component.getCredentials().getAccessToken(), component.getUrl());
            default:
                throw new OscarDataException("Illegal type for privateAccessUrl");
        }
    }

}
