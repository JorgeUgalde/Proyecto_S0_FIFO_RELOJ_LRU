/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

/**
 * This class contains the information and methods about the object Page
 */
public class Page {
    private int pageFrame;
    private int pageNumber;
    private String comments;
    private int modificationBit;

    public Page(int pageFrame, int pageNumber, String comments) {
        this.pageFrame = pageFrame;
        this.pageNumber = pageNumber;
        this.comments = comments;
    }

    public Page(int pageNumber, String comments) {
        this.pageNumber = pageNumber;
        this.comments = comments;
    }
    
     public Page(int pageNumber, int modificationBit) {
        this.pageNumber = pageNumber;
        this.modificationBit = modificationBit;
    }

    public int getModificationBit() {
        return modificationBit;
    }

    public void setModificationBit(int modificationBit) {
        this.modificationBit = modificationBit;
    }

    public int getPageFrame() {
        return pageFrame;
    }

    public void setPageFrame(int pageFrame) {
        this.pageFrame = pageFrame;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return pageNumber + "    "  + comments;
    }
    
    
}
