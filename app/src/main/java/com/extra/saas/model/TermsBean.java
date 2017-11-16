package com.extra.saas.model;

/**
 * Created by 戴尔 on 2017/11/16.
 */

public class TermsBean {
    private String term_id;
    private String name;
    private String slug;
    private String taxonomy;
    private String description;
    private String parent;
    private String count;
    private String path;
    private String seo_title;
    private String seo_keywords;
    private String seo_description;
    private String list_tpl;
    private String one_tpl;
    private String listorder;
    private String status;

    public String getTerm_id() {
        return term_id;
    }

    public void setTerm_id(String term_id) {
        this.term_id = term_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSeo_title() {
        return seo_title;
    }

    public void setSeo_title(String seo_title) {
        this.seo_title = seo_title;
    }

    public String getSeo_keywords() {
        return seo_keywords;
    }

    public void setSeo_keywords(String seo_keywords) {
        this.seo_keywords = seo_keywords;
    }

    public String getSeo_description() {
        return seo_description;
    }

    public void setSeo_description(String seo_description) {
        this.seo_description = seo_description;
    }

    public String getList_tpl() {
        return list_tpl;
    }

    public void setList_tpl(String list_tpl) {
        this.list_tpl = list_tpl;
    }

    public String getOne_tpl() {
        return one_tpl;
    }

    public void setOne_tpl(String one_tpl) {
        this.one_tpl = one_tpl;
    }

    public String getListorder() {
        return listorder;
    }

    public void setListorder(String listorder) {
        this.listorder = listorder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
