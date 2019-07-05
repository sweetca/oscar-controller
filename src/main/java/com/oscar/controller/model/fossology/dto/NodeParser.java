package com.oscar.controller.model.fossology.dto;

import com.google.common.base.CharMatcher;
import com.oscar.controller.model.fossology.ParsedLicenseNode;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class NodeParser {

    final char separator = '/';
    final ParsedLicenseNode mainParent;
    final Map<String, List<SingleFileResultDto>> filesMapped;
    final List<NodePath> sortedPath;

    public NodeParser(ScanResultDto dto) {

        this.filesMapped = dto
                .getFileLicenses()
                .stream()
                .sorted(Comparator.comparing(SingleFileResultDto::getFile))
                .collect(groupingBy(
                        (f) -> {
                            int lastSplit = f.getFile().lastIndexOf(separator);
                            return f.getFile().substring(0, lastSplit);
                        },
                        toList()));
        this.sortedPath = filesMapped
                .keySet()
                .stream()
                .sorted(Comparator.comparingInt(p -> CharMatcher.is(separator).countIn(p)))
                .collect(Collectors.toList())
                .stream()
                .map(NodePath::new)
                .collect(toList());

        NodePath start = sortedPath.get(0);
        start.setProcessed(true);
        mainParent = new ParsedLicenseNode();
        mainParent.setPath(start.getPath());
        mainParent.setName(nameFromPath(start.getPath()));
    }

    String nameFromPath(String path) {
        return path.substring(path.lastIndexOf(separator) + 1, path.length());
    }

    void evalChildren(ParsedLicenseNode lastParent) {

        this.filesMapped.get(lastParent.getPath()).forEach(f -> {
            lastParent.setSize(100);

            ParsedLicenseNode n = new ParsedLicenseNode();
            n.setName(nameFromPath(f.getFile()));
            n.setPath(f.getFile());

            if (f.getOutput().getCopyright() != null) {
                n.getCopyrights().addAll(f.getOutput().getCopyright().stream().map(CopyrightResultDto::getContent).collect(Collectors.toList()));
            }
            if (f.getOutput().getNomos() != null) {
                n.getUniqueLicenses().addAll(f.getOutput().getNomos());
            }

            lastParent.getChildren().add(n);

            lastParent.setNbFiles(lastParent.getNbFiles() + 1);
            lastParent.setNbLevelFiles(lastParent.getNbLevelFiles() + 1);
            lastParent.getUniqueLicenses().addAll(n.getUniqueLicenses());
        });

        this.sortedPath.forEach(path -> {
            if (!path.isProcessed() && path.getPath().startsWith(lastParent.getPath() + separator)) {
                path.setProcessed(true);

                ParsedLicenseNode asParentNode = new ParsedLicenseNode();
                asParentNode.setPath(path.getPath());
                asParentNode.setName(nameFromPath(path.getPath()));

                lastParent.getChildren().add(asParentNode);
                lastParent.setSize(100);

                this.evalChildren(asParentNode);

                lastParent.setNbFiles(lastParent.getNbFiles() + asParentNode.getNbFiles());
                lastParent.setNbLevelDir(lastParent.getNbLevelDir() + 1);
                lastParent.getUniqueLicenses().addAll(asParentNode.getUniqueLicenses());
            }
        });
    }

    public ParsedLicenseNode result() {
        evalChildren(mainParent);
        return this.mainParent;
    }

    @Data
    class NodePath {

        private String path;
        private boolean processed = false;

        NodePath(String path) {
            this.path = path;
        }
    }
}
