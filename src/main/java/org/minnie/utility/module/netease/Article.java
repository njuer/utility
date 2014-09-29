package org.minnie.utility.module.netease;

/**
 * 论坛文章列表
 * @author eazytec
 *http://bbs.caipiao.163.com/home.php?mod=space&uid=747026&do=thread&view=me&type=thread&from=space
 *http://bbs.caipiao.163.com/home.php?mod=space&uid=667895&do=thread&view=me&type=thread&from=space
 */
public class Article {
	
	private Integer threadId;//文章ID
	private String module;//模块，竞彩足球：zucai
	private String subject;//主题
	private String postTime;//发表时间
	private String author;//作者
	private Integer authorId;//作者ID
	private String category;//文章类别,recommendation:推荐
	private String content;//文章内容
	private String link;//文章链接
	
	public Integer getThreadId() {
		return threadId;
	}
	public void setThreadId(Integer threadId) {
		this.threadId = threadId;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getPostTime() {
		return postTime;
	}
	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Integer getAuthorId() {
		return authorId;
	}
	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	@Override
	public String toString() {
		return "Article [threadId=" + threadId + ", module=" + module
				+ ", subject=" + subject + ", postTime=" + postTime
				+ ", author=" + author + ", authorId=" + authorId
				+ ", category=" + category + ", content=" + content + ", link="
				+ link + "]";
	}
	
	
}