package org.springsalad.dsa.questions.findfile.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/***
 * find file name contains given string
 */

public class Solution {
    public static void  main (String ... args) {
        System.out.println("hello world!");
        testOne();
    }

    public static class File {
        private String name;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        private Directory parent;

        public Directory getParent() {
            return parent;
        }

        public void setParent(Directory parent) {
            this.parent = parent;
        }

        public String getFullPath() {
           return parent.getFullPath() +  name;
        }

        public boolean isNameContained(String subString){
            return name == null?  false: name.contains(subString);
        }

        public String toString(){
            return  getFullPath();
        }

    }


    public static class Directory {
        private String name;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        private List<File> files = new ArrayList<>();

        public List<File> getFiles() {
            return files;
        }

        private List<Directory> subDirectories = new ArrayList<>();

        public List<Directory> getSubDirectories() {
            return subDirectories;
        }


        private Directory parent;

        public void setParent(Directory parent) {
            this.parent = parent;
        }

        public String getFullPath() {
            return (parent == null ? "/" : parent.getFullPath()) + name + "/";
        }

        private List<File> findAllFilesNameContain(String subString) {

            if (files == null) {
                return Collections.emptyList();
            } else {
                return files.stream().filter((file -> {
                            return file.isNameContained(subString);
                })).collect(Collectors.toList());
            }
        }

        private List<File> findAllSubDirectoryContain(String subString) {
            if (subDirectories == null) {
                return Collections.emptyList();
            } else {
                return  subDirectories.stream().map( directory -> {
                    return directory.findAllFileNameContainDescent(subString);
                }).flatMap(Collection::stream).collect(Collectors.toList());
            }
        }

        public List<File> findAllFileNameContainDescent(String subString) {
            List<File> allFiles = new ArrayList<>();
            allFiles.addAll(this.findAllFilesNameContain(subString));
            allFiles.addAll(this.findAllSubDirectoryContain(subString));
            return allFiles;
        };



        public String toString() {
            return  name + "\n" +  ": { "  + "\n"
                    + (files == null? "":files.toString() ) + " \n, {{ \n"
                    + (subDirectories == null? "":subDirectories.toString()) + "\n}} \n}";
        }
    }

    public static class DirectoryHelper {
        public static void addFileToDir(Directory dir, String fileName) {

            List<File> files =  dir.getFiles();
            File aFile = new File();
            aFile.setName(fileName);
            aFile.setParent(dir);
            files.add(aFile);
        }

        public static void bindDir(Directory parent, Directory child){
            parent.getSubDirectories().add(child);
            child.setParent(parent);
        }
    }

// for searching or matching in terms

    private static Directory testSetup() {
        // set up test
        // 3 layer structure, layer two has two subdirectory

        Directory top = new Directory();
        top.setName("Iamtop");

        DirectoryHelper.addFileToDir(top, "filetop-aaa");
        DirectoryHelper.addFileToDir(top, "filetop-bbb");


        Directory subDirA = new Directory();
        subDirA.setName("subdirA");
        DirectoryHelper.bindDir(top, subDirA);
        DirectoryHelper.addFileToDir(subDirA, "filedirA-111");
        DirectoryHelper.addFileToDir(subDirA, "fileDirA-222");

        Directory subDirB = new Directory();
        subDirB.setName("subDirB");
        DirectoryHelper.bindDir(top, subDirB);
        DirectoryHelper.addFileToDir(subDirB, "filedirB-111");
        DirectoryHelper.addFileToDir(subDirB, "fileDirB-222");


        return  top;

    };

    public static void testOne() {

        Directory testData = testSetup();
        System.out.println(testData);
        List<File> result = testData.findAllFileNameContainDescent("ir");

        System.out.println("Test Result:");
        result.forEach(file -> {
            System.out.println(file.getFullPath());
        });
    }
}
